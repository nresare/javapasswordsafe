/*
 * Copyright (c) 2008 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;

/**
 * Exports the safe to a text file.
 *
 * @author Glen Smith
 */
public class ExportToTextAction extends Action {

    public ExportToTextAction() {
        super("Plain Text (tab separated)...");
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        final PasswordSafeJFace app = PasswordSafeJFace.getApp();
        FileDialog fw = new FileDialog(app.getShell(), SWT.SAVE);
        String newFilename = fw.open();
        if (newFilename != null) {
            app.exportToText(newFilename);
        }
    }

}