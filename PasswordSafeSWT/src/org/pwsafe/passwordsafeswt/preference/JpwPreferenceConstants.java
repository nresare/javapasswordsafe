package org.pwsafe.passwordsafeswt.preference;

public interface JpwPreferenceConstants {
	
	// Display
	public static final String ALWAYS_ON_TOP = "display.always.on.top";
	public static final String SHOW_PASSWORD_IN_LIST = "show.password.in.list";
	public static final String SHOW_PASSWORD_IN_EDIT_MODE = "show.password.in.edit.mode";
	public static final String SHOW_ICON_IN_SYSTEM_TRAY = "show.icon.in.system.tray";
	public static final String TREE_COLUMN_SIZE = "tree.width.column";
	public static final String TABLE_COLUMN_SIZE = "table.width.column";

    // Password Policy
	public static final String DEFAULT_PASSWORD_LENGTH = "default.password.length";
	public static final String USE_LOWERCASE_LETTERS = "use.lowercase.letters";
	public static final String USE_UPPERCASE_LETTERS = "use.uppercase.letters";
	public static final String USE_DIGITS = "use.digits";
	public static final String USE_SYMBOLS = "use.symbols";
	public static final String USE_EASY_TO_READ = "use.easy.to.read";
	public static final String USE_HEX_ONLY = "use.hex.only";

	// User name
	public static final String USE_DEFAULT_USERNAME = "use.default.username";
	public static final String DEFAULT_USERNAME = "default.username";
	public static final String QUERY_FOR_DEFAULT_USERNAME = "query.for.default.username";

	// Security
	public static final String CLEAR_CLIPBOARD_ON_MIN = "clear.clipboard.on.minimize";
	public static final String LOCK_DB_ON_MIN = "lock.database.on.minimize";
	public static final String CONFIRM_SAVE_ON_MIN = "confirm.save.on.minimize";
	public static final String CONFIRM_COPY_TO_CLIPBOARD = "confirm.copy.to.clipboard";
	public static final String LOCK_DB_ON_WS_LOCK = "lock.database.on.workstation.lock";
	public static final String LOCK_ON_IDLE = "lock.db.on.idle";
	public static final String LOCK_ON_IDLE_MINS = "lock.db.on.idle.minutes";

	// Misc
	public static final String CONFIRM_ITEM_DELETION = "confirm.item.deletion";
	public static final String SAVE_IMMEDIATELY_ON_EDIT = "save.immediately.on.edit";
	public static final String ESCAPE_KEY_EXITS_APP = "escape.key.exits.app";
	public static final String HOT_KEY_ACTIVE = "hot.key.active";
	public static final String HOT_KEY = "hot.key.value";
	public static final String DOUBLE_CLICK_COPIES_TO_CLIPBOARD = "double.click.copies.to.clipboard";
	public static final String DEFAULT_OPEN_READ_ONLY = "default.open.readonly";
	public static final String RECORD_LAST_ACCESS_TIME = "record.last.access.time";


	// GUI Internal
	public static final String DISPLAY_AS_LIST_PREF = "display.as.list";
	
}
