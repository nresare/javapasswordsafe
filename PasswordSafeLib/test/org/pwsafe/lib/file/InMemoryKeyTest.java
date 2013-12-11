package org.pwsafe.lib.file;

import java.util.Arrays;

import junit.framework.TestCase;

import org.pwsafe.lib.crypto.InMemoryKey;

public class InMemoryKeyTest extends TestCase {

	public void testCreateKey() {
		final InMemoryKey key = new InMemoryKey(8);
		key.init();
		key.dispose();
	}

	public void testGetKey() {
		final InMemoryKey key = new InMemoryKey(8);
		key.init();
		final byte[] content = key.getKey();
		assertEquals(8, content.length);
		assertTrue(Arrays.equals(content, key.getKey()));
		final InMemoryKey key2 = new InMemoryKey(8);
		key2.init();
		assertFalse(Arrays.equals(content, key2.getKey()));
		key.dispose();
	}

	public void testDispose() {
		final InMemoryKey key = new InMemoryKey(8);
		key.init();
		key.dispose();
		try {
			key.getKey();
			fail("a disposed key can't be used");
		} catch (final IllegalStateException anEx) {
			// expected
		}
	}

	public void testRotate() {
		byte b = 123;
		final byte distance = 3;
		b = (byte) ((b >>> distance) | (b << -distance));
		System.out.println();
	}

}
