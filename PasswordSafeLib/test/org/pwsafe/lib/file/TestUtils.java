/*
 * $Id$
 * 
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.file;

import java.io.IOException;
import java.util.Date;

import org.pwsafe.lib.exception.PasswordSafeException;

/**
 * Static helper methods for Tests.
 * @author roxon
 *
 */
public class TestUtils {

	public static void addDummyRecords(PwsFileV3 file, int amount) throws PasswordSafeException {
		for (int i = 0; i < amount; i++) {
			if (i % 5 == 0) {
				System.out.print(".");
			}
			PwsRecordV3 record = (PwsRecordV3) file.newRecord();
			
			record.setField(new PwsStringUnicodeField(PwsRecordV3.GROUP, "group" + i % 10));
			record.setField(new PwsStringUnicodeField(PwsRecordV3.GROUP, "group" + i % 10));
			record.setField(new PwsStringUnicodeField(PwsRecordV3.TITLE, "title" + i));
			record.setField(new PwsStringUnicodeField(PwsRecordV3.USERNAME, "user" + i));
			record.setField(new PwsStringUnicodeField(PwsRecordV3.PASSWORD, "pw" + i));
			record.setField(new PwsStringUnicodeField(PwsRecordV3.NOTES, "notes" + i));
			Date now = new Date();
			record.setField(new PwsTimeField(PwsRecordV3.LAST_ACCESS_TIME, now));
			record.setField(new PwsTimeField(PwsRecordV3.LAST_MOD_TIME, now));
			record.setField(new PwsStringUnicodeField(PwsRecordV3.URL, "http://www.nowhere.all/file" + i));
			file.add(record);
		}
	}

	public static void addDummyRecords(PwsFileV2 file, int amount) throws PasswordSafeException {
		for (int i = 0; i < amount; i++) {
			if (i % 5 == 0) {
				System.out.print(".");
			}
			PwsRecordV2 record = (PwsRecordV2) file.newRecord();

			record.setField(new PwsStringField(PwsRecordV2.GROUP, "group" + i % 10));
			record.setField(new PwsStringField(PwsRecordV2.TITLE, "title" + i));
			record.setField(new PwsStringField(PwsRecordV2.USERNAME, "user" + i));
			record.setField(new PwsStringField(PwsRecordV2.PASSWORD, "pw" + i));
			record.setField(new PwsStringField(PwsRecordV2.NOTES, "notes" + i));
			file.add(record);
		}
	}
	
	public static PwsFileV3 createPwsFileV3(String filename, StringBuilder password) throws IOException {
		PwsFileV3 pwsFileV3 = new PwsFileV3();
		PwsFileStorage storage = new PwsFileStorage(filename);
		pwsFileV3.setStorage(storage);
		pwsFileV3.setPassphrase(password);
		pwsFileV3.save();
		pwsFileV3.close();
		
		return pwsFileV3;
	}

}
