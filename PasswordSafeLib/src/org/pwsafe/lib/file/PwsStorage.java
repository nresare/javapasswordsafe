package org.pwsafe.lib.file;

import java.io.IOException;

/**
 * This interface is an abstraction of the storage mechanism.  The idea is that
 * each potential medium that the information could be stored (e.g. a file)
 * will have an associated provider implementation (e.g. PwsFileStorage).
 * 
 * In many ways, this interface is a simplified combination of InputStream
 * and OutputStream.
 * 
 * Note that all bytes handled by IO functions in this interface are
 * <b>already encrypted</b>.  This interface does not handle any unencrypted
 * data.
 * 
 * @author mtiller
 *
 */
public interface PwsStorage {
	/**
	 * This method provides an input stream that can be used 
	 * @return
	 * @throws IOException
	 */
	//public InputStream getInputStream() throws IOException;
	
	public byte[] load() throws IOException;
	
	/**
	 * This method takes a series of bytes as input and then attempts
	 * to save them to the underlying storage provider.  It returns
	 * true if the save was succesful and false otherwise.
	 * 
	 * Note that this interface does not care what version or format the
	 * file is.  That is handled at the PwSFile layer.
	 * 
	 * TODO Should this throw an exception instead?
	 * 
	 * @param data The bytes making up the PasswordSafe file
	 * @return true if save was successful
	 * @throws IOException
	 */
	public boolean save(byte[] data);
	
	/**
	 * This method is used by storage schemes (S3) that utilitze a passphase <b>
	 * in addition to the actual password file</b>.
	 * @param passphrase
	 */
	public void setPassphrase(String passphrase);
}
