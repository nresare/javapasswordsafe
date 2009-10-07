/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.dialog.PasswordDialog;
import org.pwsafe.passwordsafeswt.util.UserPreferences;

/**
 * Locks the password database.
 * This action expects all changes to be saved 
 * <b>BEFORE</b> it is called!
 *
 * @author David Mueller
 */
public class LockDbAction extends Action {

	private static final Log log = LogFactory.getLog(LockDbAction.class);

    public LockDbAction() {
        super(Messages.getString("LockDbAction.Label")); //$NON-NLS-1$
        setToolTipText(Messages.getString("LockDbAction.Tooltip")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
	public void run() {
    	performLock();
    }
    
    public TimerTask createTaskTimer() { 
    	return new TimerTask() {
    		@Override
			public void run () {
    			//calls outer class
    			PasswordSafeJFace.getApp().getShell().getDisplay().syncExec(
    					new Runnable () {
    						public void run () {
    							performLock();
    						}
    					});
    			
    		}
    	};
    }

    public void performLock () {
	    PasswordSafeJFace app = PasswordSafeJFace.getApp();
	    if (app.getPwsFile() != null) {
		    log.debug(Messages.getString("LockDbAction.Log.Locking")); //$NON-NLS-1$
		    app.getPwsFile().dispose();
		    app.clearView();
		    app.setPwsFile(null);
		    app.setLocked(true);
	    }
    }

    public boolean performUnlock() {
    	boolean isUnlocked = false;
    	// calling code should stop a running locker timer first
	    PasswordSafeJFace app = PasswordSafeJFace.getApp();
    	log.info(Messages.getString("LockDbAction.Log.TryToUnlock")); //$NON-NLS-1$
        PasswordDialog pd = new PasswordDialog(app.getShell());
        pd.setVerified(false);
        String fileName = UserPreferences.getInstance().getMRUFile();
        pd.setFileName(fileName);
        StringBuilder password = pd.open();
        if (password != null) {
            try {
                app.openFile(fileName, password); // readonly state stays unchanged
                isUnlocked = true;
                app.setLocked(false);
            } catch (Exception anEx) {
                app.displayErrorDialog(Messages.getString("LockDbAction.ReOpenError.Title"), Messages.getString("LockDbAction.ReOpenError.Message"), anEx); //$NON-NLS-1$ //$NON-NLS-2$
            }
		} 
        return isUnlocked;
    }

}