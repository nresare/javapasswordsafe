/*
 * $Id$
 * 
 * Copyright (c) 2008-2011 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.util;

import junit.framework.TestCase;

public class PasswordPolicyTest extends TestCase {
	public void testValueOf() {
		PassphrasePolicy pol = PassphrasePolicy.valueOf("FFFFFFF000FFF000FFF");
		assertTrue(pol.digitChars);
		assertTrue(pol.lowercaseChars);
		assertTrue(pol.uppercaseChars);
		assertTrue(pol.symbolChars);
		assertTrue(pol.easyview);
		assertEquals(Integer.parseInt("FFF", 16), pol.length);

		pol = PassphrasePolicy.valueOf("0000000FFF000FFF000");
		assertFalse(pol.digitChars);
		assertFalse(pol.lowercaseChars);
		assertFalse(pol.uppercaseChars);
		assertFalse(pol.symbolChars);
		assertFalse(pol.easyview);
		assertEquals(0, pol.length);

		// String krims = Integer.toBinaryString(Integer.parseInt("80C0",16));
		// String krams = Integer.toBinaryString(Integer.parseInt("8888",16));
		pol = PassphrasePolicy.valueOf("0800008FFF000FFF000");
		assertFalse(pol.digitChars);
		assertFalse(pol.lowercaseChars);
		assertFalse(pol.uppercaseChars);
		assertFalse(pol.symbolChars);
		assertFalse(pol.easyview);
		assertEquals(8, pol.length);

	}
}
