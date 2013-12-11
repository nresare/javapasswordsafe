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
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;

/**
 * Clears the current user clipboard.
 * 
 * @author Glen Smith
 */
public class ClearClipboardAction extends Action {

	public ClearClipboardAction() {
		super(Messages.getString("ClearClipboardAction.Label")); //$NON-NLS-1$
		setAccelerator(SWT.MOD1 | SWT.DEL);
		setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader()
				.getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_clearclip.gif"))); //$NON-NLS-1$
		setToolTipText(Messages.getString("ClearClipboardAction.Tooltip")); //$NON-NLS-1$

	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		PasswordSafeJFace app = PasswordSafeJFace.getApp();

		Clipboard cb = new Clipboard(app.getShell().getDisplay());

		cb.setContents(new Object[] { "  " }, //$NON-NLS-1$
				new Transfer[] { TextTransfer.getInstance() });

		cb.dispose();

	}

}