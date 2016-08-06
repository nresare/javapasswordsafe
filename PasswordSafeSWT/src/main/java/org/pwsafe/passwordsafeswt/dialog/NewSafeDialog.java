/*
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

/**
 * Prompts the user for passwords for a new safe.
 * 
 * @author Glen Smith
 */
public class NewSafeDialog extends PasswordDialog {

	public NewSafeDialog(Shell parent, int style) {
		super(parent, style);
		this.purposeText = Messages.getString("NewSafeDialog.Info"); //$NON-NLS-1$
		this.windowTitle = Messages.getString("NewSafeDialog.Title"); //$NON-NLS-1$
	}

	public NewSafeDialog(Shell parent) {
		this(parent, SWT.NONE);
	}

}
