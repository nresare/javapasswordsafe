/*
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.listener;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.pwsafe.passwordsafeswt.model.PasswordTableSorter;

/**
 * An adaptor used for handing sorting of a column when the user clicks the
 * column header.
 * 
 * @author Glen Smith
 */
public class TableColumnSelectionAdaptor extends SelectionAdapter {

	private TableViewer tv;
	private int columnNumber;

	public TableColumnSelectionAdaptor(TableViewer tv, int columnNumber) {
		this.tv = tv;
		this.columnNumber = columnNumber;
	}

	@Override
	public void widgetSelected(SelectionEvent se) {
		PasswordTableSorter pts = (PasswordTableSorter) tv.getSorter();
		pts.sortOnColumn(columnNumber);
		tv.refresh();
	}

}
