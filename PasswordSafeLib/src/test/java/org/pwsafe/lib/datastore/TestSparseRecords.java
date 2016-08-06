/*
 * $Id$
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.datastore;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.pwsafe.lib.file.PwsFileV3;
import org.pwsafe.lib.file.TestUtils;

public class TestSparseRecords extends TestCase {

	private String filename;
	private StringBuilder password;

	private PwsFileV3 pwsFile;
	private PwsEntryStoreImpl entryStore;

	@Override
	public void setUp() {
		filename = System.getProperty("user.dir") + File.separator + "sample3.psafe3";
		password = new StringBuilder("Pa$$word");

		try {
			pwsFile = TestUtils.createPwsFileV3(filename, password);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		entryStore = new PwsEntryStoreImpl(pwsFile);
	}

	@Override
	public void tearDown() {
		deletePwsFile(filename);
	}

	private static void deletePwsFile(String filename) {
		final File file = new File(filename);
		file.delete();
	}

	public void testCreateDataStore() throws Exception {
		List<PwsEntryBean> theEntries = entryStore.getSparseEntries();
		assertNotNull(theEntries);
		assertTrue(theEntries.isEmpty());

		TestUtils.addDummyRecords(pwsFile, 10);

		entryStore = new PwsEntryStoreImpl(pwsFile);
		theEntries = entryStore.getSparseEntries();
		assertNotNull(theEntries);
		assertFalse(theEntries.isEmpty());

		for (final PwsEntryBean theEntry : theEntries) {
			assertTrue(theEntry.isSparse());
			assertNotNull(theEntry.getTitle());
			assertNotNull(theEntry.getGroup());
			assertNull(theEntry.getPassword());
			assertNull(theEntry.getAutotype());
			assertNotNull(theEntry.getTitle());
		}

	}

	public void testSaveEntries() throws Exception {
		final int initialSize = entryStore.getSparseEntries().size();
		assertEquals(initialSize, pwsFile.getRecordCount());
		final PwsEntryBean theEntry = new PwsEntryBean();
		try {
			entryStore.addEntry(theEntry);
			fail("sparse entries should not be addable");
		} catch (final IllegalArgumentException e) {
			// Expected
		}
		assertEquals(initialSize, pwsFile.getRecordCount());
		assertEquals(entryStore.getSparseEntries().size(), pwsFile.getRecordCount());

		theEntry.setSparse(false);
		theEntry.setTitle("testTitle");
		theEntry.setGroup("groupX");
		theEntry.setUsername("userX");
		assertTrue(entryStore.addEntry(theEntry));
		assertEquals(initialSize + 1, pwsFile.getRecordCount());
		assertEquals(entryStore.getSparseEntries().size(), pwsFile.getRecordCount());

		final List<PwsEntryBean> theEntries = entryStore.getSparseEntries();
		final int storeIndex = theEntries.size() - 1;
		final PwsEntryBean filledEntryBean = entryStore.getEntry(theEntries.size() - 1);
		assertEquals(theEntries.get(theEntries.size() - 1).getStoreIndex(),
				filledEntryBean.getStoreIndex());

		filledEntryBean.setUrl("http://www.some.net");
		entryStore.updateEntry(filledEntryBean);
		assertEquals(initialSize + 1, pwsFile.getRecordCount());
		assertEquals(entryStore.getSparseEntries().size(), pwsFile.getRecordCount());
		final PwsEntryBean newFilledEntry = entryStore.getEntry(storeIndex);

		assertEquals(filledEntryBean.getUrl(), newFilledEntry.getUrl());

	}

	// Move this to an own PwsEntryBeanTest class
	public void testObjectMethods() throws Exception {

		final PwsEntryBean theEntry = new PwsEntryBean();
		theEntry.setSparse(false);
		theEntry.setTitle("testTitle");
		theEntry.setGroup("groupX");
		theEntry.setUsername("userX");

		assertFalse(theEntry.equals(null));
		assertFalse(theEntry.equals(new Object()));
		assertFalse(theEntry.equals(new PwsEntryBean()));

		final PwsEntryBean theClone = theEntry.clone();

		assertEquals(theEntry, theClone);
		assertEquals(theEntry.hashCode(), theClone.hashCode());
		assertEquals(theEntry.toString(), theClone.toString());

		theClone.setNotes("someNotes");
		assertEquals(theEntry.hashCode(), theClone.hashCode());
		assertFalse(theEntry.equals(theClone));

		theClone.setTitle("newTitle");
		assertFalse(theEntry.hashCode() == theClone.hashCode());
		assertFalse(theEntry.equals(theClone));

	}

}
