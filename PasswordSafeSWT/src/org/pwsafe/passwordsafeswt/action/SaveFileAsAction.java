/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.pwsafe.lib.file.PwsFile;
import org.pwsafe.lib.file.PwsS3Storage;
import org.pwsafe.lib.file.PwsS3Storage.AccountDetails;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.dialog.S3CredentialsDialog;

/**
 * SaveAs command.
 *
 * @author Glen Smith
 */
public class SaveFileAsAction extends Action {

	// careful when changing - sync with extension handling further down 
	static final String[] fileExtensions = new String[]{
		"*.psafe3", "*.pws3"  //$NON-NLS-1$ //$NON-NLS-2$
	};
	
	static String[] extensionNames = new String[] {
		Messages.getString("SaveFileAsAction.FilterLabel.V3Files"), //$NON-NLS-1$
		Messages.getString("SaveFileAsAction.FilterLabel.S3Files")  //$NON-NLS-1$
	};
	
    public SaveFileAsAction() {
        super(Messages.getString("SaveFileAsAction.Label")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
	public void run() {
        PasswordSafeJFace app = PasswordSafeJFace.getApp();
        FileDialog fw = new FileDialog(app.getShell(), SWT.SAVE);
		fw.setFilterExtensions(fileExtensions); 
		fw.setFilterNames(extensionNames); 
		fw.setFileName(Messages.getString("SaveFileAsAction.NewSafeName")); //$NON-NLS-1$
        String newFilename = fw.open();

        if (newFilename == null) 
        	return;

        try {
        	String extension = fileExtensions[fw.getFilterIndex()].substring(1);
        	
        	if (!newFilename.endsWith(extension) && 
        			(! newFilename.contains("."))) { // allow other extensions
        		newFilename += extension;
        	}
	        if (! newFilename.endsWith(PwsS3Storage.FILE_EXTENSION)) {
	        	app.saveFileAs(newFilename);
	        	
	        } else {
	        	S3CredentialsDialog sc = new S3CredentialsDialog(app);      	
	        	AccountDetails ad = sc.getAccountDetails();
	        	if (ad == null) {
	        		return;
	        	}

	        	PwsFile pwsFile = app.getPwsFile();
	        	
	        	pwsFile.setStorage(new PwsS3Storage (newFilename, ad, pwsFile.getPassphrase()));
	        	app.saveFile();
	        }
//    	SaveSafeSelectionWizard newSafeWizard = new SaveSafeSelectionWizard ();
//    	WizardDialog dlg = new WizardDialog(app.getShell(), newSafeWizard);
//    	int rc = dlg.open();
//        if (rc != 1) {
        
            } catch (IOException e1) {
                app.displayErrorDialog(Messages.getString("SaveFileAsAction.ErrorDialog.Title"), e1.getMessage(), e1); //$NON-NLS-1$
            } catch (NoSuchAlgorithmException e) {
                app.displayErrorDialog(Messages.getString("SaveFileAsAction.ErrorDialog.Title"), e.getMessage(), e); //$NON-NLS-1$
			}

        
    }

}