/*
 * $Id: PwsFieldTest.java 404 2009-09-21 19:19:25Z roxon $
 * 
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.file;

import junit.framework.TestCase;


/**
 * Tests basic Object Methods for all PwsFields.
 * 
 * @author roxon
 *
 */
public class PwsFieldTypeTest extends TestCase {

	public void testValueOf () {
		for (PwsFieldTypeV3 aType : PwsFieldTypeV3.values()) {
			assertEquals(aType, PwsFieldTypeV3.valueOf(aType.getId()));
		}
		
	}
}
