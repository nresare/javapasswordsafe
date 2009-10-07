/*
 * $Id$
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.pwsafe.passwordsafeswt.action.ExitAppAction;
import org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants;

import com.swtdesigner.SWTResourceManager;

/**
 * 
 * @author roxon
 *
 */
public class SysTray {

	protected static final Log log = LogFactory.getLog(SysTray.class);

	private TrayItem trayItem;

	
	/**
	 * On platforms where a tray is supported, installs the event handlers for
	 * showing and hiding the tray.
	 */
	public boolean init(final Shell aMainShell) {
		IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
		
		if (! thePrefs.getBoolean(JpwPreferenceConstants.SHOW_ICON_IN_SYSTEM_TRAY)) {
			return false;
		}
		
		Display display = null;
		if (aMainShell == null) {
		// Check whether this works on Linux
			display = Display.getCurrent();
			if (display == null) {
				display = Display.getDefault();
			}
		} else {
			display = aMainShell.getDisplay();
		}
		
		Image image = SWTResourceManager.getImage(PasswordSafeJFace.class,
				"/org/pwsafe/passwordsafeswt/images/cpane.gif"); //$NON-NLS-1$
		
		ImageData data = image.getImageData();
		data.scaledTo(16, 16);
		image = new Image(display, data);
		
		final Tray tray = display.getSystemTray();
		if (tray == null) {
			if (log.isInfoEnabled())
				log.info("The system tray is not available"); 
			return false;
		} else {
			if (log.isDebugEnabled())
				log.debug("Setting up System Tray"); 

			trayItem = new TrayItem(tray, SWT.NONE);
			trayItem.setToolTipText(PasswordSafeJFace.APP_NAME);

			// hide / restore on double click
			trayItem.addSelectionListener(new SelectionAdapter () {
				//TODO: Check whether to FIRST check for locking etc. 
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					final Shell mainShell = getPwsMainShell ();

					if (! mainShell.getVisible()) {
						mainShell.setVisible(true);
						mainShell.setMinimized(false);
					} else {
						mainShell.setVisible(false);
						mainShell.setMinimized(true);					
					}

				}
			});
			final Menu menu = new Menu(new Shell(display));
						
			MenuItem trayRestore = new MenuItem(menu, SWT.PUSH);
			trayRestore.setText(Messages.getString("PasswordSafeJFace.Tray.RestoreLabel")); //$NON-NLS-1$
			trayRestore.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						final Shell mainShell = getPwsMainShell ();
						mainShell.setVisible(true);
						mainShell.setMinimized(false);
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
			// hide / restore on double click
//			trayItem.addSelectionListener(new SelectionAdapter() {
//				@Override
//				public void widgetSelected(SelectionEvent arg0) {
//					final Shell mainShell = getPwsMainShell ();
//					if (mainShell.getMinimized()) {
//						mainShell.setMinimized(false);
//						mainShell.setVisible(true);
//					} else if (mainShell.isVisible()) {
//						mainShell.setMinimized(true);					
//						mainShell.setVisible(false);
//					}
//
//				}
//			});
			
			return true;
		}
	}
	
	private Shell getPwsMainShell () {
		return PasswordSafeJFace.getApp().getShell();
	}

	
	public void dispose() {
		trayItem.getImage().dispose();
		trayItem.dispose();
	}

}
