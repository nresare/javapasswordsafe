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

/**
 * Changes the safe to Tree view.
 *
 * @author Glen Smith
 */
public class ViewAsTreeAction extends Action {

    public ViewAsTreeAction() {
        super(Messages.getString("ViewAsTreeAction.Label"), AS_RADIO_BUTTON); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        final PasswordSafeJFace app = PasswordSafeJFace.getApp();
        app.showTreeView();
    }

}