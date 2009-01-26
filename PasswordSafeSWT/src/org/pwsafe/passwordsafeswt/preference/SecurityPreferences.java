/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.preference;

import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.CLEAR_CLIPBOARD_ON_MIN;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.CONFIRM_COPY_TO_CLIPBOARD;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.CONFIRM_SAVE_ON_MIN;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.LOCK_DB_ON_MIN;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.LOCK_DB_ON_WS_LOCK;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.LOCK_ON_IDLE;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.LOCK_ON_IDLE_MINS;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/**
 * Preferences related to locking terminal and other security stuff.
 *
 * @author Glen Smith
 */
public class SecurityPreferences extends PreferencePage {

	  // Text fields for user to enter preferences
	  private Spinner spiMinutesIdle;

	  Button btnClearClipboard;
	  Button btnLockDatabaseOnMin;
	  Button btnConfirmSaveOnMinimize;
	  Button btnConfirmCopy;
	  Button btnLockDatabaseOnWorkstationLock;
	  Button btnLockOnIdle;
	  
	  

	  /**
	   * Creates the controls for this page
	   */
	  protected Control createContents(Composite parent) {
	    Composite composite = new Composite(parent, SWT.NONE);
	    composite.setLayout(new GridLayout());
	    
	    // Get the preference store
	    IPreferenceStore preferenceStore = getPreferenceStore();

	    btnClearClipboard = new Button(composite, SWT.CHECK);
	    btnClearClipboard.setText("Clear clipboard upon minimize or exit");
	    btnClearClipboard.setSelection(preferenceStore.getBoolean(CLEAR_CLIPBOARD_ON_MIN));
	    
	    btnLockDatabaseOnMin = new Button(composite, SWT.CHECK);
	    btnLockDatabaseOnMin.setText("Lock password database on minimize");
	    btnLockDatabaseOnMin.setSelection(preferenceStore.getBoolean(LOCK_DB_ON_MIN));

	    btnConfirmSaveOnMinimize = new Button(composite, SWT.CHECK);
	    btnConfirmSaveOnMinimize.setEnabled(false);
	    btnConfirmSaveOnMinimize.setText("Confirm password database save on minimize");
	    btnConfirmSaveOnMinimize.setSelection(preferenceStore.getBoolean(CONFIRM_SAVE_ON_MIN));
	    btnConfirmSaveOnMinimize.setEnabled(false);

	    btnConfirmCopy = new Button(composite, SWT.CHECK);
	    btnConfirmCopy.setText("Confirm item copy to clipboard");
	    btnConfirmCopy.setSelection(preferenceStore.getBoolean(CONFIRM_COPY_TO_CLIPBOARD));
	    btnConfirmCopy.setEnabled(false);

	    btnLockDatabaseOnWorkstationLock = new Button(composite, SWT.CHECK);
	    btnLockDatabaseOnWorkstationLock.setText("Lock password database on workstation lock");
	    btnLockDatabaseOnWorkstationLock.setSelection(preferenceStore.getBoolean(LOCK_DB_ON_WS_LOCK));
	    btnLockDatabaseOnWorkstationLock.setEnabled(false);

	    final Composite composite_1 = new Composite(composite, SWT.NONE);
	    final GridLayout gridLayout = new GridLayout();
	    gridLayout.marginWidth = 0;
	    gridLayout.marginHeight = 0;
	    gridLayout.numColumns = 3;
	    composite_1.setLayout(gridLayout);

	    btnLockOnIdle = new Button(composite_1, SWT.CHECK);
	    btnLockOnIdle.setText("Lock password database after");
	    btnLockOnIdle.setSelection(preferenceStore.getBoolean(LOCK_ON_IDLE));

	    spiMinutesIdle = new Spinner(composite_1, SWT.BORDER);
	    spiMinutesIdle.setSelection(preferenceStore.getInt(LOCK_ON_IDLE_MINS));

	    final Label lblMinsIdle = new Label(composite_1, SWT.NONE);
	    lblMinsIdle.setText("minutes idle");

	    return composite;
	  }

	  /**
	   * Called when user clicks Restore Defaults
	   */
	  protected void performDefaults() {
	    // Get the preference store
	    IPreferenceStore preferenceStore = getPreferenceStore();

	    // Reset the fields to the defaults
	    btnClearClipboard.setSelection(preferenceStore.getDefaultBoolean(CLEAR_CLIPBOARD_ON_MIN));
	    btnLockDatabaseOnMin.setSelection(preferenceStore.getDefaultBoolean(LOCK_DB_ON_MIN));
	    btnConfirmSaveOnMinimize.setSelection(preferenceStore.getDefaultBoolean(CONFIRM_SAVE_ON_MIN));
	    btnConfirmCopy.setSelection(preferenceStore.getDefaultBoolean(CONFIRM_COPY_TO_CLIPBOARD));
	    btnLockDatabaseOnWorkstationLock.setSelection(preferenceStore.getDefaultBoolean(LOCK_DB_ON_WS_LOCK));
	    btnLockOnIdle.setSelection(preferenceStore.getDefaultBoolean(LOCK_ON_IDLE));
	    spiMinutesIdle.setData(preferenceStore.getDefaultInt(LOCK_ON_IDLE_MINS));
	        
	  }

	  /**
	   * Called when user clicks Apply or OK
	   * 
	   * @return boolean
	   */
	  public boolean performOk() {
	    // Get the preference store
	    IPreferenceStore preferenceStore = getPreferenceStore();

	    // Set the values from the fields
    	preferenceStore.setValue(CLEAR_CLIPBOARD_ON_MIN, btnClearClipboard.getSelection());
    	preferenceStore.setValue(LOCK_DB_ON_MIN, btnLockDatabaseOnMin.getSelection());
    	preferenceStore.setValue(CONFIRM_SAVE_ON_MIN, btnConfirmSaveOnMinimize.getSelection());
    	preferenceStore.setValue(CONFIRM_COPY_TO_CLIPBOARD, btnConfirmCopy.getSelection());
    	preferenceStore.setValue(LOCK_DB_ON_WS_LOCK, btnLockDatabaseOnWorkstationLock.getSelection());
    	preferenceStore.setValue(LOCK_ON_IDLE, btnLockOnIdle.getSelection());
    	preferenceStore.setValue(LOCK_ON_IDLE_MINS, spiMinutesIdle.getSelection());

	    // Return true to allow dialog to close
	    return true;
	  }
}
