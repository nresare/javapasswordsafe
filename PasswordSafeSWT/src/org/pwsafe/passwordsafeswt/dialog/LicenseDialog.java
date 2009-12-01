/*
 * $Id$
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.dialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author roxon
 *
 */
public class LicenseDialog extends TitleAreaDialog {

	private static final Log LOG = LogFactory.getLog(LicenseDialog.class);

	public LicenseDialog(Shell parentShell) {
		super(parentShell);	
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	
	/**
	   * Creates the dialog's contents
	   * 
	   * @param parent the parent composite
	   * @return Control
	   */
	  @Override
	protected Control createContents(Composite parent) {
	    Control contents = super.createContents(parent);

	    // Set the title
	    setTitle(Messages.getString("LicenseDialog.Title")); //$NON-NLS-1$
	    
	    // Set the message
	    setMessage(Messages.getString("LicenseDialog.Message"), IMessageProvider.INFORMATION); //$NON-NLS-1$


	    return contents;
	  }

	@Override
	protected Control createDialogArea(Composite parent) {
		// create the top level composite for the dialog area
		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 2;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setFont(parent.getFont());
		// Build the separator line
		final Label titleBarSeparator = new Label(composite, SWT.HORIZONTAL
				| SWT.SEPARATOR);
		titleBarSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		final Group group = new Group(composite, SWT.NONE);
		group.setText(Messages.getString("LicenseDialog.LicenseLabel")); //$NON-NLS-1$
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginHeight = 5;		
		group.setLayout(gridLayout_2);
		
		final Text txtLicence = new Text(group, SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY | SWT.MULTI);
		final GridData txtGrid = new GridData(SWT.FILL, SWT.CENTER, true, true);
		txtGrid.horizontalIndent = 6;
		txtGrid.minimumHeight = 300;
		txtGrid.minimumWidth = 300;
		txtLicence.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		
//		ColorRegistry colors = JFaceResources.getColorRegistry();
//		Color foreground = colors.get("WHITE");
//		Color background = colors.get("WHITE");
//		
//		if (foreground != null)
//			txtLicence.setForeground(foreground);
//		if (background != null)
//			txtLicence.setBackground(background);
		
		txtLicence.setText(getLicence().toString());
		
		return composite;
	}
	
	private StringBuilder getLicence () {
		final StringBuilder licence = new StringBuilder(2048);
		final String licenceFileName = "LICENSE"; //$NON-NLS-1$
		URL address = getClass().getResource("../"+licenceFileName); //$NON-NLS-1$
		if (address == null) {
			address = getClass().getResource(licenceFileName); 
		}
		InputStream in = null;
		try {
			if (address == null) {
				address = new File(licenceFileName).toURI().toURL(); 
			}
			LOG.debug("License path " + address); //$NON-NLS-1$
					
			in = address.openStream();
			in = new BufferedInputStream(in);

			InputStreamReader inReader = null;
			inReader = new InputStreamReader(in, "UTF-8"); //$NON-NLS-1$
			while (true) {
				int c = inReader.read();
				if (c == -1) break;
				licence.append((char) c);
			}
			inReader.close();
		} catch (MalformedURLException e) {
			LOG.error(e);
		} catch (UnsupportedEncodingException e1) {
			LOG.error(e1);

		} catch (IOException e) {
			LOG.error(e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					LOG.warn("Exception on close" + e);
				}
		}
		return licence;
	}
	
	/**
	 * Adds buttons to this dialog's button bar.
	 * <p>
	 * The <code>Dialog</code> implementation of this framework method adds
	 * standard ok and cancel buttons using the <code>createButton</code>
	 * framework method. These standard buttons will be accessible from
	 * <code>getCancelButton</code>, and <code>getOKButton</code>.
	 * Subclasses may override.
	 * </p>
	 * 
	 * @param parent
	 *            the button bar composite
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);

	}
	
}
