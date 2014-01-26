/*
 * $Id: TestSparseRecords.java 404 2009-09-21 19:19:25Z roxon $
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.datastore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.junit.Ignore;
import org.pwsafe.lib.file.PwsFileFactory;
import org.pwsafe.lib.file.PwsFileStorage;
import org.pwsafe.lib.file.PwsFileV3;

public class CreateBigDataStoreTest extends TestCase {

	SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss.SSS");

	Log log = LogFactory.getLog(CreateBigDataStoreTest.class);

	private String filename;
	private StringBuilder password;

	private PwsFileV3 pwsFile;
	private PwsEntryStoreImpl entryStore;

	@Override
	public void setUp() throws Exception {
		filename = System.getProperty("user.dir") + File.separator + "bigsample50000.psafe3";
		password = new StringBuilder("test");

		pwsFile = (PwsFileV3) PwsFileFactory.newFile();
		pwsFile.setPassphrase(password);

		final PwsFileStorage storage = new PwsFileStorage(filename);

		pwsFile.setStorage(storage);

		entryStore = new PwsEntryStoreImpl(pwsFile);
	}

	@Override
	public void tearDown() {
		//		deletePwsFile(filename);
	}

	private static void deletePwsFile(final String filename) {
		final File file = new File(filename);
		file.delete();
	}

	//	@Ignore
	public void notestCreateManyEntries() throws Exception {

		final int groups = 10;
		final int subgroups = 100;
		final int entries = 50;

		String groupName;
		String subgroupName;
		String entryTitle;

		log.info(dateFormatter.format(new Date()) + " Start Create Big Sample");

		for (int group = 0; group<groups; group++) {
			groupName = "root " + group;
			for (int subgroup = 0; subgroup < subgroups; subgroup++) {
				subgroupName = groupName + ".sub " + subgroup;
				for (int entry = 0; entry < entries; entry++) {
					final int entryNumber = group + subgroup + entry;
					entryTitle = "title " + group + subgroup + entry;
					final PwsEntryBean entryBean = createEntry (subgroupName, entryTitle, entryNumber);
					entryStore.addEntry(entryBean);
				}
			}
		}
		log.info(dateFormatter.format(new Date()) + " Big Sample filled");
		pwsFile.save();
		log.info(dateFormatter.format(new Date()) + " Big Sample saved");

	}

	private PwsEntryBean createEntry(final String groupName, final String entryTitle, final int entryNumber) {

		final PwsEntryBean entry = new PwsEntryBean();
		entry.setSparse(false);
		entry.setGroup(groupName);
		entry.setTitle(entryTitle);
		entry.setUsername("user_" + entryNumber);
		entry.setPassword(new StringBuilder("fromPassword"));
		entry.setExpires(new Date());
		entry.setNotes("Lorem ipsum and so on for just a few words of text on " + entryTitle);
		entry.setUrl("https://nowhere.local/aSecret/service/id=" + entryNumber);

		return entry;
	}

}
