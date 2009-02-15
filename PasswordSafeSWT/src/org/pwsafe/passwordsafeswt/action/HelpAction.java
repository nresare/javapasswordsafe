/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;

/**
 * Displays the Help.
 *
 * @author Glen Smith
 */
public class HelpAction extends Action {

    public HelpAction() {
        super(Messages.getString("HelpAction.Label")); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader().getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_help.gif"))); //$NON-NLS-1$
        setToolTipText(Messages.getString("HelpAction.Tooltip")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        final PasswordSafeJFace app = PasswordSafeJFace.getApp();
        //TODO show help here...
    }

}