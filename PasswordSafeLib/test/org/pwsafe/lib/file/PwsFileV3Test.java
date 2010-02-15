/*
 * $Id$
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

import org.pwsafe.lib.Log;
import org.pwsafe.lib.exception.EndOfFileException;
import org.pwsafe.lib.exception.UnsupportedFileVersionException;

/**
 * Test cases for sample v3 record.
 * @author Glen Smith
 *
 */
public class PwsFileV3Test extends TestCase {
	
	private static final Log log = Log.getInstance(PwsFileV3Test.class.getName());
	
	private String filename;
	private String passphrase;
	
	private PwsFileV3 pwsFile;
	
	@Override
	public void setUp() {
		filename = System.getProperty("user.dir") + File.separator + "sample3.psafe3";
		passphrase = "Pa$$word";
		
		try {
			pwsFile = TestUtils.createPwsFileV3(filename, new StringBuilder(passphrase));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		assertTrue("testsafe creation failed", new File(filename).exists());
	}
	
	@Override
	public void tearDown() {
		deletePwsFile(filename);
	}
	
	private static void deletePwsFile(String filename) {
		File file = new File(filename);
		if (file.exists()) {
			assertTrue("Couldn't delete testfile", file.delete());
		}
	}
	
	public void testPassphrase() throws EndOfFileException, IOException, UnsupportedFileVersionException, NoSuchAlgorithmException {
		String myPassphrase = "new Passphrase";
		
		assertEquals(0, pwsFile.getRecordCount());
		log.info("New file records: " + pwsFile.getRecordCount());
		
		StringBuilder savedPassphrase = new StringBuilder(myPassphrase);
		pwsFile.setPassphrase(savedPassphrase);
		pwsFile.save();
		pwsFile.close();
		
		PwsFileStorage storage2 = new PwsFileStorage(filename);
		PwsFileV3 pwsFile2 = new PwsFileV3(storage2, myPassphrase);
		assertEquals(myPassphrase, pwsFile2.getPassphrase());
		pwsFile2.close();
		
		// should fail with old passphrase:
		storage2 = new PwsFileStorage(filename);
		try {
			pwsFile2 = new PwsFileV3(storage2, passphrase);
			fail ("passphrase change failed !?");
		} catch (IOException anEx) {
			//ok
		}
		
		// dispose has to clean everything:
		pwsFile.dispose();
		assertEquals(null,pwsFile.getPassphrase());
		//the original Stringbuilder is also deleted:
		assertEquals("", savedPassphrase.toString());
	}

	public void testReadOnly() throws Exception {
		
		pwsFile.setReadOnly(true);
		try {
			pwsFile.save();
			fail("save on Read-only file without exception");
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

	public void testUnknownField () throws Exception {
		int unknownFieldType = 213;
		String payload = "unknown";
		
		TestUtils.addDummyRecords(pwsFile, 2);
		
		PwsRecordV3 rec = (PwsRecordV3) pwsFile.getRecord(0);
		PwsUnknownField field = new PwsUnknownField(unknownFieldType, payload.getBytes());
		assertEquals(payload, new String(field.getBytes()));
		assertEquals(payload, new String((byte[])field.getValue()));

		rec.setField(field);
		pwsFile.set(0, rec);
		pwsFile.setModified();
		pwsFile.save();
		pwsFile.close();
		pwsFile.dispose();
		
		PwsFileStorage storage2 = new PwsFileStorage(filename);
		PwsFileV3 pwsFile2 = new PwsFileV3(storage2, passphrase);
		pwsFile2.readAll();
		pwsFile2.close();
				
		PwsRecordV3 savedRec = (PwsRecordV3) pwsFile2.getRecord(0);

		PwsField noField = savedRec.getField(unknownFieldType - 1);
		assertNull (noField);

		PwsField savedField = savedRec.getField(unknownFieldType);
		assertNotNull(savedField);
		assertTrue(savedField instanceof PwsUnknownField);
		assertEquals(payload, new String(savedField.getBytes()));
	}
	
	
	public void testLargeFile() throws Exception {
		PwsFileV3 file = (PwsFileV3) PwsFileFactory.newFile();
		file.setPassphrase(new StringBuilder(passphrase));
		int amount = 1000;
		TestUtils.addDummyRecords(file, amount);
		
		PwsFileStorage storage = new PwsFileStorage(filename);
		file.setStorage(storage);
		System.out.println("\nDone.");
		
		file.save();
		System.out.println("Wrote records: " + file.getRecordCount());
		file.close();
		
		PwsFileStorage storage2 = new PwsFileStorage(filename);
		PwsFileV3 file2 = new PwsFileV3(storage2, passphrase.toString());
		file2.readAll();
		System.out.println("Read records: " + file2.getRecordCount());
	}

	
	/**
	 * Checks if a record with a new passphrase policy field (#16) can be loaded.
	 * @throws Exception
	 */
	public void testNewPasswordPolicyField () throws Exception {
		PwsFileV3 theFile = (PwsFileV3) PwsFileFactory.loadFile("new_policy_bug.psafe3", new StringBuilder("test"));
		assertEquals(1,theFile.getRecordCount());
		Iterator i = theFile.getRecords();
		assertTrue(i.hasNext());
		PwsRecordV3 theRow = (PwsRecordV3) i.next();
		PwsField theField = theRow.getField(16);
		assertNotNull(theField);
		
	}
}
