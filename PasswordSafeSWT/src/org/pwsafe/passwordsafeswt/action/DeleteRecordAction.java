/*
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants;
import org.pwsafe.passwordsafeswt.util.UserPreferences;

/**
 * Deletes the selected record after prompting.
 * 
 * @author Glen Smith
 */
public class DeleteRecordAction extends Action {

	public DeleteRecordAction() {
		super(Messages.getString("DeleteRecordAction.Label")); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader()
				.getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_delete.gif"))); //$NON-NLS-1$
		setToolTipText(Messages.getString("DeleteRecordAction.Tooltip")); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		PasswordSafeJFace app = PasswordSafeJFace.getApp();
		PwsEntryBean selectedRec = app.getSelectedRecord();
		if (selectedRec != null) {
			if (UserPreferences.getInstance().getBoolean(
					JpwPreferenceConstants.CONFIRM_ITEM_DELETION)) {
				MessageBox mb = new MessageBox(app.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				mb.setText(Messages.getString("DeleteRecordAction.Dialog.Title")); //$NON-NLS-1$
				mb.setMessage(NLS.bind(
						Messages.getString("DeleteRecordAction.Dialog.Message"), selectedRec.getTitle())); //$NON-NLS-1$
				int result = mb.open();
				if (result == SWT.YES) {
					app.deleteRecord();
				}
			} else {
				app.deleteRecord();
			}
		}

	}

}