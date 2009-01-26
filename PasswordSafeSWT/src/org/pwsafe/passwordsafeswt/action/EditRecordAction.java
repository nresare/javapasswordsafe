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
import org.pwsafe.lib.file.PwsRecord;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.dialog.EditDialog;
import org.pwsafe.passwordsafeswt.dto.PwsEntryDTO;

/**
 * Displays the Edit dialog.
 *
 * @author Glen Smith
 */
public class EditRecordAction extends Action {

    public EditRecordAction() {
        super("&Edit Record");
        setAccelerator( SWT.MOD1 | Character.LINE_SEPARATOR  );
        setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader().getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_edit.gif")));
        setToolTipText("Edit Selected Record");
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        PasswordSafeJFace app = PasswordSafeJFace.getApp();
        PwsRecord selectedRecord = app.getSelectedRecord();
        if (selectedRecord != null) {
            PwsEntryDTO newEntry = PwsEntryDTO.fromPwsRecord(selectedRecord);
            EditDialog ed = new EditDialog(app.getShell(), newEntry);
            newEntry = (PwsEntryDTO) ed.open();
            if (newEntry != null) {
                app.editRecord(newEntry);
            }
        }

    }

}