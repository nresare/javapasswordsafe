/*
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.lib.datastore.PwsEntryStore;

/**
 * Content Provider for the Table.
 * 
 * @author Glen Smith
 */
public class PasswordTableContentProvider implements IStructuredContentProvider {

	private static final Log log = LogFactory.getLog(PasswordTableContentProvider.class);

	PwsEntryStore dataStore;

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(final Viewer vwr, final Object oldInput, final Object newInput) {
		if (newInput instanceof PwsEntryStore) {
			dataStore = (PwsEntryStore) newInput;
		}

		if (log.isDebugEnabled())
			log.debug("Input changed fired");
	}

	/**
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(final Object inputElement) {

		if (inputElement instanceof PwsEntryStore) {
			dataStore = (PwsEntryStore) inputElement;
			return dataStore.getSparseEntries().toArray();
		}
		return new PwsEntryBean[0];
	}

}