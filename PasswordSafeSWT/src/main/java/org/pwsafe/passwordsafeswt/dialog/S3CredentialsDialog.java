package org.pwsafe.passwordsafeswt.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.pwsafe.lib.file.PwsS3Storage.AccountDetails;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class S3CredentialsDialog extends Dialog {

	S3InformationComposite accountComposite;

	AccountDetails account;

	public S3CredentialsDialog(IShellProvider parentShell) {
		super(parentShell);

	}

	/*
	 * (non-Javadoc) Method declared in Window.
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Messages.getString("S3CredentialsDialog.Title")); //$NON-NLS-1$
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite aComp = (Composite) super.createDialogArea(parent);

		Label exp = new Label(aComp, SWT.WRAP);
		String msg = Messages.getString("S3CredentialsDialog.Description") + //$NON-NLS-1$
				"file you must first provide information about your S3 account. " + //$NON-NLS-1$
				"If you don't have an S3 account, you must first sign up for one.\n" + //$NON-NLS-1$
				"This information will be encrypted using the same combination you " + //$NON-NLS-1$
				"provided for the Password Safe and stored locally.\n" + //$NON-NLS-1$
				"If the bucket named does not already contain a Password Safe\n" + //$NON-NLS-1$
				"file, one will be created using the same combination that is " + //$NON-NLS-1$
				"used to encrypt the S3 account information."; //$NON-NLS-1$
		exp.setText(msg);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.widthHint = SWT.DEFAULT;
		gridData.heightHint = SWT.DEFAULT;
		gridData.verticalIndent = 10;
		exp.setLayoutData(gridData);

		accountComposite = new S3InformationComposite(aComp, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.widthHint = SWT.DEFAULT;
		gridData.heightHint = SWT.DEFAULT;
		gridData.verticalIndent = 10;
		accountComposite.setLayoutData(gridData);

		return aComp;
	}

	@Override
	protected void okPressed() {
		account = new AccountDetails(getBucketName(), getAccessKey(),
				String.valueOf(getAccessSecret()));
		super.okPressed();
	}

	public String getBucketName() {
		return accountComposite.getBucketName();
	}

	public String getAccessKey() {
		return accountComposite.getAccessKey();
	}

	public char[] getAccessSecret() {
		return accountComposite.getAccessSecret();
	}

	public AccountDetails getAccountDetails() {

		int rc = open();
		if (rc == Window.OK)
			return account;
		else
			return null;
	}

}
