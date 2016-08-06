/*
 * $Id$
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.dnd.Clipboard;
import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants;

/**
 * Copies the URL from selected item to the clipboard.
 * 
 * @author Glen Smith, Tim Hughes
 */
public class CopyURLAction extends Action {
	private static final Log log = LogFactory.getLog(CopyURLAction.class);

	public CopyURLAction() {
		super(Messages.getString("CopyURLAction.Label")); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader()
				.getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_url.gif"))); //$NON-NLS-1$
		setToolTipText(Messages.getString("CopyURLAction.Tooltip")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {

		return super.isEnabled();

		// TODO: the following will always disable the actions, find a better
		// way:
		// PasswordSafeJFace app = PasswordSafeJFace.getApp();
		//
		// PwsEntryBean selected = app.getSelectedRecord();
		// if (selected == null)
		// return false;
		//
		// if (selected.getUrl() != null) {
		// return true;
		// }
		//
		// return false;
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		// TODO: disable option if v1 or v2; URL only seems to be available in
		// V3 files
		final PasswordSafeJFace app = PasswordSafeJFace.getApp();

		final PwsEntryBean selected = app.getSelectedRecord();
		if (selected == null)
			return;

		// TODO: only fetch a filled entry if URL is not part of sparse fields.
		PwsEntryBean theEntry;
		if (selected.getUrl() != null && selected.getUrl().length() > 0) {
			theEntry = selected;
		} else {// retrieve filled Entry for sparse
			theEntry = app.getPwsDataStore().getEntry(selected.getStoreIndex());
		}

		Clipboard cb = new Clipboard(app.getShell().getDisplay());

		app.copyToClipboard(cb, theEntry.getUrl());

		final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
		final boolean recordAccessTime = thePrefs
				.getBoolean(JpwPreferenceConstants.RECORD_LAST_ACCESS_TIME);
		if (recordAccessTime) { // this could/should be sent to a background
								// thread
			app.updateAccessTime(theEntry);
		}

		cb.dispose();
	}
}
