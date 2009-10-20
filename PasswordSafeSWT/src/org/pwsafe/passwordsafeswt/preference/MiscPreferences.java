/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.preference;

import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.CONFIRM_ITEM_DELETION;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.DEFAULT_OPEN_READ_ONLY;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.DOUBLE_CLICK_COPIES_TO_CLIPBOARD;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.ESCAPE_KEY_EXITS_APP;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.HOT_KEY;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.HOT_KEY_ACTIVE;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.SAVE_IMMEDIATELY_ON_EDIT;

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
 * Miscellaneous preference items.
 *
 * @author Glen Smith
 */
public class MiscPreferences extends PreferencePage {
	  
	// Text fields for user to enter preferences
	private Button btnConfirmDeletion;
	private Button btnSaveImmediately;
	private Button btnOpenReadOnly;
	private Button btnEscapeExitsApp;
	private Button btnHotKey;
	private Text txtHotKey;
	private Button btnCopiesPasswordToClipboard;
	private Button btnViewsEntry;


	/**
	 * Creates the controls for this page
	 */
	@Override
	protected Control createContents(Composite parent) {
	    Composite composite = new Composite(parent, SWT.NONE);
	    composite.setLayout(new GridLayout());
	    
		// Get the preference store and setup defaults
	    IPreferenceStore preferenceStore = getPreferenceStore();

	    btnConfirmDeletion = new Button(composite, SWT.CHECK);
	    btnConfirmDeletion.setText(Messages.getString("MiscPreferences.ConfirmDelete")); //$NON-NLS-1$
	    btnConfirmDeletion.setSelection(preferenceStore.getBoolean(CONFIRM_ITEM_DELETION));

	    btnSaveImmediately = new Button(composite, SWT.CHECK);
	    btnSaveImmediately.setText(Messages.getString("MiscPreferences.SaveOnChange")); //$NON-NLS-1$
	    btnSaveImmediately.setSelection(preferenceStore.getBoolean(SAVE_IMMEDIATELY_ON_EDIT));

	    btnOpenReadOnly = new Button(composite, SWT.CHECK);
	    btnOpenReadOnly.setText(Messages.getString("MiscPreferences.DefaultOpenReadOnly")); //$NON-NLS-1$
	    btnOpenReadOnly.setSelection(preferenceStore.getBoolean(DEFAULT_OPEN_READ_ONLY));

	    btnEscapeExitsApp = new Button(composite, SWT.CHECK);
	    btnEscapeExitsApp.setText(Messages.getString("MiscPreferences.QuitOnEsc")); //$NON-NLS-1$
	    btnEscapeExitsApp.setSelection(preferenceStore.getBoolean(ESCAPE_KEY_EXITS_APP));
	    btnEscapeExitsApp.setEnabled(false);

	    final Composite compositeHotKey = new Composite(composite, SWT.NONE);
	    final GridLayout gridLayout = new GridLayout();
	    gridLayout.marginWidth = 0;
	    gridLayout.numColumns = 2;
	    gridLayout.marginHeight = 0;
	    compositeHotKey.setLayout(gridLayout);

	    btnHotKey = new Button(compositeHotKey, SWT.CHECK);
	    btnHotKey.setText(Messages.getString("MiscPreferences.HotKey")); //$NON-NLS-1$
	    btnHotKey.setSelection(preferenceStore.getBoolean(HOT_KEY_ACTIVE));
	    btnHotKey.setEnabled(false);

	    txtHotKey = new Text(compositeHotKey, SWT.BORDER);
	    txtHotKey.setText(preferenceStore.getString(HOT_KEY));
	    txtHotKey.setEnabled(false);

	    final Composite compositeDoubleClick = new Composite(composite, SWT.NONE);
	    final GridLayout gridLayout_1 = new GridLayout();
	    gridLayout_1.numColumns = 2;
	    compositeDoubleClick.setLayout(gridLayout_1);

	    final Label lblDoubleClick = new Label(compositeDoubleClick, SWT.NONE);
	    lblDoubleClick.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
	    lblDoubleClick.setText(Messages.getString("MiscPreferences.DoubleClickActionLabel")); //$NON-NLS-1$

	    final Composite compositeRadios = new Composite(compositeDoubleClick, SWT.NONE);
	    final GridLayout gridLayout_2 = new GridLayout();
	    gridLayout_2.marginWidth = 0;
	    gridLayout_2.marginHeight = 0;
	    compositeRadios.setLayout(gridLayout_2);

	    btnCopiesPasswordToClipboard = new Button(compositeRadios, SWT.RADIO);
	    btnCopiesPasswordToClipboard.setText(Messages.getString("MiscPreferences.CopyPasswordOnDoubleClick")); //$NON-NLS-1$

	    btnViewsEntry = new Button(compositeRadios, SWT.RADIO);
	    btnViewsEntry.setText(Messages.getString("MiscPreferences.ViewEntryOnDoubleClick")); //$NON-NLS-1$
	    
	    if (preferenceStore.getBoolean(DOUBLE_CLICK_COPIES_TO_CLIPBOARD)) {
	    	btnCopiesPasswordToClipboard.setSelection(true);
	    } else {
	    	btnViewsEntry.setSelection(true);
	    }
	    
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
	    btnConfirmDeletion.setSelection(preferenceStore.getDefaultBoolean(CONFIRM_ITEM_DELETION));
	    btnSaveImmediately.setSelection(preferenceStore.getDefaultBoolean(SAVE_IMMEDIATELY_ON_EDIT));
	    btnOpenReadOnly.setSelection(preferenceStore.getDefaultBoolean(DEFAULT_OPEN_READ_ONLY));
	    btnEscapeExitsApp.setSelection(preferenceStore.getDefaultBoolean(ESCAPE_KEY_EXITS_APP));
	    btnHotKey.setSelection(preferenceStore.getDefaultBoolean(HOT_KEY_ACTIVE));
	    txtHotKey.setText(preferenceStore.getDefaultString(HOT_KEY));
	    if (preferenceStore.getDefaultBoolean(DOUBLE_CLICK_COPIES_TO_CLIPBOARD)) {
	    	btnCopiesPasswordToClipboard.setSelection(true);
	    } else {
	    	btnViewsEntry.setSelection(true);
	    }
	}

	/**
	 * Called when user clicks Apply or OK
	 * 
	 * @return boolean
	 */
	@Override
	public boolean performOk() {
	    // Get the preference store
	    IPreferenceStore preferenceStore = getPreferenceStore();

	    // Set the values from the fields
	    preferenceStore.setValue(CONFIRM_ITEM_DELETION, btnConfirmDeletion.getSelection());
   		preferenceStore.setValue(SAVE_IMMEDIATELY_ON_EDIT,btnSaveImmediately.getSelection());
   		preferenceStore.setValue(DEFAULT_OPEN_READ_ONLY,btnOpenReadOnly.getSelection());
   		preferenceStore.setValue(ESCAPE_KEY_EXITS_APP,btnEscapeExitsApp.getSelection());
		preferenceStore.setValue(HOT_KEY_ACTIVE,btnHotKey.getSelection());
		preferenceStore.setValue(HOT_KEY,txtHotKey.getText());
		preferenceStore.setValue(DOUBLE_CLICK_COPIES_TO_CLIPBOARD, btnCopiesPasswordToClipboard.getSelection());
	    
	    // Return true to allow dialog to close
	    return true;
	}
}
