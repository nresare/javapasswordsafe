/*
 * $Id$
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants;
import org.pwsafe.passwordsafeswt.util.IOUtils;

/**
 * Open the URL from selected item to the clipboard.
 *
 * @author roxon
 */
public class OpenUrlAction extends Action {
    private static final Log log = LogFactory.getLog(OpenUrlAction.class);

    public OpenUrlAction() {
        super(Messages.getString("OpenUrlAction.Label")); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader().getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_openurl.gif"))); //$NON-NLS-1$
        setToolTipText(Messages.getString("OpenUrlAction.Tooltip")); //$NON-NLS-1$
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		// TODO: only enable if an URL is available
		return super.isEnabled();
	}

	/**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
	public void run() {
        PasswordSafeJFace app = PasswordSafeJFace.getApp();

        // TODO: this should check if URL is part of sparse fields
        PwsEntryBean selected = app.getSelectedRecord();
        if (selected == null || selected.getUrl() == null || selected.getUrl().length() == 0)
        	return;
        
        IOUtils.openBrowser(selected.getUrl());
        
        final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
        final boolean recordAccessTime =  thePrefs.getBoolean(JpwPreferenceConstants.RECORD_LAST_ACCESS_TIME);
        if (recordAccessTime) {// this could/should be sent to a background thread
        	app.updateAccessTime(selected);
        }
    }
}
