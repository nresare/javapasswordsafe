/*
 * $Id$
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.file;

import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pwsafe.lib.datastore.PwsEntryStore;
import org.pwsafe.lib.exception.InvalidPassphraseException;

public class PwsFileFactoryTest extends TestCase {

	private final static Log LOGGER = LogFactory.getLog(PwsFileFactoryTest.class);
	private final static String testV2Filename = "password_file_2.dat";

	private final static String PASSPHRASE = "THEFISH";

	// /**
	// * Make sure a new safe is available.
	// * @throws PasswordSafeException
	// */
	// public void testFile() throws PasswordSafeException
	// {
	// PwsFileV2 file;
	//
	// file = new PwsFileV2();
	// TestUtils.addDummyRecords(file, 5);
	//
	// }

	public void testLoadFile() throws Exception {
		PwsFile theFile = PwsFileFactory.loadFile(testV2Filename, new StringBuilder(PASSPHRASE));

		assertNotNull(theFile);
		assertTrue(theFile instanceof PwsFileV2);

		assertEquals(1, theFile.getRecordCount());

		final PwsEntryStore theStore = PwsFileFactory.getStore(theFile);
		assertNotNull(theStore);
		assertEquals(1, theStore.getSparseEntries().size());

		try {
			theFile = PwsFileFactory
					.loadFile(testV2Filename, new StringBuilder("wrong passphrase"));
			fail("Wrong passphrase should lead to an InvalidPassphraseException");
		} catch (final InvalidPassphraseException e) {
			// ok
			LOGGER.info(e.toString());
		}

	}

	public void testReadOnly() throws Exception {
		final PwsFile pwsFile = PwsFileFactory.loadFile(testV2Filename, new StringBuilder(
				PASSPHRASE));
		pwsFile.setReadOnly(true);
		try {
			pwsFile.save();
			fail("save on Read only file without exception");
		} catch (final IOException anEx) {
			// ok
		}
	}

	public void testConcurrentMod() throws Exception {
		final PwsFile pwsFile = PwsFileFactory.loadFile(testV2Filename, new StringBuilder(
				PASSPHRASE));

		final File file = new File(testV2Filename);
		file.setLastModified(System.currentTimeMillis() + 1000);
		pwsFile.setModified();
		try {
			pwsFile.save();
			fail("save concurrently modified file without exception");
		} catch (final ConcurrentModificationException e) {
			// ok
		}
		// and after save:
		file.setLastModified(System.currentTimeMillis() + 2000);
		pwsFile.setModified();
		try {
			pwsFile.save();
			fail("save concurrently modified file without exception");
		} catch (final ConcurrentModificationException e) {
			// ok
		}
	}

	public void testNewFile() {
		final PwsFile theFile = PwsFileFactory.newFile();

		assertNotNull(theFile);
		assertEquals(PwsFileV3.class, theFile.getClass());
	}

}
