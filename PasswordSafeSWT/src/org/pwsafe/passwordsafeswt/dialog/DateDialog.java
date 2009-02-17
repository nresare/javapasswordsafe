/*
 * $Id$
 * 
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.dialog;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * A dialog to choose a Date from a calendar.
 * Designed similar to org.eclipse.swt.widgets.ColorDialog.
 *
 * @author David Müller
 */
public class DateDialog extends Dialog {

	private static final Log log = LogFactory.getLog(DateDialog.class);

	private Calendar cal = Calendar.getInstance();

	public DateDialog(Shell shell) {
		super(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}

	public DateDialog(Shell shell, int i) {
		super(shell, i);
	}
	
	public Date getDate() {
		return cal == null ? null : cal.getTime();
	}

	public Date open() {				
		final Shell shell = new Shell (getParent(), getStyle());
		shell.setText(getText());
		createContents(shell);
		shell.pack();
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}		
		return cal == null ? null : cal.getTime();
	}

	private void createContents(final Shell shell) {
		shell.setLayout (new GridLayout (2, true));

		final DateTime calendar = new DateTime (shell, SWT.CALENDAR | SWT.BORDER);
		GridData data = new GridData();
		data.horizontalSpan = 2;
		calendar.setLayoutData(data);
		calendar.setDay(cal.get(Calendar.DAY_OF_MONTH));
		calendar.setMonth(cal.get(Calendar.MONTH));
		calendar.setYear(cal.get(Calendar.YEAR));
		
		Button ok = new Button (shell, SWT.PUSH);
		ok.setText (Messages.getString("DateDialog.OkButton")); //$NON-NLS-1$
		ok.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false));
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				//if (log.isDebugEnabled())
					log.debug (NLS.bind(Messages.getString("DateDialog.Log.DateSelected"),new Integer []{calendar.getMonth () + 1, calendar.getDay (),calendar.getYear ()}) ); //$NON-NLS-1$ 
				cal.set(Calendar.DAY_OF_MONTH, calendar.getDay());
				cal.set(Calendar.MONTH, calendar.getMonth());
				cal.set(Calendar.YEAR, calendar.getYear());
				shell.close ();
			}
		});
		shell.setDefaultButton (ok);
		
		Button cancel = new Button (shell, SWT.PUSH);
		cancel.setText (Messages.getString("DateDialog.CancelButton")); //$NON-NLS-1$
		cancel.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false));
		cancel.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				log.debug (NLS.bind(Messages.getString("DateDialog.Log.DateSelected"),new Integer []{0, 0,0}) ); //$NON-NLS-1$
				cal = null;
				shell.close ();
			}
		});

	}

	public void setDate(Date aDate) {
		if (cal == null)
			cal = Calendar.getInstance();
		if (aDate != null) 
			cal.setTimeInMillis(aDate.getTime());
	}
}
