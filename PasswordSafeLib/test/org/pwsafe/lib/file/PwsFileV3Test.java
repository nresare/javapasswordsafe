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
import java.util.ConcurrentModificationException;
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
	
	private PwsFileV3 pwsFile;
	
	@Override
	public void setUp() {
		filename = System.getProperty("user.dir") + File.separator + "sample3.psafe3";
		password = "Pa$$word";
		
		try {
			pwsFile = TestUtils.createPwsFileV3(filename, password);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void tearDown() {
		deletePwsFile(filename);
	}
	
	private static void deletePwsFile(String filename) {
		File file = new File(filename);
		file.delete();
	}
	
	public void testPassphrase() throws EndOfFileException, IOException, UnsupportedFileVersionException, NoSuchAlgorithmException {
		
		PwsFileStorage storage = new PwsFileStorage(filename);
		PwsFileV3 pwsFile = new PwsFileV3(storage, password);
		pwsFile.readAll();
		assertEquals(0, pwsFile.getRecordCount());
		System.out.println(pwsFile.getRecordCount());
		pwsFile.close();
	}

	public void testReadOnly() throws Exception {
		
		pwsFile.setReadOnly(true);
		try {
			pwsFile.save();
			fail("save on Read only file without exception");
		} catch (IOException anEx) {
			//ok
		}
	}

	public void testConcurrentMod() throws Exception {
		File file = new File(filename);
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

	public void testLargeFile() throws EndOfFileException, IOException, UnsupportedFileVersionException, Exception {
		PwsFileV3 file = (PwsFileV3) PwsFileFactory.newFile();
		file.setPassphrase(password);
		int amount = 1000;
		TestUtils.addDummyRecords(file, amount);
		
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
