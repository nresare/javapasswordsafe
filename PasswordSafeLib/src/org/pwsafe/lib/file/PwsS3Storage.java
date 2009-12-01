/*
 * $Id:$
 * 
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.file;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.pwsafe.lib.Log;
import org.pwsafe.lib.crypto.SHA1;
import org.pwsafe.lib.exception.EndOfFileException;
import org.pwsafe.lib.exception.PasswordSafeException;
import org.pwsafe.lib.exception.UnsupportedFileVersionException;

import com.amazonaws.crypto.Base64;
import com.amazonaws.s3.S3;
import com.amazonaws.s3.S3Bucket;
import com.amazonaws.s3.S3Object;

/**
 * This is an implementation of the storage interface that uses S3
 * as the backend.
 * 
 * @author mtiller
 *
 */
public class PwsS3Storage implements PwsStorage {
	private static final Log LOG = Log.getInstance(PwsS3Storage.class.getPackage().getName());
	
	/**
	 * File extension of the <b>local</b> file holding the s3 
	 * account information.
	 */
	public static final String FILE_EXTENSION = ".pws3";
	
	/**
	 * A helper class to hold the Amazon S3 credentials.  This
	 * will probably be refactored as the handling of this
	 * information is improved.
	 * 
	 * Note that this class "scrambles" the bucket name.  This is
	 * not security related but rather to avoid name clashes since
	 * all buckets across all of S3 are in a single namespace (or
	 * at least so it would appear).
	 * 
	 * @author mtiller
	 *
	 */
	public static class AccountDetails {
		/** The plain unhashed bucket name */
		String bucketTitle;
		String keyId;
		String secretKey;
		private final String hashedBucket;
		public AccountDetails(String bucket, String id, String secret) {
			SHA1 sha1 = new SHA1();
			this.bucketTitle = bucket;
			this.keyId = id;
			this.secretKey = secret;
			byte [] bb = bucket.getBytes();
			byte [] kb = keyId.getBytes();
			byte [] sb = secretKey.getBytes();
			sha1.update( bb, 0, bb.length );
			sha1.update( kb, 0, kb.length );
			sha1.update( sb, 0, sb.length );
			sha1.finalize();
			String hash = Base64.encodeBytes(sha1.getDigest());
			/* trim the last char of the hash */
			hash = hash.substring(0, hash.length()-2);
			/* We don't want any slashes in the name because that
			 * confuses things.  Same with plus signs.
			 */
			hash = hash.replace('/', '_');
			hash = hash.replace('+', '-');
			this.hashedBucket = "jps3-"+hash+"-"+bucket;
			keyId = id;
			secretKey = secret;
		}
		/**
		 * Get a hashed form of the bucket name.
		 * @return The hashed form.
		 */
		public String getHashedName() {
			return hashedBucket;
		}
	}
	
	/**
	 * Group ID of the local S3 credentials store, in order
	 * to easily identify S3 in case of future extensions.  
	 */
	private static final String S3_LOCAL_GROUP_ID = "S3";
	
	/**
	 * S3 Object key inside the bucket.
	 */
	private static final String DEFAULT_KEY = "passwordSafeData.psafe3";
	
	/**
	 * This object provides the interface to S3.
	 */
	private S3 s3;

	/** These are the details about the amazon account required to access the
	 * S3 storage.  These can either be read from a local file or entered by
	 * the user in the case where the password safe is being initialized.
	 */
	private AccountDetails account;
	
	/** The filename used for local account details storage */
	private final String filename;

	/** The local PWsFile where S3 credentials are stored */
	private PwsFileV3 localFile;
	
	/**
	 * Constructs an instance of an Amazon S3 storage provider.
	 *FIXME: does too much, split up in a create new and open existing.
	 * 
	 * @param bucket The bucket name
	 * @param aFilename The filename the account information is stored in (if it exists) or
	 * the file to write it to if the storage is initialized.
	 * @param account The bucket name and access credentials for the S3 account.  These are
	 * only required if a new storage area is being initialized. Otherwise, they are
	 * read from the specified file.
	 */
	public PwsS3Storage(String aFilename, AccountDetails acc, String passphrase) throws IOException {
		this.filename = aFilename;
		
		File theFile = new File(aFilename);
		if (theFile.exists()) {
			try {
				localFile = new PwsFileV3(new PwsFileStorage(aFilename), passphrase);
				localFile.readAll();
				localFile.close();
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw new IOException(e1.getMessage());
			} catch (EndOfFileException e1) {
				throw new IOException(e1.getMessage());
			} catch (UnsupportedFileVersionException e1) {
				e1.printStackTrace();
				throw new IOException(e1.getMessage());
			}
			
			//TODO: for compatibility with other local access stores, filter for S3_GROUP_ID
			PwsRecordV3 theAccountRecord = (PwsRecordV3) localFile.getRecords().next();
			String bucket = theAccountRecord.getField(PwsRecordV3.TITLE).toString();
		    String keyId = theAccountRecord.getField(PwsRecordV3.USERNAME).toString();
		    String secretKey = theAccountRecord.getField(PwsRecordV3.PASSWORD).toString();
		    this.account = new AccountDetails(bucket, keyId, secretKey);
			/** Note the use of HTTPS in the connection. */
			s3 = new S3( S3.HTTPS_URL, account.keyId, account.secretKey );
		} else {
			this.account = acc;
			localFile = new PwsFileV3();
			localFile.setStorage(new PwsFileStorage(aFilename));
			localFile.setPassphrase(passphrase);
			PwsRecordV3 theAccountRecord;
			try {
				theAccountRecord = (PwsRecordV3) localFile.newRecord();
				theAccountRecord.setField(new PwsStringUnicodeField(PwsRecordV3.GROUP, S3_LOCAL_GROUP_ID));
				theAccountRecord.setField(new PwsStringUnicodeField(PwsRecordV3.TITLE, acc.bucketTitle));
				theAccountRecord.setField(new PwsStringUnicodeField(PwsRecordV3.USERNAME, acc.keyId));
				theAccountRecord.setField(new PwsStringUnicodeField(PwsRecordV3.PASSWORD, acc.secretKey));
				localFile.add(theAccountRecord);
			} catch (PasswordSafeException e) {
				throw new IOException(e.getMessage());
			}
			if (acc!=null && acc.bucketTitle!=null && acc.keyId!=null && acc.secretKey!=null) {
				/** Note the use of HTTPS in the connection. */
				s3 = new S3( S3.HTTPS_URL, account.keyId, account.secretKey );
				final String hash = account.getHashedName();
				S3Bucket aBucket = null;
				try {
					aBucket = s3.listBucket(hash);
				} catch (Exception e) {
					//probably no bucket - only log if unknown exception
					if (! e.getMessage().contains("NoSuchBucket"))
						LOG.error("unexpected Error on S3 Bucket opening:", e);
				}
				try {
					if (aBucket != null) {
						// FIXME: also allow the use of existing s3 safes, 
						// even without local credentials 
						LOG.debug1("Bucket "+hash+" found.");
					} else {
						LOG.debug1("Bucket "+hash+" not found, creating...");
						s3.createBucket(hash);
						LOG.debug1("...done");
					}
				} catch (Exception e) {
					throw new IOException("Couldn't create S3 bucket:"+e.getMessage());
				}
				localFile.save();
				localFile.close();
			} else {
				// FIXME: What to do?
				/* Nothing can be done...throw exception? */
				s3 = null;
				throw new IOException("S3 credentials required");
			}
		}
	}

	/**
	 * This method grabs the data from S3 (in one shot)
	 * and then constructs a ByteArrayInputStream for
	 * use at the PwsFile level.
	 *
	 */
	public byte[] load() throws IOException {
		try {
			/* Get the S3 object */
			S3Object obj = s3.getObject(account.getHashedName(), DEFAULT_KEY);
			/* Grab the associated data */
			String data = obj.getData();
			/* Decode the string into bytes */
			byte[] bytes = Base64.decode(data.getBytes());
			return bytes;
		} catch (Exception e) {
			throw new IOException("Unable to load bucket "+account.getHashedName()+": "+e.getMessage());
		}
	}

	/**
	 * This method saves all the data back to S3 (in one
	 * shot).
	 */
	public boolean save(byte[] bytes) {
		/* Turn the bytes into a String for S3 */
		String data = Base64.encodeBytes(bytes);
		try {
			/* Upload the S3 object */
			s3.putObjectInline(account.getHashedName(), DEFAULT_KEY, data);
			return true;
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return false;			
		}
	}
	
	/* (non-Javadoc)
	 * @see org.pwsafe.lib.file.PwsStorage#getIdentifier()
	 */
	public String getIdentifier() {
		return filename;
	}

	
	
	/** 
	 * S3 does not provide last modified information.
	 * 
	 * @see org.pwsafe.lib.file.PwsStorage#getModifiedDate()
	 * 
	 * @return null
	 */
	public Date getModifiedDate() {
		// an implementation could explicitly store a last modified Object to S3
		// and update after each modification.
		return null;
	}

	/**
	 * Tries to delete the password safe on S3 and the bucket
	 * containung it. 
	 * 
	 * @return true on successful deletion
	 * @return false if nothing was deleted
	 * @throws IOException if there is other data in the bucket
	 */
	public boolean delete () throws IOException {
		final String hash = account.getHashedName();
		try {
			if (myBucketExists()) {
				s3.deleteObject(hash, DEFAULT_KEY);
				s3.deleteBucket(hash);
				return true;
			} else {
				return false;
			}
		} catch (IOException anIoEx) {
			throw anIoEx;
		} catch (Exception anEx) {
			throw new IOException("Unable to delete bucket " + account.getHashedName()+": " + anEx.getMessage());

		}
	}

	/**
	 * Tries to delete the bucket, but throws an exception 
	 * if there is any data, including a password safe, stored in it.
	 * 
	 * @return true on successful deletion
	 * @return false if nothing was deleted
	 * @throws IOException if data is in the store
	 */
	public boolean deleteBucket () throws IOException {
		final String hash = account.getHashedName();
		try {
			if (myBucketExists()) {
				s3.deleteBucket(hash);
				return true;
			} else {
				return false;
			}
		
		} catch (IOException anIoEx) {
			throw anIoEx;
		} catch (Exception anEx) {
			throw new IOException("Unable to delete bucket " + account.getHashedName()+": " + anEx.getMessage());

		}
	}

	private boolean myBucketExists() {
		try {
			return s3.listBucket(account.getHashedName())!= null;
		} catch (Exception e) {
			return false;
		}
	}
}
