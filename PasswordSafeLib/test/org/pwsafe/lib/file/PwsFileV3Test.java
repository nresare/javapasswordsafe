/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.file;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import junit.framework.TestCase;

import org.pwsafe.lib.exception.EndOfFileException;
import org.pwsafe.lib.exception.UnsupportedFileVersionException;

/**
 * Test cases for sample v3 record.
 * @author Glen Smith
 *
 */
public class PwsFileV3Test extends TestCase {
	
	private String filename;
	private String password;
	
	public void setUp() {
		filename = System.getProperty("user.dir") + File.separator + "sample3.psafe3";
		password = "Pa$$word";
		
		try {
			createPwsFile(filename, password);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void tearDown() {
		deletePwsFile(filename);
	}
	
	private static void createPwsFile(String filename, String password) throws IOException {
		PwsFileV3 pwsFileV3 = new PwsFileV3();
		PwsFileStorage storage = new PwsFileStorage(filename);
		pwsFileV3.setStorage(storage);
		pwsFileV3.setPassphrase(password);
		pwsFileV3.save();
	}
	
	private static void deletePwsFile(String filename) {
		File file = new File(filename);
		file.delete();
	}
	
	public void testPassphrase() throws EndOfFileException, IOException, UnsupportedFileVersionException, NoSuchAlgorithmException {
		createPwsFile(filename, password);
		
		PwsFileStorage storage = new PwsFileStorage(filename);
		PwsFileV3 pwsFile = new PwsFileV3(storage, password);
		pwsFile.readAll();
		System.out.println(pwsFile.getRecordCount());
		pwsFile.close();
	}
	
	public void testLargeFile() throws EndOfFileException, IOException, UnsupportedFileVersionException, Exception {
		PwsFileV3 file = (PwsFileV3) PwsFileFactory.newFile();
		file.setPassphrase(password);
		for (int i = 0; i < 1000; i++) {
			if (i%1 == 0) { System.out.print("."); }
			PwsRecordV3 v3 = (PwsRecordV3) file.newRecord();
			
            v3.setField(new PwsStringUnicodeField(PwsRecordV3.GROUP , "group" + i%10));
            v3.setField(new PwsStringUnicodeField(PwsRecordV3.TITLE , "title" + i));
            v3.setField(new PwsStringUnicodeField(PwsRecordV3.USERNAME , "user"+i));
            v3.setField(new PwsStringUnicodeField(PwsRecordV3.PASSWORD , "pw" + i));
            v3.setField(new PwsStringUnicodeField(PwsRecordV3.NOTES , "notes"+i));
			file.add(v3);
		}
		
		PwsFileStorage storage = new PwsFileStorage(filename);
		file.setStorage(storage);
		System.out.println("\nDone.");
		
		file.save();
		System.out.println("Wrote records: " + file.getRecordCount());
		file.close();
		
		PwsFileStorage storage2 = new PwsFileStorage(filename);
		PwsFileV3 file2 = new PwsFileV3(storage2, password);
		file2.readAll();
		System.out.println("Read records: " + file2.getRecordCount());
	}
	
	/**
	 * Checks if a record with a new password policy field (#16) can be loaded.
	 * @throws Exception
	 */
	public void testNewPasswordPolicyField () throws Exception {
		PwsFileV3 theFile = (PwsFileV3) PwsFileFactory.loadFile("new_policy_bug.psafe3", "test");
		assertEquals(1,theFile.getRecordCount());
		Iterator i = theFile.getRecords();
		assertTrue(i.hasNext());
		PwsRecordV3 theRow = (PwsRecordV3) i.next();
		PwsField theField = theRow.getField(16);
		assertNotNull(theField);
		
	}
}
