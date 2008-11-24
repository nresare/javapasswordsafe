/*
 * Copyright (c) 2008 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.dialog.NewSafeDialog;

/**
 * New command.
 *
 * @author Glen Smith
 */
public class NewFileAction extends Action {

    public NewFileAction() {
        super("&New File");
        setAccelerator( SWT.MOD1 | 'N'  );
        setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader().getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_new.gif")));
        setToolTipText("Create New Safe");
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        PasswordSafeJFace app = PasswordSafeJFace.getApp();
        boolean cancelled = app.saveAppIfDirty();
        if (!cancelled) {
            NewSafeDialog nsf = new NewSafeDialog(app.getShell());
            String passphrase = (String) nsf.open();
            if (passphrase != null) {
                app.newFile(passphrase);
            }
        }
    }

}