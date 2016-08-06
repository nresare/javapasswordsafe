/*
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants;

/**
 * Implements the sorting logic for the table. Most of this was lifted straight
 * from "Definitive Guide to SWT & JFace" - a very cool SWT book.
 * 
 * @author Glen Smith
 * 
 */
public class PasswordTableSorter extends ViewerSorter {

	public int ASCENDING = 0;
	public int DESCENDING = 1;

	private int column;
	private int direction;

	public void sortOnColumn(final int columnNumber) {
		if (columnNumber == column) {
			direction = 1 - direction;
		} else {
			this.column = columnNumber;
			direction = ASCENDING;
		}

	}

	@Override
	public int compare(final Viewer arg0, final Object a, final Object b) {

		final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
		final boolean showNotes = thePrefs.getBoolean(JpwPreferenceConstants.SHOW_NOTES_IN_LIST);

		int rc = 0;

		final PwsEntryBean entry1 = (PwsEntryBean) a;
		final PwsEntryBean entry2 = (PwsEntryBean) b;

		switch (column) {

		case 1:
			rc = getComparator().compare(entry1.getTitle(), entry2.getTitle());
			break;
		case 2:
			rc = getComparator().compare(entry1.getUsername(), entry2.getUsername());
			break;

		case 3:
			if (showNotes) {
				rc = getComparator().compare(entry1.getNotes(), entry2.getNotes());
			} else {
				rc = getComparator().compare(safeFormatDate(entry1.getLastChange()), safeFormatDate(entry2.getLastChange()));
			}
			break;
		case 4:
			rc = getComparator().compare(safeFormatDate(entry1.getLastChange()), safeFormatDate(entry2.getLastChange()));
			break;
		}

		if (direction == DESCENDING)
			rc = -rc;

		return rc;
	}

	private String safeFormatDate(final Date aDate) {
		final SimpleDateFormat dateformat = new SimpleDateFormat("YYYY-MM-dd");
		return aDate == null ? "" : dateformat.format(aDate);
	}
}
