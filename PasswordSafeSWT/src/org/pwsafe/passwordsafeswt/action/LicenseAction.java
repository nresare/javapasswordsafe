/*
 * $Id$
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.eclipse.jface.action.Action;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.dialog.LicenseDialog;

/**
 * Defines a License action for easy creation of buttons etc.
 * 
 * @author roxon
 *
 */
public class LicenseAction extends Action {

    public LicenseAction() {
        super(Messages.getString("LicenseAction.Label")); //$NON-NLS-1$
//        setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader().getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_edit.gif"))); //$NON-NLS-1$
        setToolTipText(Messages.getString("LicenseAction.Tooltip")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
	public void run() {
        PasswordSafeJFace app = PasswordSafeJFace.getApp();
        	LicenseDialog ed = new LicenseDialog(app.getShell());
        	ed.open();
    }

}
