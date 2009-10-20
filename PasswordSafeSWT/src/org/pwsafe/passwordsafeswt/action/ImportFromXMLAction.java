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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.dialog.PasswordDialog;

/**
 * Imports a file from XML Source
 * 
 * @author Glen Smith
 */
public class ImportFromXMLAction extends Action {

	public ImportFromXMLAction() {
		super(Messages.getString("ImportFromXMLAction.Label")); //$NON-NLS-1$
	}
	
	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		PasswordSafeJFace app = PasswordSafeJFace.getApp();
    	FileDialog fod = new FileDialog(app.getShell(), SWT.OPEN);
    	String fileName = fod.open();
		if (fileName != null) {
	
				try {
					app.importFromXML(fileName);
				} catch (Exception e) {
					app.displayErrorDialog(Messages.getString("ImportFromXMLAction.ErrorDialog.Title"), Messages.getString("ImportFromXMLAction.ErrorDialog.Message"), e); //$NON-NLS-1$ //$NON-NLS-2$
				}

			
		}
	}

}