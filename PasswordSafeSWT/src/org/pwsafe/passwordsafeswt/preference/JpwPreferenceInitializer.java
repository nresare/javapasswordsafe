package org.pwsafe.passwordsafeswt.preference;

import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.ALWAYS_ON_TOP;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.CLEAR_CLIPBOARD_ON_MIN;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.CONFIRM_COPY_TO_CLIPBOARD;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.CONFIRM_ITEM_DELETION;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.CONFIRM_SAVE_ON_MIN;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.DEFAULT_OPEN_READ_ONLY;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.DEFAULT_PASSWORD_LENGTH;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.DEFAULT_USERNAME;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.DOUBLE_CLICK_COPIES_TO_CLIPBOARD;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.ESCAPE_KEY_EXITS_APP;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.HOT_KEY;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.HOT_KEY_ACTIVE;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.LOCK_DB_ON_MIN;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.LOCK_DB_ON_WS_LOCK;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.LOCK_ON_IDLE;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.LOCK_ON_IDLE_MINS;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.QUERY_FOR_DEFAULT_USERNAME;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.SAVE_IMMEDIATELY_ON_EDIT;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.SHOW_ICON_IN_SYSTEM_TRAY;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.SHOW_PASSWORD_IN_EDIT_MODE;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.SHOW_PASSWORD_IN_LIST;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.TABLE_COLUMN_SIZE;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.TREE_COLUMN_SIZE;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.USE_DEFAULT_USERNAME;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.USE_DIGITS;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.USE_EASY_TO_READ;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.USE_HEX_ONLY;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.USE_LOWERCASE_LETTERS;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.USE_SYMBOLS;
import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.USE_UPPERCASE_LETTERS;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.JFacePreferences;

/**
 * Initializer for default preference values.
 * 
 * @author roxon
 *
 */
public class JpwPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		// Use a scope etc. once we migrate to Eclipse RCP. 
		// IScopeContext theContext = new DefaultScope();
		
		// For now we simply use:
		IPreferenceStore theStore = JFacePreferences.getPreferenceStore();

		// Display
		theStore.setDefault(ALWAYS_ON_TOP, false);
		theStore.setDefault(SHOW_PASSWORD_IN_LIST, false);
		theStore.setDefault(SHOW_PASSWORD_IN_EDIT_MODE, false);
		theStore.setDefault(SHOW_ICON_IN_SYSTEM_TRAY, true);
		theStore.setDefault(TREE_COLUMN_SIZE, 150); 
		theStore.setDefault(TABLE_COLUMN_SIZE, 150);
		
	    // Password Policy
		theStore.setDefault(DEFAULT_PASSWORD_LENGTH, 8);
		theStore.setDefault(USE_LOWERCASE_LETTERS, true);
		theStore.setDefault(USE_UPPERCASE_LETTERS, true);
		theStore.setDefault(USE_DIGITS, true);
		theStore.setDefault(USE_SYMBOLS, false);
		theStore.setDefault(USE_EASY_TO_READ, false);
		theStore.setDefault(USE_HEX_ONLY, false);
		
		// User name
		theStore.setDefault(USE_DEFAULT_USERNAME, false);
		theStore.setDefault(DEFAULT_USERNAME, "");
		theStore.setDefault(QUERY_FOR_DEFAULT_USERNAME, false);
		
		// Security
		theStore.setDefault(CLEAR_CLIPBOARD_ON_MIN, false);
		theStore.setDefault(LOCK_DB_ON_MIN, false);
		theStore.setDefault(CONFIRM_SAVE_ON_MIN, false);
		theStore.setDefault(CONFIRM_COPY_TO_CLIPBOARD, false);
		theStore.setDefault(LOCK_DB_ON_WS_LOCK, true);
		theStore.setDefault(LOCK_ON_IDLE, true);
		theStore.setDefault(LOCK_ON_IDLE_MINS, 5);
		theStore.setDefault(CONFIRM_SAVE_ON_MIN, false);

		// Misc
		theStore.setDefault(CONFIRM_ITEM_DELETION, true);
		theStore.setDefault(SAVE_IMMEDIATELY_ON_EDIT, true);
		theStore.setDefault(ESCAPE_KEY_EXITS_APP, false);
		theStore.setDefault(HOT_KEY_ACTIVE, false);
		theStore.setDefault(HOT_KEY, false);
		theStore.setDefault(DOUBLE_CLICK_COPIES_TO_CLIPBOARD, true);
		theStore.setDefault(DEFAULT_OPEN_READ_ONLY, false);

	}

}
