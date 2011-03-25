/*
 * $Id$
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

/**
 * Locks the password database.
 * This action expects all changes to be saved 
 * <b>BEFORE</b> it is called!
 *
 * Implements Runnable so that it can be called asynchronously.
 *
 * @author David Mueller
 */
public class LockDbAction extends Action implements Runnable {

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
    			//calls outer class, use the event tread
    			//TODO: have minimize in an extra Timer, and with a shorter period that so that it won't
    			PasswordSafeJFace.getApp().getShell().getDisplay().syncExec(
    					new Runnable () {
    						public void run () {
    							PasswordSafeJFace app = PasswordSafeJFace.getApp();
    							if (! app.getShell().getMinimized()) {
    								PasswordSafeJFace.getApp().getShell().setMinimized(true);
    							}
    							if (! app.isLocked()) {
    								performLock();
    							}
    						}
    					});
    			
    		}
    	};
    }

    public void performLock () {
	    PasswordSafeJFace app = PasswordSafeJFace.getApp();
	    if (app.getPwsFile() != null) {
		    log.info(Messages.getString("LockDbAction.Log.Locking")); //$NON-NLS-1$
		    app.getPwsFile().dispose();
		    app.clearView();
		    app.setPwsFile(null);
		    app.setLocked(true);
	    }
    }
}