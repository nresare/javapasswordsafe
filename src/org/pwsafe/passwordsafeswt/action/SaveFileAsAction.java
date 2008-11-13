/*
 * Copyright (c) 2008 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;

/**
 * SaveAs command.
 *
 * @author Glen Smith
 */
public class SaveFileAsAction extends Action {

    public SaveFileAsAction() {
        super("Save &As");
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        PasswordSafeJFace app = PasswordSafeJFace.getApp();
        FileDialog fw = new FileDialog(app.getShell(), SWT.SAVE);
        String newFilename = fw.open();
        if (newFilename != null) {
            try {
                app.saveFileAs(newFilename);
            } catch (IOException e1) {
                app.displayErrorDialog("Error Saving Safe", e1.getMessage(), e1);
            } catch (NoSuchAlgorithmException e) {
                app.displayErrorDialog("Error Saving Safe", e.getMessage(), e);
			}

        }

    }

}