/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.crypto;

import org.pwsafe.lib.Util;

import junit.framework.TestCase;

/**
 * Test HMAC operation.
 * 
 * @author Glen Smith
 */
public class HmacPwsTest extends TestCase {

	
    public void testDigest() {
        
        String key = "Jefe";
        String data = "what do ya want for nothing?";
        HmacPws hash = new HmacPws(key.getBytes());
        hash.digest(data.getBytes());
        byte[] hmac = hash.doFinal();
        
        String result = Util.bytesToHex(hmac);
        assertEquals("5bdcc146bf60754e6a042426089575c75a003f089d2739839dec58b964ec3843", result);
        
    }

}
