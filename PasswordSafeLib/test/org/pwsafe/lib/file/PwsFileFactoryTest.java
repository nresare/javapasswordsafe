/*
 * $Id:$
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.file;

import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import junit.framework.TestCase;

import org.pwsafe.lib.exception.PasswordSafeException;


public class PwsFileFactoryTest extends TestCase {

	private final static String testV2Filename = "password_file_2.dat";
	private final static String groupName = "bank.online";
	
	
	public void testFile()
	throws PasswordSafeException
	{
		PwsFileV2	file;
		PwsRecordV2	rec;

		int i = 0;
		file	= new PwsFileV2();
		createdummyRecord(file, ++i);
		createdummyRecord(file, ++i);
		createdummyRecord(file, ++i);
		createdummyRecord(file, ++i);
		createdummyRecord(file, ++i);
		

		assertEquals(i, file.getRecordCount());
		for (Iterator theFiles = file.getRecords(); theFiles.hasNext(); ) {
			rec = (PwsRecordV2) theFiles.next();
			assertNotNull(rec.getField(PwsRecordV2.USERNAME));
			assertNotNull(rec.getField(PwsRecordV2.PASSWORD));
			assertNotNull(rec.getField(PwsRecordV2.TITLE));
			assertNotNull(rec.getField(PwsRecordV2.GROUP));
			assertNotNull(rec.getField(PwsRecordV2.GROUP));
			assertEquals(groupName, rec.getField(PwsRecordV2.GROUP).toString());
		}

	}

	private void createdummyRecord(PwsFileV2 file, int i)
			throws PasswordSafeException {
		PwsRecordV2 rec;
		rec		= (PwsRecordV2) file.newRecord();
		
		rec.setField( new PwsStringField( PwsRecordV2.USERNAME, "User " + i) );
		rec.setField( new PwsStringField( PwsRecordV2.PASSWORD, "Pass " + i) );
		rec.setField( new PwsStringField( PwsRecordV2.TITLE, "Online Bank " + i) );
		rec.setField( new PwsStringField( PwsRecordV2.GROUP, groupName) );
		
		file.add( rec );
	}
	
	public void testFileStorage() throws Exception {
		PwsFileStorage pfs = new PwsFileStorage(testV2Filename);
		byte[] data = pfs.load();
		assertNotNull(data);
		assertTrue(data.length > 0);
	}

	public void testLoadFile () throws Exception {
		PwsFile theFile = PwsFileFactory.loadFile(testV2Filename, "THEFISH");
		
		assertNotNull(theFile);
		assertTrue(theFile instanceof PwsFileV2);
		
		assertEquals (1, theFile.getRecordCount());
	}

	public void testReadOnly() throws Exception {
		PwsFile pwsFile = PwsFileFactory.loadFile(testV2Filename, "THEFISH");
		pwsFile.setReadOnly(true);
		try {
			pwsFile.save();
			fail("save on Read only file without exception");
		} catch (IOException anEx) {
			//ok
		}
	}

	public void testConcurrentMod() throws Exception {
		PwsFile pwsFile = PwsFileFactory.loadFile(testV2Filename, "THEFISH");

		File file = new File(testV2Filename);
		file.setLastModified(System.currentTimeMillis() + 1000);
		pwsFile.setModified();
		try {
			pwsFile.save();			
			fail("save concurrently modified file without exception");
		} catch (ConcurrentModificationException e) {
			//ok
		}
		// and after save:
		file.setLastModified(System.currentTimeMillis() + 2000);
		pwsFile.setModified();
		try {
			pwsFile.save();			
			fail("save concurrently modified file without exception");
		} catch (ConcurrentModificationException e) {
			//ok
		}

	}

}
