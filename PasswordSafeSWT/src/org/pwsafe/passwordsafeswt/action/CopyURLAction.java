/*
 * $Id$
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.dnd.Clipboard;
import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants;

/**
 * Copies the URL from selected item to the clipboard.
 *
 * @author Glen Smith, Tim Hughes
 */
public class CopyURLAction extends Action {
    private static final Log log = LogFactory.getLog(CopyURLAction.class);

    public CopyURLAction() {
        super(Messages.getString("CopyURLAction.Label")); //$NON-NLS-1$
        setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader().getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_url.gif"))); //$NON-NLS-1$
        // TODO: set disabled image
        setToolTipText(Messages.getString("CopyURLAction.Tooltip")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
	public void run() {
        // todo: disable option if v1 or v2; URL only seems to be available in V3 files
        PasswordSafeJFace app = PasswordSafeJFace.getApp();

        PwsEntryBean selected = app.getSelectedRecord();
        if (selected == null)
        	return;
        // retrieve filled Entry for sparse
        PwsEntryBean theEntry = app.getPwsDataStore().getEntry(selected.getStoreIndex());

        Clipboard cb = new Clipboard(app.getShell().getDisplay());

        app.copyToClipboard(cb, theEntry, theEntry.getUrl() );

        cb.dispose();
        
        final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
        final boolean recordAccessTime =  thePrefs.getBoolean(JpwPreferenceConstants.RECORD_LAST_ACCESS_TIME);
        if (recordAccessTime) {
        	theEntry.setLastAccess(new Date());
        	app.updateRecord(theEntry);
        }

    }
}
