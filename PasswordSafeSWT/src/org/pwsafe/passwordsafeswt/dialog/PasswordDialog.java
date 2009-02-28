/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.dialog;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pwsafe.passwordsafeswt.util.ShellHelpers;

/**
 * Prompts the user for passwords for a new safe.
 * 
 * @author Glen Smith
 */
public class PasswordDialog extends Dialog {

	protected static final Log log = LogFactory.getLog(PasswordDialog.class);

	protected String windowTitle = Messages.getString("PasswordDialog.Title"); //$NON-NLS-1$;
	protected String purposeText = Messages.getString("PasswordDialog.Info"); //$NON-NLS-1$;
	protected String shortFileName = "";
	protected String fileName = "";
	private Text txtVerify;
	private Text txtCombination;
	protected Object result;
	protected Shell shell;
	protected boolean verified = true;

	public PasswordDialog(Shell parent, int style) {
		super(parent, style);
	}

	public PasswordDialog(Shell parent) {
		this(parent, SWT.NONE);
	}

	public Object open() {
		createContents();
		ShellHelpers.centreShell(getParent(), shell);
		// shell.pack();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}

	protected void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setLayout(new FormLayout());
		shell.setSize(380, 250);
		shell.setText(windowTitle);
		final Label label = new Label(shell, SWT.WRAP);
		final FormData formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.left = new FormAttachment(0, 5);
		label.setLayoutData(formData);
		label.setText(purposeText + " " + shortFileName); //$NON-NLS-1$;
		label.setToolTipText(fileName);

		final Label lblCombination = new Label(shell, SWT.NONE);
		final FormData formData_1 = new FormData();
		formData_1.top = new FormAttachment(label, 30, SWT.BOTTOM);
		formData_1.left = new FormAttachment(10, 0);
		lblCombination.setLayoutData(formData_1);
		lblCombination.setText(Messages.getString("PasswordDialog.SafeCombination")); //$NON-NLS-1$;

		txtCombination = new Text(shell, SWT.PASSWORD | SWT.BORDER);
		final FormData formData_2 = new FormData();
		formData_2.top = new FormAttachment(lblCombination, 0, SWT.TOP);
		formData_2.left = new FormAttachment(lblCombination, 5);
		formData_2.right = new FormAttachment(85, 0);
		txtCombination.setLayoutData(formData_2);

		if (verified) {
			final Label lblVerify = new Label(shell, SWT.NONE);
			final FormData formData_3 = new FormData();
			formData_3.top = new FormAttachment(lblCombination, 20);
			formData_3.right = new FormAttachment(lblCombination, 0, SWT.RIGHT);
			lblVerify.setLayoutData(formData_3);
			lblVerify.setText(Messages.getString("PasswordDialog.Verify")); //$NON-NLS-1$

			txtVerify = new Text(shell, SWT.PASSWORD | SWT.BORDER);
			final FormData formData_4 = new FormData();
			formData_4.top = new FormAttachment(lblVerify, 0, SWT.TOP);
			formData_4.left = new FormAttachment(txtCombination, 0, SWT.LEFT);
			formData_4.right = new FormAttachment(txtCombination, 0, SWT.RIGHT);
			txtVerify.setLayoutData(formData_4);
		}
		final Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		final FormData formData_7 = new FormData();
		formData_7.width = 80;
		formData_7.bottom = new FormAttachment(100, -10);
		formData_7.left = new FormAttachment(50, -5);
		btnCancel.setLayoutData(formData_7);
		btnCancel.setText(Messages.getString("PasswordDialog.CancelButton")); //$NON-NLS-1$

		final Button btnOk = new Button(shell, SWT.NONE);
		shell.setDefaultButton(btnOk);

		if (verified) {
			btnOk.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (txtCombination.getText().equals(txtVerify.getText())) {
						result = txtCombination.getText();
						shell.dispose();
					} else {
						MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR
								| SWT.OK);
						mb.setText(Messages.getString("PasswordDialog.PasswordMismatchMessage.Title")); //$NON-NLS-1$
						mb.setMessage(Messages.getString("PasswordDialog.PasswordMismatchMessage.Text")); //$NON-NLS-1$
						mb.open();
					}
				}
			});
		} else {
			btnOk.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					result = txtCombination.getText();
					shell.dispose();
				}
			});
		}
		final FormData formData_6 = new FormData();
		formData_6.width = 80;
		formData_6.top = new FormAttachment(btnCancel, 0, SWT.TOP);
		formData_6.right = new FormAttachment(btnCancel, -10, SWT.LEFT);
		btnOk.setLayoutData(formData_6);
		btnOk.setText(Messages.getString("PasswordDialog.OkButton")); //$NON-NLS-1$

		/*
		 * final Button btnHelp = new Button(shell, SWT.NONE); final FormData
		 * formData_8 = new FormData(); formData_8.width = 80; formData_8.top =
		 * new FormAttachment(btnCancel, 0, SWT.TOP); formData_8.left = new
		 * FormAttachment(btnCancel, 10, SWT.RIGHT);
		 * btnHelp.setLayoutData(formData_8); btnHelp.setText(Messages.getString("PasswordDialog.HelpButton")); //$NON-NLS-1$
		 */
	}

	/**
	 * @param aFileName
	 *            The fileName to set.
	 */
	public void setFileName(String aFileName) {
		try {
			shortFileName = new File(aFileName).getName();
		} catch (Exception anEx) {
			log.debug("Ignored exception trying to get a file from " + aFileName, anEx);
			shortFileName = aFileName;
		}
		fileName = aFileName;
	}

	public void setVerified(boolean verify) {
		this.verified = verify;
	}
	
}
