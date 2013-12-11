package org.pwsafe.lib.file;

import java.io.File;
import java.util.Vector;

import junit.framework.TestCase;

import org.bouncycastle.util.Arrays;
import org.pwsafe.lib.file.PwsS3Storage.AccountDetails;

import com.amazonaws.s3.S3;
import com.amazonaws.s3.S3Bucket;

/**
 * This test is NOT part of the standard test suite. In order to use it, add the
 * following java properties to the run configuration:
 * <ul>
 * <li>-Djpwsafe.s3.bucket=</li>
 * <li>-Djpwsafe.s3.accessKey=</li>
 * <li>-Djpwsafe.s3.accessSecret=</li>
 * </ul>
 * 
 * Do <b>NOT</b> use your normal S3 credentials, especially not your bucket, as
 * this test deletes the bucket!
 * 
 * @author roxon
 * 
 */
public class S3FileTest extends TestCase {

	private static final String PASSPHRASE = "test";
	private static final String TEST_FILE_NAME = "s3test.pws3";
	byte[] someBytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	AccountDetails s3Account;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		s3Account = new AccountDetails(System.getProperty("jpwsafe.s3.bucket"),
				System.getProperty("jpwsafe.s3.accessKey"),
				System.getProperty("jpwsafe.s3.accessSecret"));
	}

	@Override
	protected void tearDown() throws Exception {
		final File theFile = new File(TEST_FILE_NAME);
		theFile.delete();

		if (s3Account != null) {
			final S3 s3 = new S3(S3.HTTPS_URL, s3Account.keyId, s3Account.secretKey);
			S3Bucket bucket = null;
			try {
				bucket = s3.listBucket(s3Account.getHashedName());
			} catch (final Exception e) {
				// ok - already gone
			}
			if (bucket != null) {
				final Vector names = bucket.getNames();
				for (int i = 0; i < names.size(); i++) {
					final Object o = names.get(i);
					s3.deleteObject(bucket.getBucketName(), o.toString());
				}
				s3.deleteBucket(bucket.getBucketName());
			}
		}

		super.tearDown();
	}

	public void testStorage() throws Exception {
		final PwsS3Storage theStore = new PwsS3Storage(TEST_FILE_NAME, s3Account, PASSPHRASE);
		assertNotNull(theStore);
	}

	public void testSimpleIo() throws Exception {
		final PwsS3Storage theStore = new PwsS3Storage(TEST_FILE_NAME, s3Account, PASSPHRASE);
		theStore.save(someBytes);
		final byte[] result = theStore.load();
		assertTrue(Arrays.areEqual(someBytes, result));
		assertTrue(theStore.delete());
	}

	public void testLoad() throws Exception {
		final PwsS3Storage theStore = new PwsS3Storage(TEST_FILE_NAME, s3Account, PASSPHRASE);
		final PwsFileV3 theFile = new PwsFileV3();

		theFile.setPassphrase(PASSPHRASE);
		theFile.setStorage(theStore);
		theFile.save();
		theFile.close();

		final PwsFileV3 theFile2 = new PwsFileV3(theStore, PASSPHRASE);
		assertNotNull(theFile2);
		final PwsRecord record = theFile2.newRecord();
		record.setField(new PwsStringUnicodeField(PwsRecordV3.TITLE, "test"));
		record.setField(new PwsStringUnicodeField(PwsRecordV3.GROUP, "test"));
		record.setField(new PwsStringUnicodeField(PwsRecordV3.PASSWORD, "test"));
		theFile2.add(record);
		theFile2.save();
		theFile2.close();

		final PwsFileV3 theFile3 = new PwsFileV3(theStore, PASSPHRASE);
		theFile3.readAll();
		theFile3.close();
		final PwsRecord record2 = theFile3.getRecords().next();
		assertEquals("test", record2.getField(PwsRecordV3.TITLE).getValue());
		assertEquals("test", record2.getField(PwsRecordV3.GROUP).getValue());
		assertEquals("test", record2.getField(PwsRecordV3.PASSWORD).getValue());
	}

}
