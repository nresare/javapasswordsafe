/*
 * $Id$
 * 
 * Copyright (c) 2008 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.file;

import junit.framework.TestCase;

import org.pwsafe.util.FileConverter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 */
public class PwsFileV1Test extends TestCase {
	private static final String PASSPHRASE = "passphrase";

	/**
	 * Tests that the library can create a version 1 database, read from the created file and
	 * round trip convert to V2.
	 */
	public void testV1File() throws Exception {
		Path tmpFile = Files.createTempFile("javapasswordsafe_v1", ".dat");
		assertTrue(tmpFile.toFile().delete());
		String v1FileName = tmpFile.toString();

		try {
			PwsRecord rec = null;
			final PwsFile v1File = new PwsFileV1();

			v1File.setStorage(new PwsFileStorage(v1FileName));
			v1File.setPassphrase(new StringBuilder(PASSPHRASE));

			rec = v1File.newRecord();

			rec.setField(new PwsStringField(PwsRecordV1.TITLE, "Entry number 1"));
			rec.setField(new PwsStringField(PwsRecordV1.PASSWORD, "Password 1"));

			v1File.add(rec);

			rec = v1File.newRecord();

			rec.setField(new PwsStringField(PwsRecordV1.TITLE, "Entry number 2"));
			rec.setField(new PwsStringField(PwsRecordV1.PASSWORD, "Password 2"));
			rec.setField(new PwsStringField(PwsRecordV1.USERNAME, "Username 2"));
			rec.setField(new PwsStringField(PwsRecordV1.NOTES, "Notes line 1\r\nNotes line 2"));

			v1File.add(rec);

			assertTrue("Modified flag is not TRUE", v1File.isModified());
			assertEquals("Record count is not = 2", 2, v1File.getRecordCount());

			v1File.save();

			assertTrue("Modified flag is not FALSE", !v1File.isModified());

			v1File.dispose();
			assertEquals(null, v1File.getPassphrase());

			PwsFile anotherV1File = new PwsFileV1(new PwsFileStorage(v1FileName), PASSPHRASE);
			anotherV1File.readAll();
			anotherV1File.close();

			assertEquals(2, anotherV1File.getRecordCount());


			PwsFileV1 thirdV1File = new PwsFileV1(new PwsFileStorage(v1FileName), PASSPHRASE);
			thirdV1File.readAll();
			thirdV1File.close();

			final PwsFileV2 newTempFile = (PwsFileV2) FileConverter.convertV1ToV2(thirdV1File);
			assertEquals(2, newTempFile.getRecordCount());

			final PwsFileV1 newFile = (PwsFileV1) FileConverter.convertV2ToV1(newTempFile);
			assertEquals(2, newFile.getRecordCount());

			assertEquals(thirdV1File.getRecord(0), newFile.getRecord(0));

		} finally {
			assertTrue(tmpFile.toFile().delete());
		}
	}

}
