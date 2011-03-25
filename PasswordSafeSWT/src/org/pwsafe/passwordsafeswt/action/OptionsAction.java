/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.preference.DisplayPreferences;
import org.pwsafe.passwordsafeswt.preference.MiscPreferences;
import org.pwsafe.passwordsafeswt.preference.PasswordPolicyPreferences;
import org.pwsafe.passwordsafeswt.preference.SecurityPreferences;
import org.pwsafe.passwordsafeswt.preference.UsernamePreferences;
import org.pwsafe.passwordsafeswt.util.IOUtils;
import org.pwsafe.passwordsafeswt.util.UserPreferences;


/**
 * Displays the options dialog to manage user preferences.
 *
 * @author Glen Smith
 */
public class OptionsAction extends Action {

    public OptionsAction() {
        super(Messages.getString("OptionsAction.Label")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        final PasswordSafeJFace app = PasswordSafeJFace.getApp();

        // Create the preference manager
        PreferenceManager mgr = new PreferenceManager();

        // Create the nodes
        PreferenceNode displayPrefs = new PreferenceNode("display", Messages.getString("OptionsAction.DisplayNode"), null, DisplayPreferences.class.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		PreferenceNode securityPrefs = new PreferenceNode("security", Messages.getString("OptionsAction.SecurityNode"), null, SecurityPreferences.class //$NON-NLS-1$ //$NON-NLS-2$
				.getName());
		PreferenceNode passwordPolicyPrefs = new PreferenceNode("policy", Messages.getString("OptionsAction.PolicyNode"), null, //$NON-NLS-1$ //$NON-NLS-2$
				PasswordPolicyPreferences.class.getName());
		PreferenceNode usernamePrefs = new PreferenceNode("username", Messages.getString("OptionsAction.UserNameNode"), null, UsernamePreferences.class //$NON-NLS-1$ //$NON-NLS-2$
				.getName());
        PreferenceNode miscPrefs = new PreferenceNode("misc", Messages.getString("OptionsAction.MiscNode"), null, MiscPreferences.class.getName()); //$NON-NLS-1$ //$NON-NLS-2$

        // Add the nodes
        mgr.addToRoot(displayPrefs);
        mgr.addToRoot(securityPrefs);
        mgr.addToRoot(passwordPolicyPrefs);
        mgr.addToRoot(usernamePrefs);
        mgr.addToRoot(miscPrefs);

        // Create the preferences dialog
        PreferenceDialog dlg = new PreferenceDialog(app.getShell(), mgr);
		PreferenceDialog.setDefaultImage(IOUtils.getImage(PasswordSafeJFace.class,
				"/org/pwsafe/passwordsafeswt/images/clogo.gif")); //$NON-NLS-1$

        // Set the preference store
        dlg.setPreferenceStore(JFacePreferences.getPreferenceStore());

        // Open the dialog
        dlg.open();

        try {
        	if (JFacePreferences.getPreferenceStore().needsSaving()) {
        		// Be Paranoid - Save the preferences now
        		UserPreferences.getInstance().savePreferences();
        	}
        } catch (IOException e) {
          e.printStackTrace();
        }
       

    }

}