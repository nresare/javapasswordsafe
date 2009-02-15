/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.eclipse.jface.action.Action;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.dialog.PasswordDialog;

/**
 * Changes the safe combination.
 *
 * @author Glen Smith
 */
public class ChangeSafeCombinationAction extends Action {

    public ChangeSafeCombinationAction() {
        super(Messages.getString("ChangeSafeCombinationAction.Label")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        PasswordSafeJFace app = PasswordSafeJFace.getApp();
        PasswordDialog pd = new PasswordDialog(app.getShell());
        if (app.getPwsFile() != null) {
            //pd.setFileName(app.getPwsFile().getFilename());
        	// FIXME
        	pd.setFileName("FIXME"); //$NON-NLS-1$
            String newPassword = (String) pd.open();
            if (newPassword != null) {
                app.setPassphrase(newPassword);
            }
        }
    }

}