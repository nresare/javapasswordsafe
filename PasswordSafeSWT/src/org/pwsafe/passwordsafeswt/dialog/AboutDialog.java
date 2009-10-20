/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.dialog;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.pwsafe.passwordsafeswt.action.LicenseAction;
import org.pwsafe.passwordsafeswt.action.VisitPasswordSafeWebsiteAction;
import org.pwsafe.passwordsafeswt.util.VersionInfo;

import com.swtdesigner.SWTResourceManager;

/**
 * AboutDialog shows author/contributor/contact details.
 * 
 * @author Glen Smith
 */
public class AboutDialog extends org.eclipse.jface.dialogs.Dialog {

	public AboutDialog(Shell parent) {
		super(parent);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		createMyContents(composite);
		applyDialogFont(composite);
		return composite;
	}
	
	/*
	 * (non-Javadoc) Method declared in Window.
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Messages.getString("AboutDialog.Title")); //$NON-NLS-1$);
	}

	
	protected void createMyContents(Composite aComp) {

		final Group group = new Group(aComp, SWT.NONE);
		group.setText(Messages.getString("AboutDialog.AboutLabel")); //$NON-NLS-1$
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setLayout(new GridLayout());

		final Label lblLogo = new Label(group, SWT.NONE);
		lblLogo.setAlignment(SWT.CENTER);
		lblLogo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		lblLogo.setImage(SWTResourceManager.getImage(AboutDialog.class, "/org/pwsafe/passwordsafeswt/images/psafetxtNew.gif")); //$NON-NLS-1$

		final Label lblAuthor = new Label(group, SWT.NONE);
		lblAuthor.setAlignment(SWT.CENTER);
		lblAuthor.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		lblAuthor.setText(Messages.getString("AboutDialog.Copyright")); //$NON-NLS-1$

		final Label lblVersion = new Label(group, SWT.NONE);
		lblVersion.setAlignment(SWT.CENTER);
		lblVersion.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_END));
		lblVersion.setText(NLS.bind(Messages.getString("AboutDialog.Version"), VersionInfo.getVersion())); //$NON-NLS-1$

		final Label lblWebsite = new Label(group, SWT.CENTER);
		lblWebsite.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				new VisitPasswordSafeWebsiteAction().run();
			}
		});
		lblWebsite.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		lblWebsite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		lblWebsite.setText(Messages.getString("AboutDialog.WebsiteLabel")); //$NON-NLS-1$
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create buttons
		((GridLayout) parent.getLayout()).numColumns++;// for layout
		ActionContributionItem licenceContrib = new ActionContributionItem(new LicenseAction());
		licenceContrib.fill(parent);		
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	}

}
