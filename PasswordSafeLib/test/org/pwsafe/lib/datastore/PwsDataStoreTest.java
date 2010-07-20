/*
 * $Id: TestSparseRecords.java 404 2009-09-21 19:19:25Z roxon $
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.datastore;

import java.io.File;
import java.util.Date;

import junit.framework.TestCase;

import org.pwsafe.lib.file.PwsFieldTypeV3;
import org.pwsafe.lib.file.PwsFileFactory;
import org.pwsafe.lib.file.PwsFileV1;
import org.pwsafe.lib.file.PwsFileV2;
import org.pwsafe.lib.file.PwsFileV3;
import org.pwsafe.lib.file.PwsRecordV1;
import org.pwsafe.lib.file.PwsRecordV2;
import org.pwsafe.lib.file.PwsRecordV3;

public class PwsDataStoreTest extends TestCase {

	private String filename;
	private StringBuilder password;
	
	private PwsFileV3 pwsFile;
	private PwsEntryStoreImpl entryStore;
	
	@Override
	public void setUp() {
		filename = System.getProperty("user.dir") + File.separator + "sample3.psafe3";
		password = new StringBuilder("Pa$$word");

		pwsFile = (PwsFileV3) PwsFileFactory.newFile();
		pwsFile.setPassphrase(password);

		entryStore = new PwsEntryStoreImpl(pwsFile);
	}

	@Override
	public void tearDown() {
		deletePwsFile(filename);
	}
	
	private static void deletePwsFile(String filename) {
		File file = new File(filename);
		file.delete();
	}

	
	public void testNewV3Entry () throws Exception {
		PwsRecordV3 aRecord = (PwsRecordV3) pwsFile.newRecord();
		
		PwsEntryBean entry = PwsEntryBean.fromPwsRecord(aRecord);
		assertEquals("", entry.getTitle());
		assertNotNull(entry.getId());
		entry.setTitle("fromTitle");
		entry.setUsername("fromName");

		entry.toPwsRecord(aRecord);
		assertEquals("fromTitle", aRecord.getField(PwsFieldTypeV3.TITLE).toString());
		assertEquals("fromName", aRecord.getField(PwsFieldTypeV3.USERNAME).toString());
		assertNull(aRecord.getField(PwsFieldTypeV3.GROUP));
		assertNull(aRecord.getField(PwsFieldTypeV3.NOTES));
		assertNull(aRecord.getField(PwsFieldTypeV3.URL));
		assertNull(aRecord.getField(PwsFieldTypeV3.AUTOTYPE));

		
		entry = PwsEntryBean.fromPwsRecord(aRecord);
		assertEquals("fromTitle", entry.getTitle());
		assertEquals("fromName", entry.getUsername());
		assertEquals(entry, PwsEntryBean.fromPwsRecord(aRecord));
	
		entry = new PwsEntryBean();
		entry.setTitle("fromTitle");
		entry.setPassword(new StringBuilder("fromPassword"));
		Date changeDate = new Date();
		entry.setLastChange(changeDate);
		aRecord = (PwsRecordV3) pwsFile.newRecord();
		entry.toPwsRecord(aRecord);
		
		assertEquals("fromPassword", aRecord.getField(PwsFieldTypeV3.PASSWORD).toString());
		assertEquals(changeDate, aRecord.getField(PwsFieldTypeV3.LAST_MOD_TIME).getValue());
		assertNull(aRecord.getField(PwsFieldTypeV3.GROUP));
		assertNull(aRecord.getField(PwsFieldTypeV3.USERNAME));
		assertNull(aRecord.getField(PwsFieldTypeV3.NOTES));
		assertNull(aRecord.getField(PwsFieldTypeV3.URL));
		assertNull(aRecord.getField(PwsFieldTypeV3.AUTOTYPE));
	}		
	
	public void testNewV2Entry () throws Exception {
		PwsFileV2	file = new PwsFileV2();

		PwsRecordV2 aRecord = (PwsRecordV2) file.newRecord() ;
		
		PwsEntryBean entry = PwsEntryBean.fromPwsRecord(aRecord);
		entry.setTitle("fromTitle");
		entry.setUsername("fromName");

		entry.toPwsRecord(aRecord);
		assertEquals("fromTitle", aRecord.getField(PwsFieldTypeV3.TITLE).toString());
		assertEquals("fromName", aRecord.getField(PwsFieldTypeV3.USERNAME).toString());
		assertEquals(entry, PwsEntryBean.fromPwsRecord(aRecord));
		
		entry = PwsEntryBean.fromPwsRecord(aRecord);
		assertEquals("fromTitle", entry.getTitle());
		assertEquals("fromName", entry.getUsername());

//		assertEquals(aRecord, actual)
	
		entry = new PwsEntryBean();
		entry.setTitle("fromTitle");
		entry.setPassword(new StringBuilder("fromPassword"));
		aRecord = (PwsRecordV2) file.newRecord();
		entry.toPwsRecord(aRecord);
		
		assertEquals("fromPassword", aRecord.getField(PwsFieldTypeV3.PASSWORD).toString());
	}		

	
	public void testNewV1Entry () throws Exception {
		PwsFileV1	file = new PwsFileV1();

		PwsRecordV1 aRecord = (PwsRecordV1) file.newRecord() ;
		
		PwsEntryBean entry = PwsEntryBean.fromPwsRecord(aRecord);
		entry.setTitle("fromTitle");
		entry.setUsername("fromName");

		entry.toPwsRecord(aRecord);
		assertEquals("fromTitle", aRecord.getField(PwsFieldTypeV3.TITLE).toString());
		assertEquals("fromName", aRecord.getField(PwsFieldTypeV3.USERNAME).toString());

		assertEquals(entry, PwsEntryBean.fromPwsRecord(aRecord));
		entry = PwsEntryBean.fromPwsRecord(aRecord);
		assertEquals("fromTitle", entry.getTitle());
		assertEquals("fromName", entry.getUsername());
	
		entry = new PwsEntryBean();
		entry.setTitle("fromTitle");
		entry.setPassword(new StringBuilder("fromPassword"));
		aRecord = (PwsRecordV1) file.newRecord();
		entry.toPwsRecord(aRecord);
		
		assertEquals("fromPassword", aRecord.getField(PwsFieldTypeV3.PASSWORD).toString());
	}		


}
