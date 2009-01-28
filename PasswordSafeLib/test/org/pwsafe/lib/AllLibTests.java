package org.pwsafe.lib;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllLibTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.pwsafe.lib");
		//$JUnit-BEGIN$
		suite.addTestSuite(I18HelperTest.class);
		suite.addTestSuite(UtilTest.class);
		suite.addTestSuite(PassphraseUtilsTest.class);
		//$JUnit-END$
		return suite;
	}

}
