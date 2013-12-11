/*
 * $Id:$
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.crypto;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllCryptoTests {

	public static Test suite() {
		final TestSuite suite = new TestSuite("Test for org.pwsafe.lib.crypto");
		// $JUnit-BEGIN$
		suite.addTestSuite(BlowfishPwsTest.class);
		suite.addTestSuite(TwofishPwsTest.class);
		suite.addTestSuite(HmacPwsTest.class);
		suite.addTestSuite(SHA256PwsTest.class);
		// $JUnit-END$
		return suite;
	}

}
