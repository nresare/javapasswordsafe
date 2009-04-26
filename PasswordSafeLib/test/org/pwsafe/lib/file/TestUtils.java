package org.pwsafe.lib.file;

import java.io.IOException;

import org.pwsafe.lib.exception.PasswordSafeException;

/**
 * Static helper methods for Tests.
 * @author rooxn
 *
 */
public class TestUtils {

	static void addDummyRecords(PwsFileV3 file, int amount)
			throws PasswordSafeException {
		for (int i = 0; i < amount; i++) {
			if (i % 1 == 0) {
				System.out.print(".");
			}
			PwsRecordV3 v3 = (PwsRecordV3) file.newRecord();

			v3.setField(new PwsStringUnicodeField(PwsRecordV3.GROUP, "group"			+ i % 10));
			v3.setField(new PwsStringUnicodeField(PwsRecordV3.TITLE, "title"
					+ i));
			v3.setField(new PwsStringUnicodeField(PwsRecordV3.USERNAME, "user"
					+ i));
			v3.setField(new PwsStringUnicodeField(PwsRecordV3.PASSWORD, "pw"
					+ i));
			v3.setField(new PwsStringUnicodeField(PwsRecordV3.NOTES, "notes"
					+ i));
			file.add(v3);
		}
	}

	static PwsFileV3 createPwsFileV3(String filename, String password) throws IOException {
		PwsFileV3 pwsFileV3 = new PwsFileV3();
		PwsFileStorage storage = new PwsFileStorage(filename);
		pwsFileV3.setStorage(storage);
		pwsFileV3.setPassphrase(password);
		pwsFileV3.save();
		
		return pwsFileV3;
	}

}
