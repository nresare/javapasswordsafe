/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt;

import static org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants.DISPLAY_AS_LIST_PREF;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.lib.datastore.PwsEntryStore;
import org.pwsafe.lib.exception.PasswordSafeException;
import org.pwsafe.lib.file.PwsFile;
import org.pwsafe.lib.file.PwsFileFactory;
import org.pwsafe.lib.file.PwsFileStorage;
import org.pwsafe.lib.file.PwsRecord;
import org.pwsafe.lib.file.PwsRecordV1;
import org.pwsafe.lib.file.PwsRecordV2;
import org.pwsafe.lib.file.PwsRecordV3;
import org.pwsafe.lib.file.PwsTimeField;
import org.pwsafe.passwordsafeswt.action.AboutAction;
import org.pwsafe.passwordsafeswt.action.AddRecordAction;
import org.pwsafe.passwordsafeswt.action.ChangeSafeCombinationAction;
import org.pwsafe.passwordsafeswt.action.ClearClipboardAction;
import org.pwsafe.passwordsafeswt.action.CopyPasswordAction;
import org.pwsafe.passwordsafeswt.action.CopyUsernameAction;
import org.pwsafe.passwordsafeswt.action.DeleteRecordAction;
import org.pwsafe.passwordsafeswt.action.EditRecordAction;
import org.pwsafe.passwordsafeswt.action.ExitAppAction;
import org.pwsafe.passwordsafeswt.action.ExportToTextAction;
import org.pwsafe.passwordsafeswt.action.ExportToXMLAction;
import org.pwsafe.passwordsafeswt.action.HelpAction;
import org.pwsafe.passwordsafeswt.action.ImportFromTextAction;
import org.pwsafe.passwordsafeswt.action.ImportFromXMLAction;
import org.pwsafe.passwordsafeswt.action.LockDbAction;
import org.pwsafe.passwordsafeswt.action.MRUFileAction;
import org.pwsafe.passwordsafeswt.action.NewFileAction;
import org.pwsafe.passwordsafeswt.action.OpenFileAction;
import org.pwsafe.passwordsafeswt.action.OptionsAction;
import org.pwsafe.passwordsafeswt.action.SaveFileAction;
import org.pwsafe.passwordsafeswt.action.SaveFileAsAction;
import org.pwsafe.passwordsafeswt.action.ViewAsListAction;
import org.pwsafe.passwordsafeswt.action.ViewAsTreeAction;
import org.pwsafe.passwordsafeswt.action.VisitPasswordSafeWebsiteAction;
import org.pwsafe.passwordsafeswt.dialog.StartupDialog;
import org.pwsafe.passwordsafeswt.dto.PwsEntryDTO;
import org.pwsafe.passwordsafeswt.listener.TableColumnSelectionAdaptor;
import org.pwsafe.passwordsafeswt.listener.ViewerDoubleClickListener;
import org.pwsafe.passwordsafeswt.model.PasswordTableContentProvider;
import org.pwsafe.passwordsafeswt.model.PasswordTableLabelProvider;
import org.pwsafe.passwordsafeswt.model.PasswordTableSorter;
import org.pwsafe.passwordsafeswt.model.PasswordTreeContentProvider;
import org.pwsafe.passwordsafeswt.model.PasswordTreeLabelProvider;
import org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants;
import org.pwsafe.passwordsafeswt.preference.WidgetPreferences;
import org.pwsafe.passwordsafeswt.util.IOUtils;
import org.pwsafe.passwordsafeswt.util.UserPreferences;
import org.pwsafe.passwordsafeswt.xml.XMLDataParser;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.swtdesigner.SWTResourceManager;

/**
 * A port of PasswordSafe to JFace. This is the main class that is launched from
 * the commandline.
 * 
 * @author Glen Smith
 */
public class PasswordSafeJFace extends ApplicationWindow {

	private ImportFromXMLAction importFromXMLAction;
	private ExportToXMLAction exportToXMLAction;
	protected OptionsAction optionsAction;
	private TreeViewer treeViewer;
	private Tree tree;
	private HelpAction helpAction;
	protected AboutAction aboutAction;
	private ChangeSafeCombinationAction changeSafeCombinationAction;
	private VisitPasswordSafeWebsiteAction visitPasswordSafeWebsiteAction;
	protected ExitAppAction exitAppAction;
	private TableViewer tableViewer;
	private NewFileAction newFileAction;
	private DeleteRecordAction deleteRecordAction;
	private EditRecordAction editRecordAction;
	private AddRecordAction addRecordAction;
	private ClearClipboardAction clearClipboardAction;
	private CopyPasswordAction copyPasswordAction;
	private CopyUsernameAction copyUsernameAction;
	private SaveFileAsAction saveFileAsAction;
	private SaveFileAction saveFileAction;
	private OpenFileAction openFileAction;
	private ViewAsListAction viewAsListAction;
	private ViewAsTreeAction viewAsTreeAction;
	private ExportToTextAction exportToTextAction;
	private ImportFromTextAction importFromTextAction;
	private LockDbAction lockDbAction;
	private Table table;
	private TrayItem trayItem;
	private StackLayout stackLayout;
	private Composite composite;
	private boolean locked = false;
	private final Timer lockTimer = new Timer("SWTPassword lock timer", true); //$NON-NLS-1$
	private TimerTask lockTask;

	protected static final Log log = LogFactory.getLog(PasswordSafeJFace.class);

	public static final String APP_NAME = "PasswordSafeSWT"; //$NON-NLS-1$

	private static PasswordSafeJFace app;

	private PwsFile pwsFile;
	private PwsEntryStore dataStore;
		
	private static final String V1_GROUP_PLACEHOLDER = Messages.getString("PasswordSafeJFace.V1GroupPlaceholder"); //$NON-NLS-1$

	/**
	 * Constructor.
	 */
	public PasswordSafeJFace() {
		super(null);
		app = this;
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
		setEditMenusEnabled(false);
	}

	private void displayOpeningDialog() {
		StartupDialog sd = new StartupDialog(getShell(), UserPreferences.getInstance().getMRUFiles());
		boolean allDone = false;

		while (!allDone) {
			String result = (String) sd.open();
			if (result == StartupDialog.CANCEL || result == null) {
				// they cancelled or clicked the close button on the dialog
				exitApplication();
				return;
			} else if (result == StartupDialog.OPEN_FILE) {
				try {
					openFile(sd.getFilename(), sd.getPassword());
					allDone = true;
				} catch (Exception e) {
					displayErrorDialog(Messages.getString("PasswordSafeJFace.OpenError.Title"), Messages.getString("PasswordSafeJFace.OpenError.Message"), e); //$NON-NLS-1$ //$NON-NLS-2$
					allDone = false;
				}
			} else if (result == StartupDialog.NEW_FILE) {
				newFileAction.run();
                if (getPwsFile() != null)
                    allDone = true;
			} else if (result == StartupDialog.OPEN_OTHER) {
				openFileAction.run();
				allDone = true;
			}
		}
		setupStatusMessage();
	}

	private void startOpeningDialogThread(final Shell shell) {

		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {
				displayOpeningDialog();
			}
		});
	}
	/**
	 * Returns a singleton handle to the app for making business logic calls.
	 * 
	 * @return a handle to a singleton of this class
	 */
	public static PasswordSafeJFace getApp() {
		return app;
	}

	/**
	 * On platforms where a tray is supported, installs the event handlers for
	 * showing and hiding the tray.
	 */
	private void setupTrayItem() {
		
		IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
		
		if (! thePrefs.getBoolean(JpwPreferenceConstants.SHOW_ICON_IN_SYSTEM_TRAY)) {
			return;
		}
		
		Image image = SWTResourceManager.getImage(PasswordSafeJFace.class,
				"/org/pwsafe/passwordsafeswt/images/cpane.ico"); //$NON-NLS-1$
		
		final Tray tray = getShell().getDisplay().getSystemTray();
		if (tray == null) {
			if (log.isInfoEnabled())
				log.info("The system tray is not available"); 
		} else {
			if (log.isDebugEnabled())
				log.debug("Setting up System Tray"); 

			trayItem = new TrayItem(tray, SWT.NONE);
			trayItem.setToolTipText(PasswordSafeJFace.APP_NAME);

			trayItem.addListener(SWT.DefaultSelection, new Listener() {
				public void handleEvent(Event event) {
					getShell().setVisible(true);
					getShell().setMinimized(false);
				}
			});
			final Menu menu = new Menu(getShell(), SWT.POP_UP);
			MenuItem trayRestore = new MenuItem(menu, SWT.PUSH);
			trayRestore.setText(Messages.getString("PasswordSafeJFace.Tray.RestoreLabel")); //$NON-NLS-1$
			trayRestore.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					getShell().setVisible(true);
					getShell().setMinimized(false);
				}
			});
			new MenuItem(menu, SWT.SEPARATOR);
			MenuItem trayExit = new MenuItem(menu, SWT.PUSH);
			trayExit.setText(Messages.getString("PasswordSafeJFace.Tray.ExitLabel")); //$NON-NLS-1$
			trayExit.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					new ExitAppAction().run();
				}
			});
			trayItem.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event event) {
					menu.setVisible(true);
				}
			});
			
			trayItem.setImage(image);
			getShell().addShellListener(new ShellAdapter() {
				@Override
				public void shellIconified(ShellEvent e) {
					final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
					if (trayItem != null
							&& thePrefs.getBoolean(JpwPreferenceConstants.SHOW_ICON_IN_SYSTEM_TRAY)) {
						if (log.isDebugEnabled())
							log.debug("Shrinking to tray"); 
						getShell().setVisible(false);
					}
				}

			});
		}
	}

	/**
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.VERTICAL));

		composite = new Composite(container, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
		if (thePrefs.getBoolean(DISPLAY_AS_LIST_PREF)) {
			addTableView(composite);
			viewAsListAction.setChecked(true);
			viewAsTreeAction.setChecked(false);
		} else {
			addTreeView(composite);
			viewAsTreeAction.setChecked(true);
			viewAsListAction.setChecked(false);
		}


		setupTrayItem();

		return container;
	}

	/**
	 * Creates the JFace actions that will be used for menus and toolbars.
	 */
	private void createActions() {

		newFileAction = new NewFileAction();
		openFileAction = new OpenFileAction();
		saveFileAction = new SaveFileAction();
		saveFileAsAction = new SaveFileAsAction();
		exportToTextAction = new ExportToTextAction();
		importFromTextAction = new ImportFromTextAction();
		exitAppAction = new ExitAppAction();
		copyUsernameAction = new CopyUsernameAction();
		copyPasswordAction = new CopyPasswordAction();
		clearClipboardAction = new ClearClipboardAction();
		addRecordAction = new AddRecordAction();
		editRecordAction = new EditRecordAction();
		deleteRecordAction = new DeleteRecordAction();
		viewAsListAction = new ViewAsListAction();
		viewAsTreeAction = new ViewAsTreeAction();
		visitPasswordSafeWebsiteAction = new VisitPasswordSafeWebsiteAction();
		changeSafeCombinationAction = new ChangeSafeCombinationAction();
		aboutAction = new AboutAction();
		helpAction = new HelpAction();
		optionsAction = new OptionsAction();
		exportToXMLAction = new ExportToXMLAction();
		importFromXMLAction = new ImportFromXMLAction();
		lockDbAction = new LockDbAction();

	}

	/**
	 * @see org.eclipse.jface.window.ApplicationWindow#createMenuManager()
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager result = new MenuManager(Messages.getString("PasswordSafeJFace.Menu.Menu")); //$NON-NLS-1$

		final MenuManager menuManagerFile = new MenuManager(Messages.getString("PasswordSafeJFace.Menu.File")); //$NON-NLS-1$
		result.add(menuManagerFile);

		menuManagerFile.add(newFileAction);
		menuManagerFile.add(openFileAction);
		menuManagerFile.add(new Separator());

		String[] mruFiles = UserPreferences.getInstance().getMRUFiles();
		if (mruFiles != null && mruFiles.length > 0) {
			for (int i = 0; i < mruFiles.length; i++) {
				String fileName = mruFiles[i];
				String menuItem = "&" + (i + 1) + " " + new File(fileName).getName(); //$NON-NLS-1$ //$NON-NLS-2$
				IAction nextMRUAction = new MRUFileAction(fileName, menuItem);
				menuManagerFile.add(nextMRUAction);
			}
			menuManagerFile.add(new Separator());
		}

		menuManagerFile.add(saveFileAction);
		menuManagerFile.add(saveFileAsAction);
		menuManagerFile.add(new Separator());

		final MenuManager menuManagerExportTo = new MenuManager(Messages.getString("PasswordSafeJFace.Menu.ExportTo")); //$NON-NLS-1$
		menuManagerFile.add(menuManagerExportTo);

		menuManagerExportTo.add(exportToTextAction);
		menuManagerExportTo.add(exportToXMLAction);

		final MenuManager menuManagerImportFrom = new MenuManager(Messages.getString("PasswordSafeJFace.Menu.ImportFrom")); //$NON-NLS-1$
		menuManagerFile.add(menuManagerImportFrom);

		menuManagerImportFrom.add(importFromTextAction);
		menuManagerImportFrom.add(importFromXMLAction);

		menuManagerFile.add(new Separator());
		menuManagerFile.add(exitAppAction);

		final MenuManager menuManagerEdit = new MenuManager(Messages.getString("PasswordSafeJFace.Menu.Edit")); //$NON-NLS-1$
		result.add(menuManagerEdit);

		menuManagerEdit.add(addRecordAction);
		menuManagerEdit.add(editRecordAction);
		menuManagerEdit.add(deleteRecordAction);
		menuManagerEdit.add(new Separator());
		menuManagerEdit.add(copyPasswordAction);
		menuManagerEdit.add(copyUsernameAction);
		menuManagerEdit.add(clearClipboardAction);

		final MenuManager menuManagerView = new MenuManager(Messages.getString("PasswordSafeJFace.Menu.View")); //$NON-NLS-1$
		result.add(menuManagerView);

		menuManagerView.add(viewAsListAction);
		menuManagerView.add(viewAsTreeAction);

		final MenuManager menuManagerManage = new MenuManager(Messages.getString("PasswordSafeJFace.Menu.Manage")); //$NON-NLS-1$
		result.add(menuManagerManage);

		menuManagerManage.add(changeSafeCombinationAction);
		menuManagerManage.add(new Separator());
		menuManagerManage.add(optionsAction);

		final MenuManager menuManagerHelp = new MenuManager(Messages.getString("PasswordSafeJFace.Menu.Help")); //$NON-NLS-1$
		result.add(menuManagerHelp);

		menuManagerHelp.add(helpAction);
		menuManagerHelp.add(new Separator());
		menuManagerHelp.add(visitPasswordSafeWebsiteAction);
		menuManagerHelp.add(new Separator());
		menuManagerHelp.add(aboutAction);

		return result;
	}

	/**
	 * Creates the popup (right click) menu for the given control.
	 * 
	 * @param ctl
	 *            the control to add the popup to
	 * @return the created menu
	 */
	protected Menu createPopupMenu(Control ctl) {

		final MenuManager menuListPopup = new MenuManager(Messages.getString("PasswordSafeJFace.Menu.ListPopup")); //$NON-NLS-1$

		menuListPopup.createContextMenu(ctl);

		menuListPopup.add(copyPasswordAction);
		menuListPopup.add(copyUsernameAction);
		menuListPopup.add(new Separator());
		menuListPopup.add(addRecordAction);
		menuListPopup.add(editRecordAction);
		menuListPopup.add(deleteRecordAction);

		return menuListPopup.getMenu();
	}

	/**
	 * @see org.eclipse.jface.window.ApplicationWindow#createToolBarManager(int)
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);

		toolBarManager.add(newFileAction);
		toolBarManager.add(openFileAction);
		toolBarManager.add(saveFileAction);
		toolBarManager.add(new Separator());
		toolBarManager.add(copyUsernameAction);
		toolBarManager.add(copyPasswordAction);
		toolBarManager.add(clearClipboardAction);
		toolBarManager.add(new Separator());
		toolBarManager.add(addRecordAction);
		toolBarManager.add(editRecordAction);
		toolBarManager.add(deleteRecordAction);
		toolBarManager.add(new Separator());
		toolBarManager.add(helpAction);
		return toolBarManager;
	}

	/**
	 * @see org.eclipse.jface.window.ApplicationWindow#createStatusLineManager()
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		statusLineManager.setMessage("http://jpwsafe.sf.net"); //$NON-NLS-1$
		return statusLineManager;
	}

	public void setupStatusMessage() {

		final PwsFile pwsf = getPwsFile();
        if (pwsf != null && pwsf.getRecordCount() > 0) {
        	final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
			if (thePrefs.getBoolean(JpwPreferenceConstants.DOUBLE_CLICK_COPIES_TO_CLIPBOARD)) {
				setStatus(Messages.getString("PasswordSafeJFace.Status.DoubleClickToCopy")); //$NON-NLS-1$
			} else {
				setStatus(Messages.getString("PasswordSafeJFace.Status.DoubleClickToEdit")); //$NON-NLS-1$
			}
		} else {
			setStatus("http://jpwsafe.sf.net"); //$NON-NLS-1$
		}

	}

	/**
	 * Main program entry point.
	 * 
	 * @param args
	 *            commandline args that are passed in
	 */
	public static void main(String args[]) {
		log.info("PasswordSafe starting..."); 
		log.info("java.library.path is: [" + System.getProperty("java.library.path") + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		log.info("log: " + log.getClass().getName()); //$NON-NLS-1$
		try {
			PasswordSafeJFace window = new PasswordSafeJFace();
			window.setBlockOnOpen(true);
			window.open();
			SWTResourceManager.dispose();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			log.error("Error Starting PasswordSafe", e); 
		}
		log.info("PasswordSafe terminating..."); 
	}

	/**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(PasswordSafeJFace.APP_NAME);
		newShell.setImage(SWTResourceManager.getImage(PasswordSafeJFace.class,
				"/org/pwsafe/passwordsafeswt/images/cpane.ico")); //$NON-NLS-1$
		WidgetPreferences.tuneShell(newShell, getClass());
		startOpeningDialogThread(newShell);
	}

	/**
	 * @see org.eclipse.jface.window.Window#getInitialSize()
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(425, 375);
	}

	/**
	 * Create a brand new empty safe.
	 * 
	 * @param password the password for the new safe
	 */
	public void newFile(String password) {
		getShell().setText(PasswordSafeJFace.APP_NAME + Messages.getString("PasswordSafeJFace.AppTitle.Default")); //$NON-NLS-1$
		PwsFile newFile = PwsFileFactory.newFile();
		newFile.setPassphrase(password);
		this.setPwsFile(newFile);
	}

	/**
	 * Opens a new safe from the filesystem.
	 * 
	 * @throws Exception
	 *             if bad things happen during open
	 */
	public void openFile(String fileName, String password) throws Exception {

		PwsFile file = PwsFileFactory.loadFile(fileName, password);
		getShell().setText(PasswordSafeJFace.APP_NAME + " - " + fileName); //$NON-NLS-1$
		setPwsFile(file);
		if (true) // TODO (!openedFromMRU)
			UserPreferences.getInstance().setMostRecentFilename(fileName);
	}

	/**
	 * Save the current safe.
	 * 
	 * @throws IOException
	 *             if bad things happen during save
	 * @throws NoSuchAlgorithmException 
	 *             if SHA-1 implementation not found
	 */
	public void saveFile() throws IOException, NoSuchAlgorithmException {
		PwsFile file = getPwsFile();
		if (file.getStorage() == null) {
			saveFileAsAction.run();
		} else {
			file.save();
		}
	}

	/**
	 * Saves the current safe under a new filename.
	 * 
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 *             if SHA-1 implementation not found
	 */
	public void saveFileAs(String newFilename) throws IOException, NoSuchAlgorithmException {
		PwsFileStorage s = new PwsFileStorage(newFilename);
		getPwsFile().setStorage(s);
		getPwsFile().save();
		getShell().setText(PasswordSafeJFace.APP_NAME + " - " + newFilename); //$NON-NLS-1$
		UserPreferences.getInstance().setMostRecentFilename(newFilename);
	}

	/**
	 * Copy field to clipboard.
	 * 
	 * @param cb
	 *            handle to the clipboard
	 * @param recordToCopy
	 *            the pwsafe record to copy
	 * @param fieldToCopy
	 *            the field in that record to copy
	 */
	public void copyToClipboard(Clipboard cb, PwsRecord recordToCopy, int fieldToCopy) {

		if (recordToCopy != null) {

			String valueToCopy = null;

			valueToCopy = recordToCopy.getField(fieldToCopy).getValue().toString();

			//set access date
			if (recordToCopy instanceof PwsRecordV3)
				recordToCopy.setField(new PwsTimeField(PwsRecordV3.LAST_ACCESS_TIME, new Date()));
			 			
			if (valueToCopy != null) {
				cb.setContents(new Object[]{valueToCopy}, new Transfer[]{TextTransfer.getInstance()});
				log.debug("Copied to clipboard"); 
			}
		}

	}

	/**
	 * Locates the selected record in the underlying tree or table control.
	 * 
	 * @return the selected record
	 */
	public PwsEntryBean getSelectedRecord() {

		PwsEntryBean recordToCopy = null;

		if (isTreeViewShowing()) {
			if (tree.getSelectionCount() == 1) {
				TreeItem ti = tree.getSelection()[0];
				Object treeData = ti.getData();
				if (treeData != null && treeData instanceof PwsEntryBean) { // must be a left, not a group entry
					recordToCopy = (PwsEntryBean) treeData;
				}					
			}
		} else {
			if (table.getSelectionCount() == 1) {
				TableItem ti = table.getSelection()[0];
				recordToCopy = (PwsEntryBean) ti.getData();
			}
		}

		return recordToCopy;
	}

	/**
	 * Edits the selected record in a new dialog.
	 */
	public void editRecord(PwsEntryBean newEntry) {
		if (log.isDebugEnabled())
			log.debug("Dialog has been edited, updating safe"); 		
		getPwsDataStore().updateEntry(newEntry);
		updateViewers();
	}

	/**
	 * Adds a new record to the safe in a new dialog.
	 */
	public void addRecord(PwsEntryBean newEntry) {
		if (log.isDebugEnabled())
			log.debug("Dialog has created new record, updating safe"); 
		try {
			getPwsDataStore().addEntry(newEntry);
			saveOnUpdateOrEditCheck();
		} catch (PasswordSafeException e) {
			displayErrorDialog(Messages.getString("PasswordSafeJFace.AddEntryError.Title"), Messages.getString("PasswordSafeJFace.AddEntryError.Message"), e); //$NON-NLS-1$ //$NON-NLS-2$
		}
		updateViewers();

	}

	/**
	 * If the user has set "Save on Update or Edit", we save the file
	 * immediately.
	 * 
	 */
	private void saveOnUpdateOrEditCheck() {
		final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();		
		if (thePrefs.getBoolean(JpwPreferenceConstants.SAVE_IMMEDIATELY_ON_EDIT)) {
			if (log.isDebugEnabled())
				log.debug("Save on Edit option active. Saving database."); 
			saveFileAction.run();
		}
	}

    /**
     * Prompts the user to save the current file, if it has been modified.
     * @return <code>true</code> if the user cancels the action which triggered the
     * call to this method, <code>false</code> if the save was successful or ignored.
     */
    public boolean saveAppIfDirty() {
        boolean cancelled = false;
        if (isDirty()) {
            int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.CANCEL;
            MessageBox messageBox = new MessageBox(getShell(), style);
            messageBox.setText(Messages.getString("PasswordSafeJFace.SaveChanges.Title")); //$NON-NLS-1$
            messageBox
                    .setMessage(Messages.getString("PasswordSafeJFace.SaveChanges.Message")); //$NON-NLS-1$
            int result = messageBox.open();
            if (result == SWT.YES) {
                try {
                    saveFile();
                } catch (IOException e1) {
                    displayErrorDialog(Messages.getString("PasswordSafeJFace.SaveSafeError.Title"), e1.getMessage(), e1); //$NON-NLS-1$
                    cancelled = true;
                } catch (NoSuchAlgorithmException e) {
                    displayErrorDialog(Messages.getString("PasswordSafeJFace.SaveSafeError.Title"), e.getMessage(), e); //$NON-NLS-1$
                    cancelled = true;
				}
            } else if (result == SWT.CANCEL) {
                cancelled = true;
            }
        }
        return cancelled;
    }

	/**
	 * Deletes the selected record in the tree or table.
	 * 
	 */
	public void deleteRecord() {
		PwsEntryBean selectedRec = getSelectedRecord();
		getPwsDataStore().removeEntry(selectedRec);
		saveOnUpdateOrEditCheck();
		updateViewers();
	}

	/**
	 * Has the current safe been modified.
	 * 
	 * @return true if the safe has been modified
	 */
	public boolean isDirty() {
		if (getPwsFile() != null) {
			return getPwsFile().isModified();
		}
		return false;
	}

	/**
	 * Is the Application in locked mode.
	 * 
	 * @return true if the safe has been modified
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Sets the Application in locked mode.
	 * 
	 * @return true if the safe has been modified
	 */
	public void setLocked(boolean isLocked) {
		locked = isLocked;
	}


	/**
	 * Returns the currently loaded pwsafe file.
	 * 
	 * @return Returns the pwsFile.
	 */
	public PwsFile getPwsFile() {
		return pwsFile;
	}
	
	/**
	 * Returns the currently loaded pwsafe file.
	 * 
	 * @return Returns the pwsFile.
	 */
	public PwsEntryStore getPwsDataStore() {
		return dataStore;
	}

	/**
	 * Sets the currently load pwsafe file.
	 * 
	 * @param pwsFile
	 *            The pwsFile to set.
	 */
	public void setPwsFile(PwsFile pwsFile) {
		this.pwsFile = pwsFile;
		this.dataStore = PwsFileFactory.getStore(pwsFile);
		updateViewers();
		setEditMenusEnabled(true);
	}

	/**
	 * Perform necessary shutdown operations, regardless of how the user
	 * exited the application.
	 *
	 */
	private void tidyUpOnExit() {
		final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();

		if (thePrefs.getBoolean(JpwPreferenceConstants.CLEAR_CLIPBOARD_ON_MIN)) {
			clearClipboardAction.run();
		}
		thePrefs.setValue(DISPLAY_AS_LIST_PREF, ! isTreeViewShowing());
		try {
			UserPreferences.getInstance().savePreferences();
		} catch (IOException e) {
			displayErrorDialog(Messages.getString("PasswordSafeJFace.SavePrefsError.Title"), Messages.getString("PasswordSafeJFace.SavePrefsError.Message") + e.getMessage(), e); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * Exit the app.
	 * 
	 */
	public void exitApplication() {
		tidyUpOnExit();
		getShell().close();
		getShell().dispose();
		if (trayItem != null)
			trayItem.dispose();
	}

	/**
	 * Set the list view as the topmost view.
	 */
	public void showListView() {
		if (table != null) // nothing to do
			return;
		if (tree != null) {
			tree.setVisible(false);
			tree.clearAll(false);
			tree.dispose();
			tree = null;
			treeViewer = null;
		}
		addTableView(composite);
		composite.layout();

	}

	protected void addTableView (Composite aComposite) {
		tableViewer = new TableViewer(aComposite, SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.addDoubleClickListener(new ViewerDoubleClickListener());
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setMenu(createPopupMenu(table));
		tableViewer.setContentProvider(new PasswordTableContentProvider());
		tableViewer.setLabelProvider(new PasswordTableLabelProvider());
		tableViewer.setInput(new Object());
        tableViewer.setSorter(new PasswordTableSorter());

		final TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(100);
		tableColumn.setText(Messages.getString("PasswordSafeJFace.Column.Title")); //$NON-NLS-1$
		tableColumn.addSelectionListener(new TableColumnSelectionAdaptor(tableViewer, 1));
		WidgetPreferences.tuneTableColumn(tableColumn, getClass(), "table/title"); //$NON-NLS-1$

		final TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setWidth(100);
		tableColumn_1.setText(Messages.getString("PasswordSafeJFace.Column.UserName")); //$NON-NLS-1$
		tableColumn_1.addSelectionListener(new TableColumnSelectionAdaptor(tableViewer, 2));
        WidgetPreferences.tuneTableColumn(tableColumn_1, getClass(), "table/userName"); //$NON-NLS-1$

		final TableColumn tableColumn_2 = new TableColumn(table, SWT.NONE);
		tableColumn_2.setWidth(100);
		tableColumn_2.setText(Messages.getString("PasswordSafeJFace.Column.Notes")); //$NON-NLS-1$
		tableColumn_2.addSelectionListener(new TableColumnSelectionAdaptor(tableViewer, 3));
        WidgetPreferences.tuneTableColumn(tableColumn_2, getClass(), "table/notes"); //$NON-NLS-1$

        final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
		if (thePrefs.getBoolean(JpwPreferenceConstants.SHOW_PASSWORD_IN_LIST)) {
			final TableColumn tableColumn_3 = new TableColumn(table, SWT.NONE);
			tableColumn_3.setWidth(100);
			tableColumn_3.setText(Messages.getString("PasswordSafeJFace.Column.Password")); //$NON-NLS-1$
			tableColumn_3.addSelectionListener(new TableColumnSelectionAdaptor(tableViewer, 4));
		}

		// Sort on first column
		PasswordTableSorter pts = (PasswordTableSorter) tableViewer.getSorter();
		pts.sortOnColumn(1);

	}
	
	/**
	 * Set the table view as the topmost view.
	 */
	public void showTreeView() {
		if (isTreeViewShowing()) // nothing to do
			return;
		if (table != null) {
			table.setVisible(false);
			table.clearAll();
			table.dispose();
			table = null;
			tableViewer = null;
		}
		addTreeView(composite);
		composite.layout();
	}

	protected void addTreeView (Composite aComposite) {
		treeViewer = new TreeViewer(aComposite, SWT.BORDER);
		treeViewer.setLabelProvider(new PasswordTreeLabelProvider());
		treeViewer.setContentProvider(new PasswordTreeContentProvider());
		treeViewer.setSorter(new ViewerSorter());		
		treeViewer.addDoubleClickListener(new ViewerDoubleClickListener());
		treeViewer.setComparer(new IElementComparer() {
			public boolean equals(Object a, Object b) {
				if (a instanceof PwsEntryBean && b instanceof PwsEntryBean)
					return ((PwsEntryBean) a).getStoreIndex() == (((PwsEntryBean) b).getStoreIndex());
				else 
					return a.equals(b);
			}

			public int hashCode(Object element) {
				if (element instanceof PwsEntryBean)
					return ((PwsEntryBean) element).getStoreIndex();
				else
					return element.hashCode();
			}
		});
		tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setMenu(createPopupMenu(tree));

		treeViewer.setInput(new Object());

        final TreeColumn treeColumn = new TreeColumn(tree, SWT.CENTER);
        treeColumn.setText(Messages.getString("PasswordSafeJFace.Column.Title")); //$NON-NLS-1$
        treeColumn.setWidth(100);
        WidgetPreferences.tuneTreeColumn(treeColumn, getClass(), "tree/title"); //$NON-NLS-1$
//        treeColumn.addSelectionListener(new TreeColumnSelectionAdaptor(treeViewer, 1));

        final TreeColumn treeColumn_1 = new TreeColumn(tree, SWT.LEFT);
        treeColumn_1.setText(Messages.getString("PasswordSafeJFace.Column.UserName")); //$NON-NLS-1$
        treeColumn_1.setWidth(100);
        WidgetPreferences.tuneTreeColumn(treeColumn_1, getClass(), "tree/userName"); //$NON-NLS-1$
//        treeColumn_1.addSelectionListener(new TreeColumnSelectionAdaptor(treeViewer, 2));

        final TreeColumn treeColumn_2 = new TreeColumn(tree, SWT.LEFT);
        treeColumn_2.setText(Messages.getString("PasswordSafeJFace.Column.Notes")); //$NON-NLS-1$
        treeColumn_2.setWidth(100);
        WidgetPreferences.tuneTreeColumn(treeColumn_2, getClass(), "tree/notes"); //$NON-NLS-1$
//        treeColumn_2.addSelectionListener(new TreeColumnSelectionAdaptor(treeViewer, 3));

        final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
        if (thePrefs.getBoolean(JpwPreferenceConstants.SHOW_PASSWORD_IN_LIST)) {
        	final TreeColumn treeColumn_3 = new TreeColumn(tree, SWT.LEFT);
            treeColumn_3.setText(Messages.getString("PasswordSafeJFace.Column.Password")); //$NON-NLS-1$
            treeColumn_3.setWidth(100);
            WidgetPreferences.tuneTreeColumn(treeColumn_3, getClass(), "tree/password"); //$NON-NLS-1$
//            treeColumn_3.addSelectionListener(new TreeColumnSelectionAdaptor(treeViewer, 4));
        }
        
        TreeColumn[] columns = tree.getColumns();
        for (int i = 0; i < columns.length; i++) {
//        	ps.getDefaultInt("bla");
//        	columns[i].setWidth(100);
            columns[i].setMoveable(true);
        }
//        treeViewer.setExpandedState(arg0, arg1)

	}

	/**
	 * Returns whether the treeview is the active view.
	 * 
	 * @return true if the treeview is showing, false otherwise
	 */
	public boolean isTreeViewShowing() {
		return (tree != null);
	}
	
	public String getSelectedTreeGroup() {
		
		if (isTreeViewShowing()) {
			//TODO: return Tree PATH (node.subnode1.subnode2), not only node name (subnode2) 
			if (tree.getSelectionCount() == 1) {
				TreeItem ti = tree.getSelection()[0];
				if (ti.getItemCount() > 0) {
					return ti.getText();
				} else {
					// we're in a leaf node
					if (ti.getParentItem() != null) {
						return ti.getParentItem().getText(); 
					} else {
						return ""; // empty root element //$NON-NLS-1$
					}
				}
			}
		}
		return null;
	}

	/**
	 * Refresh the tree and table.
	 * 
	 */
	public void updateViewers() {
		if (isTreeViewShowing()) {
			treeViewer.setInput(getPwsDataStore());
			treeViewer.refresh();
		} else {
			tableViewer.setInput(getPwsDataStore());
			// tableViewer.refresh();
		}
	}

	/**
	 * Turns on or off the edit record menus (add/edit/delete) and save/as
	 * menus.
	 * 
	 * @param enabled
	 *            true to enable the menus, false otherwise
	 */
	private void setEditMenusEnabled(boolean enabled) {
		this.addRecordAction.setEnabled(enabled);
		this.editRecordAction.setEnabled(enabled);
		this.deleteRecordAction.setEnabled(enabled);
		this.saveFileAction.setEnabled(enabled);
		this.saveFileAsAction.setEnabled(enabled);
	}

	/**
	 * Exports the contents of the password safe to tab separated file.
	 * 
	 * @param filename
	 *            full path to output file
	 * @throws FileNotFoundException
	 */
	public void exportToText(String filename) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(filename);
			Iterator iter = getPwsFile().getRecords();
			CSVWriter csvWriter = new CSVWriter(fw, '\t');
			while (iter.hasNext()) {
				PwsRecord nextRecord = (PwsRecord) iter.next();
				List<String> nextEntry = new ArrayList<String>();
				
				if (nextRecord instanceof PwsRecordV1) {
					nextEntry.add(V1_GROUP_PLACEHOLDER);
					nextEntry.add(PwsEntryDTO.getSafeValue(nextRecord, PwsRecordV1.TITLE));
					nextEntry.add(PwsEntryDTO.getSafeValue(nextRecord, PwsRecordV1.USERNAME));
					nextEntry.add(PwsEntryDTO.getSafeValue(nextRecord, PwsRecordV1.PASSWORD));
				} else {
					nextEntry.add(PwsEntryDTO.getSafeValue(nextRecord, PwsRecordV2.GROUP));
					nextEntry.add(PwsEntryDTO.getSafeValue(nextRecord, PwsRecordV2.TITLE));
					nextEntry.add(PwsEntryDTO.getSafeValue(nextRecord, PwsRecordV2.USERNAME));
					nextEntry.add(PwsEntryDTO.getSafeValue(nextRecord, PwsRecordV2.PASSWORD));
				}
				String[] nextLine = nextEntry.toArray(new String[0]);
				csvWriter.writeNext(nextLine);
				
			}
		} catch (IOException ioe) {
			displayErrorDialog(Messages.getString("PasswordSafeJFace.ExportError.Title"), Messages.getString("PasswordSafeJFace.ExportError.Message"), ioe); //$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					log.warn("Could not close output text file", e); 
				}
			}

		}

	}
	
	public void importFromText(String filename) {

		File theFile = new File(filename);

		if (!theFile.exists()) {
			throw new RuntimeException(Messages.getString("PasswordSafeJFace.ImportCSV.NoFileFound") + filename + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		Reader fileReader = null;
		
		try {
		fileReader = new FileReader(theFile);
		
		CSVReader csvReader = new CSVReader(fileReader, '\t');
		
		String[] nextLine;
		PwsFile pwsFile = getPwsFile();
		while ((nextLine = csvReader.readNext()) != null) {
			if (nextLine.length < 2) continue; // ignore blank lines
			PwsRecord newRecord = pwsFile.newRecord();
			PwsEntryDTO entry = new PwsEntryDTO();
			if (!nextLine[0].equals(V1_GROUP_PLACEHOLDER)) {
				entry.setGroup(nextLine[0]);
			}
			entry.setTitle(nextLine[1]);
			entry.setUsername(nextLine[2]);
			entry.setPassword(nextLine[3]);
			entry.toPwsRecord(newRecord);
			pwsFile.add(newRecord);
		}
		} catch (Exception e) {
			
			throw new RuntimeException(Messages.getString("PasswordSafeJFace.ImportCSV.ProcessingError") + filename + "]", e); //$NON-NLS-1$ //$NON-NLS-2$
			
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					log.warn("Could not close import text file", e); 
				}
			}
		}
		
		
		this.updateViewers();
		
	}

	/**
	 * Import an XML file into the current passwordsafe.
	 * 
	 * @param filename
	 *            the name of the file
	 * @throws PasswordSafeException
	 */
	public void importFromXML(String filename) throws PasswordSafeException {

		File theFile = new File(filename);

		if (!theFile.exists()) {
			throw new RuntimeException(Messages.getString("PasswordSafeJFace.ImportXML.NoFileFound") + filename + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		BufferedInputStream bis;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			bis = new BufferedInputStream(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(Messages.getString("PasswordSafeJFace.ImportXML.CantOpen"), e); //$NON-NLS-1$
		}

		String utf8String;
		try {
			int c;
			
	        while ((c = bis.read()) != -1) {
	        	baos.write(c);
			}
	        
	        byte[] utf8Bytes = baos.toByteArray();
	        utf8String = new String(utf8Bytes, "UTF-8"); //$NON-NLS-1$
	        char header = utf8String.charAt(0);
	        if (header > 255) {
	        	utf8String = utf8String.substring(1); // skip utf leading char
	        }
	        	
		} catch (Exception ioe) {
			throw new RuntimeException(Messages.getString("PasswordSafeJFace.ImportXML.IOException"), ioe); //$NON-NLS-1$
		} finally
        {
		    IOUtils.closeQuietly(bis);
        }

		XMLDataParser xdp = new XMLDataParser();
		PwsEntryDTO[] entries = xdp.parse(utf8String);
		if (entries != null && entries.length > 0) {
			PwsFile pwsFile = getPwsFile();
			for (int i = 0; i < entries.length; i++) {
				PwsEntryDTO nextEntry = entries[i];
				PwsRecord newRecord = pwsFile.newRecord();
				nextEntry.toPwsRecord(newRecord);
				pwsFile.add(newRecord);
			}
			this.updateViewers();
		}

	}

	/**
	 * Export the current safe to the name file in XML format.
	 * 
	 * @param filename
	 *            the filename to export to
	 * @throws IOException
	 */
	public void exportToXML(String filename) throws IOException {

		PwsFile pwsFile = getPwsFile();
		if (pwsFile != null) {
			List<PwsEntryDTO> entryList = new ArrayList<PwsEntryDTO>();
			for (Iterator iter = pwsFile.getRecords(); iter.hasNext();) {
				PwsEntryDTO nextDTO = PwsEntryDTO.fromPwsRecord((PwsRecord) iter.next());
				entryList.add(nextDTO);
			}
			XMLDataParser xdp = new XMLDataParser();
			String xmlOutput = xdp.convertToXML(entryList.toArray(new PwsEntryDTO[0]));
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename));
			// output the UTF-8 BOM byte by byte directly to the stream
			// http://tech.badpen.com/index.cfm?mode=entry&entry=21
			dos.write(239); // 0xEF
			dos.write(187); // 0xBB
			dos.write(191); // 0xBF
			dos.write(xmlOutput.getBytes());
			dos.close();

		}

	}

	/**
	 * Display error dialog and log error.
	 * 
	 * @param title
	 *            the title of the dialog
	 * @param message
	 *            the message to display
	 * @param e
	 *            the exception that caused the problem.
	 */
	public void displayErrorDialog(String title, String message, Exception e) {
		MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR);
		mb.setText(title);
		mb.setMessage(message);
		mb.open();
		log.error(message, e);
	}
	
	/**
	 * Returns a shell listener. This shell listener gets registered with this
	 * window's shell.
	 * <p>
	 * The default implementation of this framework method returns a new
	 * listener that makes this window the active window for its window manager
	 * (if it has one) when the shell is activated, and calls the framework
	 * method <code>handleShellCloseEvent</code> when the shell is closed.
	 * Subclasses may extend or reimplement.
	 * </p>
	 * 
	 * @return a shell listener
	 */
	@Override
	protected ShellListener getShellListener() {
		return new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent event) {
				event.doit = false; // don't close now
				
		    	final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
				if (thePrefs.getBoolean(JpwPreferenceConstants.SHOW_ICON_IN_SYSTEM_TRAY)) {
					// minimize on close when sys tray is present
					getShell().setMinimized(true);
					setReturnCode(OK);
					return;
				}

				boolean cancelled = saveAppIfDirty();
				if (cancelled) {
					setReturnCode(OK);
				} else {
					tidyUpOnExit();
					handleShellCloseEvent(); // forwards to Window class
				}

			}
			/**
			 * Sent when a shell becomes the active window.
			 * The default behavior is to do nothing.
			 *
			 * @param e an event containing information about the activation
			 */
			@Override
			public void shellActivated(ShellEvent e) {
				if (lockTask != null) {
					lockTask.cancel();
					lockTask = null;
				}
				if (locked) {
					lockDbAction.performUnlock();
				}

			}

			/**
			 * Sent when a shell stops being the active window.
			 * The default behavior is to do nothing.
			 *
			 * @param e an event containing information about the deactivation
			 */
			@Override
			public void shellDeactivated(ShellEvent e) {
				startLockTimer();
			}
			

			/**
			 * Sent when a shell is un-minimized.
			 * The default behavior is to do nothing.
			 *
			 * @param e an event containing information about the un-minimization
			 */
			@Override
			public void shellDeiconified(ShellEvent e) {
				if (lockTask != null) {
					lockTask.cancel();
					lockTask = null;
				}
				if (locked) {
					lockDbAction.performUnlock();	
				}

			}

			/**
			 * Sent when a shell is minimized.
			 * The default behavior is to do nothing.
			 *
			 * @param e an event containing information about the minimization
			 */
			@Override
			public void shellIconified(ShellEvent e) {
				final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
				if (thePrefs.getBoolean(JpwPreferenceConstants.CLEAR_CLIPBOARD_ON_MIN)) {
					clearClipboardAction.run();
				}
				
				//handle Save and DB lock
				if (isDirty()) {
					if (thePrefs.getBoolean(JpwPreferenceConstants.CONFIRM_SAVE_ON_MIN)) {
						//TODO
						saveFileAction.run();
					} else {
						saveFileAction.run();
					}
				}
				
				if (thePrefs.getBoolean(JpwPreferenceConstants.LOCK_DB_ON_MIN)) {
					lockDbAction.performLock();
				}
				startLockTimer();
			}
			
			private void startLockTimer() {
				final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
				if (pwsFile != null && thePrefs.getBoolean(JpwPreferenceConstants.LOCK_ON_IDLE)){
					if (lockTask != null) {//be on the safe side:
						lockTask.cancel();
					}
					String idleMins = thePrefs.getString(JpwPreferenceConstants.LOCK_ON_IDLE_MINS);
					try {
						int mins = Integer.parseInt(idleMins);
						lockTask = lockDbAction.createTaskTimer();
						lockTimer.schedule(lockTask, mins * 60 * 1000);
						
					} catch (NumberFormatException anEx) {
						log.warn("Unable to set lock timer to an amount of" + idleMins + " minutes");  
					}
				}
			}


		};
	}


	public void clearView() {
		if (isTreeViewShowing())
			tree.clearAll(true);
		else
			table.clearAll();		
	}

}