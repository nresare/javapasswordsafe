package org.pwsafe.lib.file;

public enum PwsFieldTypeV1 implements PwsFieldType {

	DEFAULT	(0),
	TITLE	(3),
	USERNAME (4),
	NOTES	(5),
	PASSWORD (6);

	private int id;
	private String name;

	private PwsFieldTypeV1(int anId) {
		id = anId;
		name = toString();
	}

	private PwsFieldTypeV1(int anId, String aName) {
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
