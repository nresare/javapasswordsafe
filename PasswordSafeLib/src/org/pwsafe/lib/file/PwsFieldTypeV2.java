package org.pwsafe.lib.file;

public enum PwsFieldTypeV2 implements PwsFieldType {

	V2_ID_STRING (0),
	UUID (1),
	GROUP (2),
	TITLE (3),
	USERNAME (4),
	NOTES (5),
	PASSWORD (6),
	CREATION_TIME (7),
	PASSWORD_MOD_TIME (8),
	LAST_ACCESS_TIME (9),
	PASSWORD_LIFETIME	(10),
	PASSWORD_POLICY		(11),
	END_OF_RECORD		(255);

	private int id;
	private String name;

	private PwsFieldTypeV2(int anId) {
		id = anId;
		name = toString();
	}

	private PwsFieldTypeV2(int anId, String aName) {
		id = anId;
		name = aName;
	}

	public int getId() {
		return id;
	}

	public String getName() {

		return name;
	}
}
