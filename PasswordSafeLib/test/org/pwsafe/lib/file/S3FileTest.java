package org.pwsafe.lib.file;

import junit.framework.TestCase;

import org.bouncycastle.util.Arrays;
import org.pwsafe.lib.file.PwsS3Storage.AccountDetails;

/**
 * This test is NOT part of the standard test suite.
 * In order to use it, add the following java properties 
 * to the run configuration: <ul>
 * <li>-Djpwsafe.s3.bucket= </li>
 * <li>-Djpwsafe.s3.accessKey= </li>
 * <li>-Djpwsafe.s3.accessSecret= </li>
 * </ul>
 * 
 * @author roxon
 *
 */
public class S3FileTest extends TestCase {

	private static final String PASSPHRASE = "test";
	private static final String TEST_FILE_NAME = "s3test.pws3";
	byte[] someBytes = new byte[] {0,1,2,3,4,5,6,7,8,9};
	
	AccountDetails s3Account;
	
	protected void setUp() throws Exception {
		super.setUp();
		s3Account = new AccountDetails(System.getProperty("jpwsafe.s3.bucket"), System.getProperty("jpwsafe.s3.accessKey"), System.getProperty("jpwsafe.s3.accessSecret"));
	}

	public void testStorage() throws Exception {
		PwsS3Storage theStore = new PwsS3Storage(TEST_FILE_NAME, s3Account, PASSPHRASE);
		assertNotNull(theStore);
	}

	public void testSimpleIo() throws Exception {
		PwsS3Storage theStore = new PwsS3Storage(TEST_FILE_NAME, s3Account, PASSPHRASE);
		theStore.save(someBytes);
		byte [] result = theStore.load();
		assertTrue(Arrays.areEqual(someBytes, result));
		
//		assertTrue(theStore.deleteBucket());
	}
	
	public void testLoad() throws Exception {
		PwsS3Storage theStore = new PwsS3Storage(TEST_FILE_NAME, s3Account, PASSPHRASE);
		PwsFileV3 theFile = new PwsFileV3();
		
		theFile.setPassphrase(PASSPHRASE);
		theFile.setStorage(theStore);
		theFile.save();
		
		PwsFileV3 theFile2 = new PwsFileV3(theStore, PASSPHRASE);
		assertNotNull (theFile2);
	}


}
