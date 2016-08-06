/*
 * $Id$
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;

/**
 * Exit command.
 * 
 * @author Glen Smith
 */
public class ExitAppAction extends Action implements Runnable {

	public ExitAppAction() {
		super(Messages.getString("ExitAppAction.Label")); //$NON-NLS-1$
		setAccelerator(SWT.MOD1 | 'Q');
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		PasswordSafeJFace app = PasswordSafeJFace.getApp();
		boolean cancelled = app.saveAppIfDirty();
		if (!cancelled) {
			app.exitApplication();
		}
	}

}