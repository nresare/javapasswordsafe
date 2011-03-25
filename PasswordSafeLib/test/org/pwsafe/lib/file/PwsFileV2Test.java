/*
 * $Id$
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.file;

import java.util.Iterator;

import junit.framework.TestCase;

import org.pwsafe.lib.exception.PasswordSafeException;

public class PwsFileV2Test extends TestCase {

	private final static String testV2Filename = "password_file_2.dat";
	private final static String groupName = "bank.online";
	
	public void testFile() throws PasswordSafeException
	{
		PwsFileV2	file;
		PwsRecordV2	rec;

		final int i = 5;
		file	= new PwsFileV2();
		TestUtils.addDummyRecords(file, i);
		

		assertEquals(i, file.getRecordCount());
		for (Iterator<PwsRecordV2> theFiles = (Iterator<PwsRecordV2>) file.getRecords(); theFiles.hasNext(); ) {
			rec = theFiles.next();
			assertNotNull(rec.getField(PwsRecordV2.USERNAME));
			assertNotNull(rec.getField(PwsFieldTypeV2.USERNAME));
			assertEquals(rec.getField(PwsRecordV2.USERNAME), rec.getField(PwsFieldTypeV2.USERNAME));
			assertNotNull(rec.getField(PwsRecordV2.PASSWORD));
			assertNotNull(rec.getField(PwsRecordV2.TITLE));
			assertNotNull(rec.getField(PwsRecordV2.GROUP));
		}

	}

	private void createdummyRecord(PwsFileV2 file, int i) throws PasswordSafeException {
		PwsRecordV2 rec;
		rec = (PwsRecordV2) file.newRecord();

		rec.setField(new PwsStringField(PwsRecordV2.USERNAME, "User " + i));
		rec.setField(new PwsStringField(PwsRecordV2.PASSWORD, "Pass " + i));
		rec.setField(new PwsStringField(PwsRecordV2.TITLE, "Online Bank " + i));
		rec.setField(new PwsStringField(PwsRecordV2.GROUP, groupName));

		file.add(rec);
	}

	public void testFileStorage() throws Exception {
		PwsFileStorage pfs = new PwsFileStorage(testV2Filename);
		byte[] data = pfs.load();
		assertNotNull(data);
		assertTrue(data.length > 0);
	}

}
