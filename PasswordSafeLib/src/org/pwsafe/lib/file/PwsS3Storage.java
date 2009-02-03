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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import org.pwsafe.lib.Log;
import org.pwsafe.lib.crypto.SHA1;

import com.amazonaws.crypto.Base64;
import com.amazonaws.s3.S3;
import com.amazonaws.s3.S3BucketList;
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
		private String hashedBucket;
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
	
	private static final String DEFAULT_KEY = "passwordSafeData.dat";
	
	/**
	 * This object provides the interface to S3.
	 */
	private S3 s3;

	/** These are the details about the amazon account required to access the
	 * S3 storage.  These can either be read from a local file or entered by
	 * the user in the case where the password safe is being initialized.
	 */
	private AccountDetails account;
	
	/** Keep a copy of the passphrase */
	private String passphrase;
	
	/**
	 * Constructs an instance of an Amazon S3 storage provider.
	 * @param bucket The bucket name
	 * @param filename The filename the account information is stored in (if it exists) or
	 * the file to write it to if the storage is initialized.
	 * @param account The bucket name and access credentials for the S3 account.  These are
	 * only required if a new storage area is being initialized.  Otherwise, they are
	 * read from the specified file.
	 */
	public PwsS3Storage(String filename, AccountDetails acc, String passphrase) throws IOException {
		this.passphrase = passphrase;
		File f = new File(filename);
		if (f.exists()) {
			FileInputStream fin = new FileInputStream(filename);
			CryptoInputStream cis = new CryptoInputStream(this.passphrase, fin);
			InputStreamReader isr = new InputStreamReader(cis);
			BufferedReader br = new BufferedReader(isr);
			String bucket = br.readLine();
		    String keyId = br.readLine();
		    String secretKey = br.readLine();
		    cis.close();
		    this.account = new AccountDetails(bucket, keyId, secretKey);
			/** Note the use of HTTPS in the connection. */
			s3 = new S3( S3.HTTPS_URL, account.keyId, account.secretKey );
		} else {
			this.account = acc;
			if (acc!=null && acc.bucketTitle!=null && acc.keyId!=null && acc.secretKey!=null) {
				/** Note the use of HTTPS in the connection. */
				s3 = new S3( S3.HTTPS_URL, account.keyId, account.secretKey );
				String hash = account.getHashedName();
				S3BucketList bl = null;
				try {
					bl = s3.listMyBuckets();
				} catch (Exception e) {
					throw new IOException("Couldn't open S3 connection");
				}
				try {
					Vector v = bl.getBuckets();
					if (v.contains(hash)) {
						LOG.debug1("Bucket "+hash+" found.");
					} else {
						LOG.debug1("Bucket "+hash+" not found, creating...");
						s3.createBucket(hash);
						LOG.debug1("...done");
					}
				} catch (Exception e) {
					throw new IOException("Couldn't create S3 bucket:"+e.getMessage());
				}

				FileOutputStream fos = new FileOutputStream(filename);
				CryptoOutputStream cos = new CryptoOutputStream(passphrase, fos);
				String output = account.bucketTitle+"\n"+account.keyId+"\n"+account.secretKey+"\n\n";
				byte[] bytes = output.getBytes();
				cos.write(bytes);	
				cos.close();
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
	
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

}
