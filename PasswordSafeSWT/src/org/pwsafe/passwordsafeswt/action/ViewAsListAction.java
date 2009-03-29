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
 * Changes the safe to List view.
 *
 * @author Glen Smith
 */
public class ViewAsListAction extends Action {

    public ViewAsListAction() {
        super(Messages.getString("ViewAsListAction.Label"), AS_RADIO_BUTTON); //$NON-NLS-1$
        setChecked(true); // default radio
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
	public void run() {
        final PasswordSafeJFace app = PasswordSafeJFace.getApp();
        if (isChecked()) {
        	app.showListView();
        	app.updateViewers();
        }
    }

    
}