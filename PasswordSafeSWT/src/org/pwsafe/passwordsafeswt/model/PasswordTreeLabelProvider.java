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
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.pwsafe.lib.datastore.PwsEntryBean;

/**
 * Label provider for tree viewer. Also implements
 * {@link org.eclipse.jface.viewers.ITableLabelProvider} to allow for tree
 * columns.
 * 
 * @author Glen Smith
 */
public class PasswordTreeLabelProvider extends AbstractTableLabelProvider implements ILabelProvider {

	private static final Log log = LogFactory.getLog(PasswordTreeLabelProvider.class);

	/**
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(final Object node) {
		return null;
	}

	/*
	 * (non-Javadoc) TODO: Merge this with the getColumnText method from {@link
	 * PasswordTableLabelProvider}.
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang
	 * .Object, int)
	 */
	public String getColumnText(final Object element, final int columnIndex) {
		String result = "";

		if (columnIndex == 0) {
			result = "<unknown node type>";
			if (element instanceof String) {
				result = element.toString();
			} else if (element instanceof PwsEntryBean) {
				final PwsEntryBean theEntry = (PwsEntryBean) element;
				result = theEntry.getTitle();
			} else if (element instanceof PasswordTreeContentProvider.TreeGroup) {
				result = element.toString();
			}
			return result;
		}

		if (element instanceof PwsEntryBean) {
			final PwsEntryBean theEntry = (PwsEntryBean) element;

			switch (columnIndex) {
			case 0:
				result = theEntry.getTitle();
				break;
			case 1:
				result = theEntry.getUsername();
				break;
			case 2:
				result = theEntry.getNotes();
				if (result != null) {
					result = result.replace('\t', ' ').replace('\r', ' ');// .replace('\n',' ')
				}
				break;
			case 3:
				result = theEntry.getPassword() != null ? theEntry.getPassword().toString() : null;
				break;
			}
		}
		return result;
	}

	/**
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(final Object node) {
		String result = "<unknown node type>";
		if (node instanceof String) {
			result = node.toString();
		} else if (node instanceof PwsEntryBean) {
			final PwsEntryBean theEntry = (PwsEntryBean) node;
			result = theEntry.getTitle();
		} else if (node instanceof PasswordTreeContentProvider.TreeGroup) {
			result = node.toString();
		}
		return result;
	}

}
