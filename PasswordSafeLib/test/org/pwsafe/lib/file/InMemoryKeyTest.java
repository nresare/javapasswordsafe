package org.pwsafe.lib.file;

import java.util.Arrays;

import org.pwsafe.lib.crypto.InMemoryKey;

import junit.framework.TestCase;

public class InMemoryKeyTest extends TestCase {

	public void testCreateKey () {
		InMemoryKey key = new InMemoryKey(8);
		key.init();
		key.dispose();
	}

	public void testGetKey () {
		InMemoryKey key = new InMemoryKey(8);
		key.init();
		byte[] content = key.getKey();
		assertEquals(8, content.length);
		assertTrue(Arrays.equals(content, key.getKey()));
		InMemoryKey key2 = new InMemoryKey(8);
		key2.init();
		assertFalse(Arrays.equals(content, key2.getKey()));
		key.dispose();
	}

	public void testDispose () {
		InMemoryKey key = new InMemoryKey(8);
		key.init();
		key.dispose();
		try {
			byte[] keyBytes = key.getKey();
			fail ("a disposed key can't be used");
		} catch (IllegalStateException anEx) {
			// expected
		}
	}
	
	public void testRotate () {
        byte b = 123;
        byte distance = 3;
        b  = (byte) ((b >>> distance) | (b << -distance));
        System.out.println ();
	}
	
}
