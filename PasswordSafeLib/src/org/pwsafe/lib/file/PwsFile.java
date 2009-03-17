/*
 * $Id$
 * 
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;

import org.pwsafe.lib.I18nHelper;
import org.pwsafe.lib.Log;
import org.pwsafe.lib.Util;
import org.pwsafe.lib.crypto.BlowfishPws;
import org.pwsafe.lib.crypto.SHA1;
import org.pwsafe.lib.exception.EndOfFileException;
import org.pwsafe.lib.exception.PasswordSafeException;
import org.pwsafe.lib.exception.UnsupportedFileVersionException;

/**
 * This is the base class for all variations of the PasswordSafe file format.
 * <p>
 * <tt>
 * <pre> +--------------+-----------+----------------------------------------------------+
 * |       Length | Name      | Description                                        |
 * +--------------+-----------+----------------------------------------------------+
 * |            8 | RandStuff | Random bytes                                       |
 * |           20 | RandHash  | Random hash                                        |
 * |           20 | Salt      | Salt                                               |
 * |            8 | IpThing   | Initial vector for decryption                      |
 * +--------------+-----------+----------------------------------------------------+</pre>
 * </tt>
 * </p><p>
 * The records follow immediately after the header.  Each record has the same
 * layout:
 * </p><p>
 * <tt>
 * <pre> +--------------+-----------+----------------------------------------------------+
 * |  BLOCK_LENGTH| RecLen    | Actual length of the data that follows (encrypted) |
 * |n*BLOCK_LENGTH| RecData   | The encrypted data.  The length is RecLen rounded  |
 * |              |           | up to be a multiple of BLOCK_LENGTH                |
 * +--------------+-----------+----------------------------------------------------+</pre>
 * </tt>
 * </p>
 */
public abstract class PwsFile  
{
	private static final Log LOG = Log.getInstance(PwsFile.class.getPackage().getName());

	/**
	 * Length of RandStuff in bytes.
	 */
	public static final int	STUFF_LENGTH	= 8;

	/**
	 * Length of RandHash in bytes.
	 */
	public static final int	HASH_LENGTH		= 20;

	/**
	 * Block length - the minimum size of a data block.  All data written to the database is
	 * in blocks that are an integer multiple of <code>BLOCK_LENGTH</code> in size. 
	 * The exception is time field, there the size used is 4.
	 */
	public static final int BLOCK_LENGTH	= 8; 

	/**
	 * This provides a wrapper around the <code>Iterator</code> that is returned by the
	 * <code>iterator()</code> method on the Collections class used to store the PasswordSafe
	 * records.  It allows us to mark the file as modified when records are deleted file
	 * using the iterator's <code>remove()</code> method.
	 */
	private class FileIterator implements Iterator
	{
		private final Log LOG = Log.getInstance(FileIterator.class.getPackage().getName());

		private final PwsFile		TheFile;
		private final Iterator	TheIterator;

		/**
		 * Construct the <code>Iterator</code> linking it to the given PasswordSafe
		 * file.
		 * 
		 * @param file the file this iterator is linked to.
		 * @param iter the <code>Iterator</code> over the records.
		 */
		public FileIterator( PwsFile file, Iterator iter )
		{
			LOG.enterMethod( "PwsFile$FileIterator" );

			TheFile		= file;
			TheIterator	= iter;
			
			LOG.leaveMethod( "PwsFile$FileIterator" );
		}

		/**
		 * Returns <code>true</code> if the iteration has more elements. (In other words, returns
		 * <code>true</code> if next would return an element rather than throwing an exception.)
		 * 
		 * @return <code>true</code> if the iterator has more elements.
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		public final boolean hasNext()
		{
			return TheIterator.hasNext();
		}

		/**
		 * Returns the next PasswordSafe record in the iteration.  The object returned will
		 * be a subclass of {@link PwsRecord}
		 * 
		 * @return the next element in the iteration.
		 * 
		 * @see java.util.Iterator#next()
		 */
		public final Object next()
		{
			return TheIterator.next();
		}

		/**
		 * Removes the last record returned by {@link PwsFile.FileIterator#next()} from the PasswordSafe
		 * file and marks the file as modified.
		 * 
		 * @see java.util.Iterator#remove()
		 */
		public final void remove()
		{
			LOG.enterMethod( "PwsFile$FileIterator.remove" );

			if (isReadOnly())
				LOG.error("Illegal remove on read only file - saving won't be possible");

			TheIterator.remove();
			TheFile.setModified();

			LOG.leaveMethod( "PwsFile$FileIterator.remove" );
		}
	}

	/** The storage implementation associated with this file */
	protected PwsStorage		storage = null;
	
	/**
	 * The passphrase for the file.
	 */
	protected String			passphrase		= null;

	/**
	 * The stream used to read data from the file.  It is non-null only whilst data
	 * are being read from the file.
	 */
	protected InputStream		inStream		= null;

	/**
	 * The stream used to write data to the file.  It is non-null only whilst data are
	 * being written to the file. 
	 */
	protected OutputStream		outStream		= null;

	/**
	 * The file's standard header.
	 */
	private PwsFileHeader		header			= null;

	/**
	 * The Blowfish object being used to encrypt or decrypt data as it is written to or
	 * read from the file. 
	 */
	private BlowfishPws			algorithm		= null;

	/**
	 * The records that are part of the file.
	 */
	protected ArrayList			recordSet		= new ArrayList();

	/**
	 * Flag indicating whether (<code>true</code>) or not (<code>false</code>) the file
	 * has been modified in memory and not yet written back to the filesystem.
	 */
	protected boolean			modified		= false;

	
	/**
	 * Flag indicating whether the file may be changed or saved. 
	 * 
	 */
	protected boolean 			readOnly 		= false;
	
	/**
	 * Last modification Date and time of the underlying storage.
	 */
	protected Date 				lastStorageChange;
	
	/**
	 * Constructs and initialises a new, empty PasswordSafe database in memory.
	 */
	protected PwsFile()
	{
		header	= new PwsFileHeader();
		storage = null;
	}

	/**
	 * Construct the PasswordSafe file by reading it from the file.
	 * 
	 * @param filename   the name of the database to open.
	 * @param aPassphrase the passphrase for the database.
	 * 
	 * @throws EndOfFileException
	 * @throws IOException
	 * @throws UnsupportedFileVersionException
	 * @throws NoSuchAlgorithmException if no SHA-1 implementation is found.
	 */
	protected PwsFile( PwsStorage aStorage, String aPassphrase )
	throws EndOfFileException, IOException, UnsupportedFileVersionException, NoSuchAlgorithmException
	{
		LOG.enterMethod( "PwsFile.PwsFile( String )" );
		this.storage = aStorage;
		open( aPassphrase );

		LOG.leaveMethod( "PwsFile.PwsFile( String )" );
	}

	/**
	 * Adds a record to the file.
	 * 
	 * @param rec the record to be added.
	 * 
	 * @throws PasswordSafeException if the record has already been added to another file. 
	 */
	public void add( PwsRecord rec )
	throws PasswordSafeException
	{
		LOG.enterMethod( "PwsFile.add" );

		if (isReadOnly())
			LOG.error("Illegal add on read only file - safing won't be possible");

		// TODO validate the record before adding it
		rec.setOwner( this );
		recordSet.add( rec );
		setModified();

		LOG.leaveMethod( "PwsFile.add" );
	}

	/**
	 * Allocates a byte array at least <code>length</code> bytes in length and which is an integer multiple
	 * of <code>BLOCK_LENGTH</code>.
	 * 
	 * @param length the number of bytes the array must hold.
	 * 
	 * @return A byte array of the correct length.
	 */
	static final byte [] allocateBuffer( int length )
	{
		LOG.enterMethod( "PwsFile.allocateBuffer" );

		int	bLen;

		bLen	= calcBlockLength( length );

		LOG.leaveMethod( "PwsFile.allocateBuffer" );

		return new byte[ bLen ];
	}

	/**
	 * Calculates the next integer multiple of <code>BLOCK_LENGTH</code> &gt;= <code>length</code>.
	 * If <code>length</code> is zero, however, then <code>BLOCK_LENGTH</code> is returned as the
	 * calculated block length.
	 * 
	 * @param length the minimum block length 
	 * 
	 * @return <code>length</code> rounded up to the next multiple of <code>BLOCK_LENGTH</code>.
	 * 
	 * @throws IllegalArgumentException if length &lt; zero.
	 */
	static final int calcBlockLength( int length )
	{
		LOG.enterMethod( "PwsFile.calcBlockLength" );

		int result;

		if ( length < 0 )
		{
			LOG.error( I18nHelper.getInstance().formatMessage("E00004") );
			LOG.leaveMethod( "PwsFile.calcBlockLength" );
			throw new IllegalArgumentException( I18nHelper.getInstance().formatMessage("E00004") );
		}
		result = ( length == 0 ) ? BLOCK_LENGTH : ( (length + (BLOCK_LENGTH - 1)) / BLOCK_LENGTH ) * BLOCK_LENGTH;
		
		LOG.debug1( "Length = " + length + ", BlockLength = " + result );

		LOG.leaveMethod( "PwsFile.calcBlockLength" );

		return result;
	}

	/**
	 * Attempts to close the file.
	 * 
	 * @throws IOException If the attempt fails.
	 */
	void close()
	throws IOException
	{
		LOG.enterMethod( "PwsFile.close" );

		if ( inStream != null )
		{	
			inStream.close();
	
			inStream	= null;
			algorithm	= null;
		}

		LOG.leaveMethod( "PwsFile.close" );
	}

	/**
	 * Returns the storage implementation for this file
	 */
	public PwsStorage getStorage() {
		return storage;
	}
	
	/**
	 * Allow the storage implementation associated with this file to be set.
	 * @param storage An implementation of the PwsStorage interface.
	 */
	public void setStorage(PwsStorage storage) {
		this.storage = storage;
	}

	/**
	 * Returns the major version number for the file.
	 * 
	 * @return The major version number for the file.
	 */
	public abstract int getFileVersionMajor();

	/**
	 * Returns the file header.
	 * 
	 * @return The file header.
	 */
	PwsFileHeader getHeader()
	{
		return header;
	}

	/**
	 * Returns the passphrase used to open the file.
	 * 
	 * @return The file's passphrase.
	 */
	public String getPassphrase()
	{
		return passphrase;
	}

	/**
	 * Returns the number of records in the file.
	 * 
	 * @return The number of records in the file.
	 */
	public int getRecordCount()
	{
		LOG.enterMethod( "PwsFile.getRecordCount" );
		
		int size = recordSet.size();

		LOG.leaveMethod( "PwsFile.getRecordCount" );

		return size;
	}

	/**
	 * Returns an iterator over the records.  Records may be deleted from the file by
	 * calling the <code>remove()</code> method on the iterator.
	 * 
	 * @return An <code>Iterator</code> over the records.
	 */
	public Iterator<? extends PwsRecord> getRecords()
	{
		return new FileIterator( this, recordSet.iterator() );
	}

	/**
	 * Returns an flag as to whether this file or any of its records have been modified.
	 * 
	 * @return <code>true</code> if the file has been modified, <code>false</code> if it hasn't.
	 */
	public boolean isModified()
	{
		return modified;
	}

	/**
	 * Constructs and initialises the blowfish encryption routines ready to decrypt or
	 * encrypt data.
	 * 
	 * @param aPassphrase
	 * 
	 * @return A properly initialised {@link BlowfishPws} object.
	 */
	private BlowfishPws makeBlowfish( byte [] aPassphrase )
	{
		SHA1	sha1;
		byte	salt[];
		
		sha1 = new SHA1();
		salt = header.getSalt();

		sha1.update( aPassphrase, 0, aPassphrase.length );
		sha1.update( salt, 0, salt.length );
		sha1.finalize();

		return new BlowfishPws( sha1.getDigest(), header.getIpThing() );
	}

	/**
	 * Allocates a new, empty record unowned by any file.
	 * 
	 * @return A new empty record
	 */
	public abstract PwsRecord newRecord();

	/**
	 * Opens the database.
	 * 
	 * @param file       the <code>File</code> to read from
	 * @param aPassphrase the passphrase for the file.
	 * 
	 * @throws EndOfFileException
	 * @throws IOException
	 * @throws UnsupportedFileVersionException
	 * @throws NoSuchAlgorithmException if no SHA-1 implementation is found.
	 */
	protected void open( String aPassphrase )
	throws EndOfFileException, IOException, UnsupportedFileVersionException, NoSuchAlgorithmException
	{
		LOG.enterMethod( "PwsFile.init" );

		passphrase		= aPassphrase;
		
		if (storage!=null) {
			inStream		= new ByteArrayInputStream(storage.load());
			lastStorageChange = storage.getModifiedDate();
		}
		header			= new PwsFileHeader( this );
		algorithm		= makeBlowfish( aPassphrase.getBytes() );

		readExtraHeader( this );

		LOG.leaveMethod( "PwsFile.init" );
	}

	/**
	 * Reads all records from the file.
	 * 
	 * @throws IOException  If an error occurs reading from the file.
	 * @throws UnsupportedFileVersionException  If the file is an unsupported version
	 */
	void readAll()
	throws IOException, UnsupportedFileVersionException
	{
		try
		{
			for ( ;; )
			{
				readRecord();
			}
		}
		catch ( EndOfFileException e )
		{
			// OK
		}
	}

	/**
	 * Allocates a block of <code>BLOCK_LENGTH</code> bytes then reads and decrypts this many
	 * bytes from the file.
	 * 
	 * @return A byte array containing the decrypted data.
	 * 
	 * @throws EndOfFileException If end of file occurs whilst reading the data.
	 * @throws IOException        If an error occurs whilst reading the file.
	 */
	protected byte [] readBlock()
	throws EndOfFileException, IOException
	{
		byte []	block;

		block = new byte[ getBlockSize() ];
		readDecryptedBytes( block );
		
		return block;
	}

	/**
	 * Reads raw (undecrypted) bytes from the file.  The method attepts to read
	 * <code>bytes.length</code> bytes from the file.
	 * 
	 * @param bytes the array to be filled from the file.
	 * 
	 * @throws EndOfFileException If end of file occurs whilst reading the data.
	 * @throws IOException        If an error occurs whilst reading the file.
	 */
	public void readBytes( byte [] bytes )
	throws IOException, EndOfFileException
	{
		int count;

		count = inStream.read( bytes );

		if ( count == -1 )
		{
			LOG.debug1( "END OF FILE" );
			throw new EndOfFileException();
		}
		else if ( count < bytes.length )
		{
			LOG.info( I18nHelper.getInstance().formatMessage("I00003", new Object [] { new Integer(bytes.length), new Integer(count) } ) );
			throw new IOException( I18nHelper.getInstance().formatMessage("E00006") );
		}
		LOG.debug1( "Read " + count + " bytes" );
	}

	/**
	 * Reads bytes from the file and decryps them.  <code>buff</code> may be any length provided
	 * that is a multiple of <code>getBlockSize()</code> bytes in length.
	 * 
	 * @param buff the buffer to read the bytes into.
	 * 
	 * @throws EndOfFileException If end of file has been reached.
	 * @throws IOException If a read error occurs.
	 * @throws IllegalArgumentException If <code>buff.length</code> is not an integral multiple of <code>BLOCK_LENGTH</code>.
	 */
	public void readDecryptedBytes( byte [] buff )
	throws EndOfFileException, IOException
	{
		if ( (buff.length == 0) || ((buff.length % getBlockSize()) != 0) )
		{
			throw new IllegalArgumentException( I18nHelper.getInstance().formatMessage("E00001") );
		}
		readBytes( buff );
		try {
			algorithm.decrypt( buff );
		} catch (PasswordSafeException e) {
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Reads any additional header from the file.  Subclasses should override this a necessary
	 * as the default implementation does nothing.
	 * 
	 * @param file the {@link PwsFile} instance to read the header from.
	 * 
	 * @throws EndOfFileException              If end of file is reached.
	 * @throws IOException                     If an error occurs while reading the file.
	 * @throws UnsupportedFileVersionException If the file's version is unsupported.
	 */
	protected void readExtraHeader( PwsFile file )
	throws EndOfFileException, IOException, UnsupportedFileVersionException
	{
	}

	/**
	 * Reads a single record from the file.  The correct subclass of PwsRecord is
	 * returned depending on the version of the file.
	 * 
	 * @return The record read from the file.
	 * 
	 * @throws EndOfFileException When end-of-file is reached.
	 * @throws IOException
	 * @throws UnsupportedFileVersionException If this version of the file cannot be handled.
	 */
	protected PwsRecord readRecord()
	throws EndOfFileException, IOException, UnsupportedFileVersionException
	{
		PwsRecord	rec;

		rec = PwsRecord.read( this );

		if ( rec.isValid() )
		{	
			recordSet.add( rec );
		}

		return rec;
	}

	/**
	 * Deletes the given record from the file.
	 * 
	 * @param rec the record to be deleted.
	 */
	void removeRecord( PwsRecord rec )
	{
		LOG.enterMethod( "PwsFile.removeRecord" );

		if ( recordSet.contains(rec) )
		{
			LOG.debug1( "Record removed" );
			recordSet.remove( rec );
			setModified();
		}
		else
		{
			LOG.debug1( "Record not removed - it is not part of this file" );
		}

		LOG.leaveMethod( "PwsFile.removeRecord" );
	}

	/**
	 * Writes this file back to the filesystem.  If successful the modified flag is also 
	 * reset on the file and all records.
	 * 
	 * @throws IOException if the attempt fails.
	 * @throws NoSuchAlgorithmException if no SHA-1 implementation is found.
	 * @throws ConcurrentModificationException if the underlying store was 
	 * independently changed  
	 */
	public void save()
	throws IOException, NoSuchAlgorithmException, ConcurrentModificationException
	{
		if (isReadOnly())
			throw new IOException("File is read only");

		if (lastStorageChange != null && // check for concurrent change
			storage.getModifiedDate().after(lastStorageChange)) {
			throw new ConcurrentModificationException("Password store was changed independently - no save possible!");
		}

		// For safety we'll write to a temporary file which will be renamed to the
		// real name if we manage to write it successfully.

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		outStream	= baos;

		try
		{
			header.save( this );

			// Can only be created once the V1 header's been written.

			algorithm	= makeBlowfish( passphrase.getBytes() );

			writeExtraHeader( this );

			PwsRecord	rec;
			for ( Iterator<?> iter = getRecords(); iter.hasNext(); )
			{
				rec = (PwsRecord) iter.next();
	
				rec.saveRecord( this );
			}
	
			outStream.close();
	
			if (storage.save(baos.toByteArray())) {
				modified = false;
				lastStorageChange = storage.getModifiedDate();
			}
			else
			{
				// FIXME: I'm not sure what this message should be, but it should
				// reflect the fact that storage failed, not anything about a temp file.
				LOG.error( I18nHelper.getInstance().formatMessage("E00010", new Object [] { "Storage file" } ) );
				// TODO Throw an exception here?
				return;
			}
		} catch (IOException e) {
			try {
				outStream.close();
			} catch (Exception e2) {
				// do nothing we're going to throw the original exception
			}
			throw e;
		} finally {
			outStream = null;
			algorithm = null;
		}
	}

	/**
	 * Set the flag to indicate that the file has been modified.  There should not normally
	 * be any reason to call this method as it should be called indirectly when a record is
	 * added, changed or removed.
	 */
	protected void setModified()
	{
		modified = true;
	}

	/**
	 * Sets the passphrase that will be used to encrypt the file when it is saved.
	 * 
	 * @param pass
	 */
	public void setPassphrase( String pass )
	{
		passphrase	= pass;
	}

	/**
	 * Writes unencrypted bytes to the file.
	 * 
	 * @param buffer the data to be written.
	 * 
	 * @throws IOException
	 */
	public void writeBytes( byte [] buffer )
	throws IOException
	{
		outStream.write( buffer );
		LOG.debug1( "Wrote " + buffer.length + " bytes" );
	}

	/**
	 * Encrypts then writes the contents of <code>buff</code> to the file.
	 * 
	 * @param buff the data to be written.
	 * 
	 * @throws IOException
	 */
	public void writeEncryptedBytes( byte [] buff )
	throws IOException
	{
		if ( (buff.length == 0) || ((buff.length % getBlockSize()) != 0) )
		{
			throw new IllegalArgumentException( I18nHelper.getInstance().formatMessage("E00001") );
		}
		
		byte [] temp = Util.cloneByteArray( buff );
		try {
			algorithm.encrypt( temp );
		} catch (PasswordSafeException e) {
			LOG.error(e.getMessage());
		}
		writeBytes( temp );
	}

	/**
	 * Writes any additional header.  This default implementation does nothing.  Subclasses 
	 * should override this as necessary. 
	 * 
	 * @param file
	 * 
	 * @throws IOException
	 */
	protected void writeExtraHeader( PwsFile file )
	throws IOException
	{
	}
	
	/** 
	 * Returns the size of blocks in this file type.
	 * 
	 * @return the size of blocks in this file type as an int
	 */
	abstract int getBlockSize();

	/**
	 * May this file be changed?
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * Sets whether this file may be changed.
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
}
