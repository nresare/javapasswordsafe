/*
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.dialog.EditDialog;
import org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants;

/**
 * Displays the Edit dialog.
 * 
 * @author Glen Smith
 */
public class EditRecordAction extends Action {

	public EditRecordAction() {
		super(Messages.getString("EditRecordAction.Label")); //$NON-NLS-1$
		setAccelerator(SWT.MOD1 | Character.LINE_SEPARATOR);
		setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader()
				.getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_edit.gif"))); //$NON-NLS-1$
		setToolTipText(Messages.getString("EditRecordAction.Tooltip")); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		final PasswordSafeJFace app = PasswordSafeJFace.getApp();
		final PwsEntryBean selectedRecord = app.getSelectedRecord();
		if (selectedRecord != null) {
			final PwsEntryBean filledEntry = app.getPwsDataStore().getEntry(
					selectedRecord.getStoreIndex());
			EditDialog dialogue = new EditDialog(app.getShell(), filledEntry);
			app.getLockStatus().addObserver(dialogue);
			final PwsEntryBean changedEntry;
			try {
				changedEntry = (PwsEntryBean) dialogue.open();
			} finally {
				app.getLockStatus().deleteObserver(dialogue);
			}
			if (!app.isReadOnly()) {
				final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
				final boolean recordAccessTime = thePrefs
						.getBoolean(JpwPreferenceConstants.RECORD_LAST_ACCESS_TIME);
				if (changedEntry != null) {
					if (recordAccessTime) {
						changedEntry.setLastAccess(new Date());
					}
					app.updateRecord(changedEntry);
				} else if (recordAccessTime) { // we still have to update the
												// record
					filledEntry.setLastAccess(new Date());
					app.updateAccessTime(filledEntry);
				}
			}
		}

	}

}