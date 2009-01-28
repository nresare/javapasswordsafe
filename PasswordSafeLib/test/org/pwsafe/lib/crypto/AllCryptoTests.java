package org.pwsafe.lib.crypto;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllCryptoTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.pwsafe.lib.crypto");
		//$JUnit-BEGIN$
		suite.addTestSuite(TwofishPwsTest.class);
		suite.addTestSuite(HmacPwsTest.class);
		suite.addTestSuite(SHA256PwsTest.class);
		//$JUnit-END$
		return suite;
	}

}
