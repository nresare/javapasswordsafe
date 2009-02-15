/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.MessageBox;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;


/**
 * Launches a browser to the passwordsafe website.
 *
 * @author Glen Smith
 */
public class VisitPasswordSafeWebsiteAction extends Action {

    public VisitPasswordSafeWebsiteAction() {
        super(Messages.getString("VisitPwWebsiteAction.Label")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
	public void run() {
        final PasswordSafeJFace app = PasswordSafeJFace.getApp();
        new Thread() {
            @Override
			public void run() {
                try {
                		Program.launch("http://jpwsafe.sf.net/"); //$NON-NLS-1$
                } catch (Exception ioe) {
                    MessageBox mb = new MessageBox(app.getShell(),
                            SWT.ICON_ERROR);
                    mb.setText(Messages.getString("VisitPwWebsiteAction.ErrorDialog.Title")); //$NON-NLS-1$
                    mb.setMessage(Messages.getString("VisitPwWebsiteAction.ErrorDialog.Message")); //$NON-NLS-1$
                    mb.open();
                }
            }
        }.start();
    }

}