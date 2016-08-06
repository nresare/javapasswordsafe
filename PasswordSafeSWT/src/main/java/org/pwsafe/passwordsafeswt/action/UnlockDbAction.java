/*
 * $Id$
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.dialog.PasswordDialog;
import org.pwsafe.passwordsafeswt.util.UserPreferences;

/**
 * Unlocks the password database.
 * 
 * @author David Mueller
 */
public class UnlockDbAction extends Action implements Runnable {

	private static final Log log = LogFactory.getLog(UnlockDbAction.class);

	public UnlockDbAction() {
		super();
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		performUnlock();
	}

	public boolean performUnlock() {
		boolean isUnlocked = false;
		// calling code should stop a running locker timer first
		PasswordSafeJFace app = PasswordSafeJFace.getApp();
		log.info(Messages.getString("LockDbAction.Log.TryToUnlock")); //$NON-NLS-1$
		PasswordDialog pd;
		if (app.getShell().isVisible()) {
			pd = new PasswordDialog(app.getShell());
		} else { // use a new shell so the main window is not shown first
			Display display = Display.getDefault();
			Shell parent = new Shell(display);
			pd = new PasswordDialog(parent);
		}

		pd.setVerified(false);
		String fileName = UserPreferences.getInstance().getMRUFile();
		pd.setFileName(fileName);
		StringBuilder password = pd.open();
		if (password != null && !"".equals(password)) {
			try {
				app.openFile(fileName, password); // readonly state stays
													// unchanged
				isUnlocked = true;
				app.setLocked(false);
			} catch (Exception anEx) {
				app.displayErrorDialog(
						Messages.getString("LockDbAction.ReOpenError.Title"), Messages.getString("LockDbAction.ReOpenError.Message"), anEx); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return isUnlocked;
	}

}