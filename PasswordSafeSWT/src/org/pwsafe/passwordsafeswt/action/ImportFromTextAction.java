/*
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
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
 * Imports a file from CSV Source
 * 
 * @author Glen Smith
 */
public class ImportFromTextAction extends Action {

	public ImportFromTextAction() {
		super(Messages.getString("ImportFromTextAction.Label")); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		PasswordSafeJFace app = PasswordSafeJFace.getApp();
		FileDialog fod = new FileDialog(app.getShell(), SWT.OPEN);
		String fileName = fod.open();
		if (fileName != null) {

			try {
				app.importFromText(fileName);
			} catch (Exception e) {
				app.displayErrorDialog(
						Messages.getString("ImportFromTextAction.ErrorDialog.Title"), Messages.getString("ImportFromTextAction.ErrorDialog.Message"), e); //$NON-NLS-1$ //$NON-NLS-2$
			}

		}
	}

}