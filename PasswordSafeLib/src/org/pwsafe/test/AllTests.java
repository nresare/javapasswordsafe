/*
 * $Id$
 * 
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 */
public class AllTests
{
	/**
	 * Returns a JUnit test suite containing all valid test cases.
	 * 
	 * @return A JUnit test suite containing all valid test cases.
	 */
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for org.pwsafe.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(PassphraseUtilsTest.class);
		suite.addTestSuite(UtilTest.class);
		suite.addTestSuite(FileTest.class);
		//$JUnit-END$
		return suite;
	}
}