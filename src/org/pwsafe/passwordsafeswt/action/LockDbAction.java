/*
 * Copyright (c) 2008 David Muller <roxon@users.sourceforge.net>.
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

/**
 * Locks the password database.
 * This action expects all changes to be saved 
 * BEFORE it is called!
 *
 * @author David Mueller
 */
public class LockDbAction extends Action {

	private static final Log log = LogFactory.getLog(LockDbAction.class);

    public LockDbAction() {
        super("Lock Database");
        setToolTipText("Lock Database");
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
    	performLock();
    }
    
    public TimerTask createTaskTimer() { 
    	return new TimerTask() {
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

    protected void performLock () {
	    PasswordSafeJFace app = PasswordSafeJFace.getApp();
	    if (app.getPwsFile() != null) {
		    log.debug("locking database... ");
		    app.setPassphrase(null);
		    app.clearView();
		    app.setPwsFile(null);
		    app.setLocked(true);
	    }
    }

}