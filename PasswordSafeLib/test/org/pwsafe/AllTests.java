/*
 * $Id$
 * Copyright (c) 2008-2011 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pwsafe.lib.AllLibTests;
import org.pwsafe.lib.crypto.AllCryptoTests;
import org.pwsafe.lib.datastore.AllDataStoreTests;
import org.pwsafe.lib.file.AllFileTests;
import org.pwsafe.util.AllUtilTests;

public class AllTests {

	public static Test suite() {
		final TestSuite suite = new TestSuite("Tests for org.pwsafe");
		// $JUnit-BEGIN$
		suite.addTest(AllLibTests.suite());
		suite.addTest(AllCryptoTests.suite());
		suite.addTest(AllFileTests.suite());
		suite.addTest(AllDataStoreTests.suite());
		suite.addTest(AllUtilTests.suite());
		// $JUnit-END$
		return suite;
	}

}
