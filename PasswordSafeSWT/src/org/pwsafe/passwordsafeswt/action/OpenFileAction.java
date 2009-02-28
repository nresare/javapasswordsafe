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
 * Open command.
 *
 * @author Glen Smith
 */
public class OpenFileAction extends Action {

    public OpenFileAction() {
        super(Messages.getString("OpenFileAction.Label")); //$NON-NLS-1$
        setAccelerator( SWT.MOD1 | 'O'  );
        setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader().getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_open.gif"))); //$NON-NLS-1$
        setToolTipText(Messages.getString("OpenFileAction.Tooltip")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        PasswordSafeJFace app = PasswordSafeJFace.getApp();
        boolean cancelled = app.saveAppIfDirty();
        if (!cancelled) {
            FileDialog fod = new FileDialog(app.getShell(), SWT.OPEN);
            fod.setFilterExtensions(new String[] { "*.psafe3", "*.dat", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            fod.setFilterNames(new String[] { Messages.getString("OpenFileAction.FilterLabel.V3Files"), Messages.getString("OpenFileAction.FilterLabel.V2Files"), Messages.getString("OpenFileAction.FilterLabel.AllFiles")} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            String fileName = fod.open();
            if (fileName != null) {
                PasswordDialog pd = new PasswordDialog(app.getShell());
                pd.setVerified(false);
                pd.setFileName(fileName);
                String password = (String) pd.open();
                if (password != null) {
                    try {
                        app.openFile(fileName, password);
                    } catch (Exception e) {
                        app.displayErrorDialog(Messages.getString("OpenFileAction.ErrorDialog.Label"), Messages.getString("OpenFileAction.ErrorDialog.Message"), e); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            }
        }
    }

}