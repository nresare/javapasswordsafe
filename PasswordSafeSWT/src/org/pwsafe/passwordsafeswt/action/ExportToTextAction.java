/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.dialog.PasswordDialog;

/**
 * Exports the safe to a text file.
 *
 * @author Glen Smith
 */
public class ExportToTextAction extends Action {

	private static final Log log = LogFactory.getLog(ExportToTextAction.class);

    public ExportToTextAction() {
        super(Messages.getString("ExportToTextAction.Label")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
	public void run() {
        final PasswordSafeJFace app = PasswordSafeJFace.getApp();
        PasswordDialog pw = new PasswordDialog(app.getShell());
        pw.setVerified(false);
        StringBuilder password = pw.open();
        if (password == null)
        	return;
        //TODO: change pwsFile passphrase access to StringBuilder & use a correct equals
        if (password.toString().equals(app.getPwsFile().getPassphrase())) {
	        FileDialog fw = new FileDialog(app.getShell(), SWT.SAVE);
	        String newFilename = fw.open();
	        if (newFilename != null) {
	            app.exportToText(newFilename);
	        }
        } else {
        	app.setStatus(Messages.getString("ExportToTextAction.AbortedStatus")); //$NON-NLS-1$
        	log.warn("Aborted text export after wrong safe combination"); //$NON-NLS-1$
        }
    }

}