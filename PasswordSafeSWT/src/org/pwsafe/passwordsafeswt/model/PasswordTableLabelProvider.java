/*
 * Copyright (c) 2008-2010 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pwsafe.lib.datastore.PwsEntryBean;

/**
 * Label Provider for the password Table.
 * 
 * @author Glen Smith
 */
public class PasswordTableLabelProvider extends AbstractTableLabelProvider {

	private static final Log log = LogFactory.getLog(PasswordTableLabelProvider.class);

	/**
	 * TODO: Merge this with the getColumnText method from
	 * {@link PasswordTreeTableLabelProvider}.
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
	 *      int)
	 */
	public String getColumnText(final Object element, final int columnIndex) {
		String columnString = null;

		if (element instanceof PwsEntryBean) {
			final PwsEntryBean entry = (PwsEntryBean) element;
			switch (columnIndex) {
			case 0:
				columnString = entry.getTitle();
				break;
			case 1:
				columnString = entry.getUsername();
				break;
			case 2:
				columnString = entry.getNotes();
				break;
			case 3:
				columnString = entry.getPassword() != null ? entry.getPassword().toString() : null;
			}
		} else {
			log.error("Unknown object of type " + element.getClass() + ": " + element);

		}
		if (log.isDebugEnabled())
			log.debug("Setting column index " + columnIndex + " to [" + columnString + "]");
		return columnString; // unknown column
	}

}
