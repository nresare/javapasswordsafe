package org.pwsafe.lib.file;

import java.io.File;

import junit.framework.TestCase;

import org.bouncycastle.util.Arrays;
import org.junit.Ignore;
import org.pwsafe.lib.file.PwsS3Storage.AccountDetails;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;

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
@Ignore
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
			final AmazonS3Client s3 = new AmazonS3Client(
					new BasicAWSCredentials(s3Account.keyId, s3Account.secretKey));
			String hash = s3Account.getHashedName();
			if (s3.doesBucketExist(hash)) {
				for (S3ObjectSummary summary : s3.listObjects(hash).getObjectSummaries()) {
					s3.deleteObject(s3Account.getHashedName(), summary.getKey());
				}

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
