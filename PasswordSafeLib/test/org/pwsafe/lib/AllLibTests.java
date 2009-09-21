/*
 * $Id$
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
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
