package org.pwsafe.lib.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.lib.datastore.PwsEntryStoreImpl;

public class TestSparseRecords extends TestCase {

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

	
	public void testCreateDataStore () throws Exception {
		PwsEntryStoreImpl theStore = new PwsEntryStoreImpl (pwsFile);
		List<PwsEntryBean> theEntries = theStore.getSparseEntries();
		assertNotNull(theEntries);
		assertTrue(theEntries.isEmpty());
		
		TestUtils.addDummyRecords(pwsFile, 10);
		
		theStore = new PwsEntryStoreImpl (pwsFile);
		theEntries = theStore.getSparseEntries();
		assertNotNull(theEntries);
		assertFalse(theEntries.isEmpty());
		
		for (PwsEntryBean theEntry : theEntries) {
			assertTrue(theEntry.isSparse());
			assertNotNull(theEntry.getTitle());
			assertNotNull(theEntry.getGroup());
			assertNull(theEntry.getPassword());
			assertNull(theEntry.getAutotype());
			assertNotNull(theEntry.getTitle());
		}
		
	}

	public void testSaveEntries () throws Exception {
		PwsEntryStoreImpl theStore = new PwsEntryStoreImpl (pwsFile);
		int initialsize = theStore.getSparseEntries().size();
		assertEquals(initialsize, pwsFile.getRecordCount());
		PwsEntryBean theEntry = new PwsEntryBean ();
		try {
			theStore.addEntry(theEntry);
			fail ("sparse entries should not be addable");
		} catch (IllegalArgumentException e) {
			//Expected
		}
		assertEquals(initialsize, pwsFile.getRecordCount());
		assertEquals(theStore.getSparseEntries().size(), pwsFile.getRecordCount());
		
		theEntry.setSparse(false);
		theEntry.setTitle("testTitle");
		theEntry.setGroup("groupX");
		theEntry.setUsername("userX");
		assertTrue(theStore.addEntry(theEntry));
		assertEquals(initialsize + 1, pwsFile.getRecordCount());
		assertEquals(theStore.getSparseEntries().size(), pwsFile.getRecordCount());
		
		List<PwsEntryBean> theEntries = theStore.getSparseEntries();
		int storeIndex = theEntries.size() - 1;
		PwsEntryBean filledEntryBean = theStore.getEntry(theEntries.size() -1);
		assertEquals(theEntries.get(theEntries.size() -1 ).getStoreIndex(), filledEntryBean.getStoreIndex());
		
		filledEntryBean.setUrl("http://www.some.net");
		theStore.updateEntry(filledEntryBean);
		assertEquals(initialsize + 1, pwsFile.getRecordCount());
		assertEquals(theStore.getSparseEntries().size(), pwsFile.getRecordCount());
		PwsEntryBean newFilledEntry = theStore.getEntry(storeIndex);
		
		assertEquals(filledEntryBean.getUrl(), newFilledEntry.getUrl());

	}


}
