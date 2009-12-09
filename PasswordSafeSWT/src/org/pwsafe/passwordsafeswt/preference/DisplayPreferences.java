/*
 * $Id$
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.preference;

import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.ALWAYS_ON_TOP;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.SHOW_ICON_IN_SYSTEM_TRAY;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.SHOW_NOTES_IN_EDIT_MODE;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.SHOW_NOTES_IN_LIST;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.SHOW_PASSWORD_IN_EDIT_MODE;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * GUI related preferences.
 * 
 * @author Glen Smith
 */
public class DisplayPreferences extends PreferencePage {

	Button btnAlwaysOnTop;
	Button btnShowNotesInList;
	Button btnShowNotesInEdit;
	Button btnShowPasswordInEdit;
	Button btnSystemTray;

	// Text fields for user to enter preferences

	public DisplayPreferences() {
		super();
	}

	/**
	 * Creates the controls for this page
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		
		// Get the preference store and setup defaults
	    IPreferenceStore preferenceStore = getPreferenceStore();
	    
		btnAlwaysOnTop = new Button(composite, SWT.CHECK);
		btnAlwaysOnTop.setText(Messages.getString("DisplayPreferences.AlwaysOnTop")); //$NON-NLS-1$
		btnAlwaysOnTop.setSelection(preferenceStore.getBoolean(ALWAYS_ON_TOP));
		btnAlwaysOnTop.setEnabled(false);

		btnShowNotesInList = new Button(composite, SWT.CHECK);
		btnShowNotesInList.setText(Messages.getString("DisplayPreferences.ShowNotesInList")); //$NON-NLS-1$
		btnShowNotesInList.setSelection(preferenceStore.getBoolean(SHOW_NOTES_IN_LIST));

		btnShowNotesInEdit = new Button(composite, SWT.CHECK);
		btnShowNotesInEdit.setText(Messages.getString("DisplayPreferences.ShowNotesInEdit")); //$NON-NLS-1$
		btnShowNotesInEdit.setSelection(preferenceStore.getBoolean(SHOW_NOTES_IN_EDIT_MODE));
		btnShowNotesInEdit.setEnabled(false);

		btnShowPasswordInEdit = new Button(composite, SWT.CHECK);
		btnShowPasswordInEdit.setText(Messages.getString("DisplayPreferences.ShowPasswordInEdit")); //$NON-NLS-1$
		btnShowPasswordInEdit.setSelection(preferenceStore.getBoolean(SHOW_PASSWORD_IN_EDIT_MODE));

		btnSystemTray = new Button(composite, SWT.CHECK);
		btnSystemTray.setText(Messages.getString("DisplayPreferences.SystemTrayOn")); //$NON-NLS-1$
		btnSystemTray.setSelection(preferenceStore.getBoolean(SHOW_ICON_IN_SYSTEM_TRAY));

		return composite;
	}
	

	/**
	 * Called when user clicks Restore Defaults
	 */
	@Override
	protected void performDefaults() {
		// Get the preference store
		IPreferenceStore preferenceStore = getPreferenceStore();

		// Reset the fields to the defaults
		btnAlwaysOnTop.setSelection(preferenceStore.getDefaultBoolean(ALWAYS_ON_TOP));
		btnShowNotesInList.setSelection(preferenceStore.getDefaultBoolean(SHOW_NOTES_IN_LIST));
		btnShowNotesInEdit.setSelection(preferenceStore.getDefaultBoolean(SHOW_NOTES_IN_EDIT_MODE));
		btnShowPasswordInEdit.setSelection(preferenceStore.getDefaultBoolean(SHOW_PASSWORD_IN_EDIT_MODE));
		btnSystemTray.setSelection(preferenceStore.getDefaultBoolean(SHOW_ICON_IN_SYSTEM_TRAY));
	}

	/**
	 * Called when user clicks Apply or OK
	 * 
	 * @return boolean true if the dialog is allowed to close, false otherwise
	 */
	@Override
	public boolean performOk() {
		// Get the preference store
		IPreferenceStore preferenceStore = getPreferenceStore();

		// Set the values from the fields
		if (btnAlwaysOnTop != null) preferenceStore.setValue(ALWAYS_ON_TOP, btnAlwaysOnTop.getSelection());
	    if (btnShowNotesInList != null) preferenceStore.setValue(SHOW_NOTES_IN_LIST, btnShowNotesInList.getSelection());
	    if (btnShowNotesInEdit != null)
	        preferenceStore.setValue(SHOW_NOTES_IN_EDIT_MODE, btnShowPasswordInEdit.getSelection());
	    if (btnShowPasswordInEdit != null)
	        preferenceStore.setValue(SHOW_PASSWORD_IN_EDIT_MODE, btnShowPasswordInEdit.getSelection());
	    if (btnSystemTray != null)
	        preferenceStore.setValue(SHOW_ICON_IN_SYSTEM_TRAY, btnSystemTray.getSelection());

		// Return true to allow dialog to close
		return true;
	}
}