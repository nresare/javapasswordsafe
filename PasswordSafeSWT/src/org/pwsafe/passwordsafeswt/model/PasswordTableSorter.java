/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.model;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.pwsafe.lib.datastore.PwsEntryBean;

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
	
	public void sortOnColumn(int columnNumber) {
		if (columnNumber == column) {
			direction = 1 - direction;
		} else {
			this.column = columnNumber;
			direction = ASCENDING;
		}
		
	}
	
	@Override
	public int compare(Viewer arg0, Object a, Object b) {
		int rc = 0;

		final PwsEntryBean entry1 = (PwsEntryBean) a;
		final PwsEntryBean entry2 = (PwsEntryBean) b;
		
		switch(column) {
		
		case 1:
			rc = collator.compare(entry1.getTitle(), entry2.getTitle());
			break;
		case 2:
			rc = collator.compare(entry1.getUsername(), entry2.getUsername());
			break;
		
		case 3:
			rc = collator.compare(entry1.getNotes(), entry2.getNotes());
			break;	
		}
		
		if (direction == DESCENDING)
			rc = -rc;
		
		return rc;
	}
	
}
