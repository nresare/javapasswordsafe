/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.dialog.EditDialog;

/**
 * Displays the Edit dialog.
 *
 * @author Glen Smith
 */
public class EditRecordAction extends Action {

    public EditRecordAction() {
        super(Messages.getString("EditRecordAction.Label")); //$NON-NLS-1$
        setAccelerator( SWT.MOD1 | Character.LINE_SEPARATOR  );
        setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader().getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_edit.gif"))); //$NON-NLS-1$
        setToolTipText(Messages.getString("EditRecordAction.Tooltip")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
	public void run() {
        PasswordSafeJFace app = PasswordSafeJFace.getApp();
        PwsEntryBean selectedRecord = app.getSelectedRecord();
        if (selectedRecord != null) {
        	PwsEntryBean filledEntry = app.getPwsDataStore().getEntry(selectedRecord.getStoreIndex());
            EditDialog ed = new EditDialog(app.getShell(), filledEntry);
            filledEntry = (PwsEntryBean) ed.open();
            if (! app.isReadOnly() && filledEntry != null) {
                app.editRecord(filledEntry);
            }
        }

    }

}