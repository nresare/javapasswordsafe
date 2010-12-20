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
import org.pwsafe.lib.file.PwsFieldTypeV1;
import org.pwsafe.lib.file.PwsFieldTypeV2;
import org.pwsafe.lib.file.PwsFieldTypeV3;
import org.pwsafe.lib.file.PwsRecordV1;
import org.pwsafe.lib.file.PwsRecordV2;
import org.pwsafe.lib.file.PwsRecordV3;

/**
 * Label Provider for the password Table.
 *
 * @author Glen Smith
 */
public class PasswordTableLabelProvider extends AbstractTableLabelProvider {
	
	private static final Log log = LogFactory.getLog(PasswordTableLabelProvider.class);
	
	/**
	 * TODO: Merge this with the getColumnText method from {@link PasswordTreeTableLabelProvider}.
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		String columnString = null;
		
		if (element instanceof PwsEntryBean) {
			PwsEntryBean entry = (PwsEntryBean) element;
			switch(columnIndex) {
				case 0:
					columnString =  entry.getTitle();
					break;
			    case 1:
			    	columnString =  entry.getUsername();
			    	break;
			    case 2:
			    	columnString = entry.getNotes();
			    	break;
			    case 3:
			    	columnString = entry.getPassword() != null ? entry.getPassword().toString() : null;
			}			
		}
		else if (element instanceof PwsRecordV3) {
			PwsRecordV3 v3 = (PwsRecordV3) element;
			switch(columnIndex) {
				case 0:
					columnString =  PwsEntryBean.getSafeValue(v3,PwsFieldTypeV3.TITLE);
					break;
			    case 1:
			    	columnString =  PwsEntryBean.getSafeValue(v3,PwsFieldTypeV3.USERNAME);
			    	break;
			    case 2:
			    	columnString = PwsEntryBean.getSafeValue(v3,PwsFieldTypeV3.NOTES);
			    	break;
			    case 3:
			    	columnString = PwsEntryBean.getSafeValue(v3,PwsFieldTypeV3.PASSWORD);
			}
		} else if (element instanceof PwsRecordV2) {
			PwsRecordV2 v2 = (PwsRecordV2) element;
			switch(columnIndex) {
				case 0:
					columnString =  PwsEntryBean.getSafeValue(v2,PwsFieldTypeV2.TITLE);
					break;
			    case 1:
			    	columnString =  PwsEntryBean.getSafeValue(v2,PwsFieldTypeV2.USERNAME);
			    	break;
			    case 2:
			    	columnString = PwsEntryBean.getSafeValue(v2,PwsFieldTypeV2.NOTES);
			    	break;
			    case 3:
			    	columnString = PwsEntryBean.getSafeValue(v2,PwsFieldTypeV2.PASSWORD);
			}
		} else {
			PwsRecordV1 v1 = (PwsRecordV1) element;
			switch(columnIndex) {
				case 0:
					columnString = PwsEntryBean.getSafeValue(v1,PwsFieldTypeV1.TITLE);
					break;
			    case 1:
			    	columnString = PwsEntryBean.getSafeValue(v1,PwsFieldTypeV1.USERNAME);
			    	break;
			    case 2:
			    	columnString = PwsEntryBean.getSafeValue(v1,PwsFieldTypeV1.NOTES);
			    	break;
			    case 3:
			    	columnString = PwsEntryBean.getSafeValue(v1,PwsFieldTypeV1.PASSWORD);
			    	break;
			}
			
		}
		if (log.isDebugEnabled())
			log.debug("Setting column index " + columnIndex + " to [" + columnString + "]");
		return columnString;  // unknown column
	}

}
