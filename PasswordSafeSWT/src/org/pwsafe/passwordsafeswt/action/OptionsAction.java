/*
 * Copyright (c) 2008 David Muller <roxon@users.sourceforge.net>.
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
import org.pwsafe.passwordsafeswt.util.UserPreferences;

import com.swtdesigner.SWTResourceManager;

/**
 * Displays the options dialog to manage user preferences.
 *
 * @author Glen Smith
 */
public class OptionsAction extends Action {

    public OptionsAction() {
        super("Options...");
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        final PasswordSafeJFace app = PasswordSafeJFace.getApp();

        // Create the preference manager
        PreferenceManager mgr = new PreferenceManager();

        // Create the nodes
        PreferenceNode displayPrefs = new PreferenceNode("display", "Display", null, DisplayPreferences.class.getName());
		PreferenceNode securityPrefs = new PreferenceNode("security", "Security", null, SecurityPreferences.class
				.getName());
		PreferenceNode passwordPolicyPrefs = new PreferenceNode("policy", "Password Policy", null,
				PasswordPolicyPreferences.class.getName());
		PreferenceNode usernamePrefs = new PreferenceNode("username", "Username", null, UsernamePreferences.class
				.getName());
        PreferenceNode miscPrefs = new PreferenceNode("misc", "Misc", null, MiscPreferences.class.getName());

        // Add the nodes
        mgr.addToRoot(displayPrefs);
        mgr.addToRoot(securityPrefs);
        mgr.addToRoot(passwordPolicyPrefs);
        mgr.addToRoot(usernamePrefs);
        mgr.addToRoot(miscPrefs);

        // Create the preferences dialog
        PreferenceDialog dlg = new PreferenceDialog(app.getShell(), mgr);
		PreferenceDialog.setDefaultImage(SWTResourceManager.getImage(PasswordSafeJFace.class,
				"/org/pwsafe/passwordsafeswt/images/clogo.gif"));

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