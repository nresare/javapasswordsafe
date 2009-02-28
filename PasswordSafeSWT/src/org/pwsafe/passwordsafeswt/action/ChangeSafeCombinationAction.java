/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.eclipse.jface.action.Action;
import org.pwsafe.lib.file.PwsFile;
import org.pwsafe.lib.file.PwsStorage;
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
		PwsFile pf = app.getPwsFile();
		if (pf != null) {
			PwsStorage pfs = pf.getStorage();
			if (pfs == null) pd.setFileName("Untitled Safe");
			else {
				pd.setFileName(pfs.getIdentifier());
			}
			String newPassword = (String) pd.open();
			if (newPassword != null) {
				new SaveFileAction().run();
			}
		}
	}

}