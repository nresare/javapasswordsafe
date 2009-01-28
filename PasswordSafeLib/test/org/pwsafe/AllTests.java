package org.pwsafe;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pwsafe.lib.AllLibTests;
import org.pwsafe.lib.crypto.AllCryptoTests;
import org.pwsafe.lib.file.AllFileTests;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.pwsafe");
		//$JUnit-BEGIN$
		suite.addTest(AllLibTests.suite());
		suite.addTest(AllCryptoTests.suite());
		suite.addTest(AllFileTests.suite());
		//$JUnit-END$
		return suite;
	}

}
