/*
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.preference;

import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.DEFAULT_USERNAME;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.QUERY_FOR_DEFAULT_USERNAME;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.USE_DEFAULT_USERNAME;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Preferences related to default username.
 * 
 * @author Glen Smith
 */
public class UsernamePreferences extends PreferencePage {

	// Text fields for user to enter preferences
	private Button btnUseDefaultUsername;
	private Text txtUsername;
	private Button btnQuerySetUsername;

	/**
	 * Creates the controls for this page
	 */
	@Override
	protected Control createContents(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());

		// Get the preference store
		final IPreferenceStore preferenceStore = getPreferenceStore();

		btnUseDefaultUsername = new Button(composite, SWT.CHECK);
		btnUseDefaultUsername.setText(Messages.getString("UsernamePreferences.UseDefaultUsername")); //$NON-NLS-1$
		btnUseDefaultUsername.setSelection(preferenceStore.getBoolean(USE_DEFAULT_USERNAME));

		final Composite group = new Composite(composite, SWT.NONE);
		final GridData gridData = new GridData();
		gridData.widthHint = 284;
		group.setLayoutData(gridData);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 40;
		gridLayout.numColumns = 2;
		group.setLayout(gridLayout);

		final Label lblUsername = new Label(group, SWT.NONE);
		lblUsername.setText(Messages.getString("UsernamePreferences.Username")); //$NON-NLS-1$

		txtUsername = new Text(group, SWT.BORDER);
		txtUsername.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtUsername.setText(preferenceStore.getString(DEFAULT_USERNAME));

		btnQuerySetUsername = new Button(composite, SWT.CHECK);
		btnQuerySetUsername.setText(Messages.getString("UsernamePreferences.AskForUserName")); //$NON-NLS-1$
		btnQuerySetUsername.setSelection(preferenceStore.getBoolean(QUERY_FOR_DEFAULT_USERNAME));
		btnQuerySetUsername.setEnabled(false);

		// Create three text fields.
		// Set the text in each from the preference store
		return composite;
	}

	/**
	 * Called when user clicks Restore Defaults
	 */
	@Override
	protected void performDefaults() {
		// Get the preference store
		final IPreferenceStore preferenceStore = getPreferenceStore();

		// Reset the fields to the defaults
		btnUseDefaultUsername.setSelection(preferenceStore.getDefaultBoolean(USE_DEFAULT_USERNAME));
		txtUsername.setText(preferenceStore.getDefaultString(DEFAULT_USERNAME));
		btnQuerySetUsername.setSelection(preferenceStore
				.getDefaultBoolean(QUERY_FOR_DEFAULT_USERNAME));

	}

	/**
	 * Called when user clicks Apply or OK
	 * 
	 * @return boolean
	 */
	@Override
	public boolean performOk() {
		// Get the preference store
		final IPreferenceStore preferenceStore = getPreferenceStore();

		// Set the values from the fields
		preferenceStore.setValue(USE_DEFAULT_USERNAME, btnUseDefaultUsername.getSelection());
		preferenceStore.setValue(DEFAULT_USERNAME, txtUsername.getText());
		preferenceStore.setValue(QUERY_FOR_DEFAULT_USERNAME, btnQuerySetUsername.getSelection());

		// Return true to allow dialog to close
		return true;
	}
}
