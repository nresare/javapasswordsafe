/*
 * Copyright (c) 2008-2013 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.dialog;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.pwsafe.passwordsafeswt.action.FindRecordAction;
import org.pwsafe.passwordsafeswt.state.LockState;

/**
 * Find dialog box.
 * 
 * @author Tim Hughes
 */
public class FindRecordDialog extends InputDialog implements Observer {

	private FindRecordAction findRecordAction;

	// todo: use org.pwsafe.lib.Log;?

	// todo move to somewhere more central
	private final static Log LOG = LogFactory.getLog(FindRecordDialog.class);
	private final static int FIND_BUTTON_ID = IDialogConstants.CLIENT_ID + 1;
	private final static int FIND_PREVIOUS_BUTTON_ID = IDialogConstants.CLIENT_ID + 2;

	public FindRecordDialog(final Shell parentShell, final String dialogTitle,
			final String dialogMessage, final String initialValue, final IInputValidator validator) {

		super(parentShell, dialogTitle, dialogMessage, initialValue, validator);
	}

	/**
	 * Called to open the "find" dialog pop-up window.
	 */
	@Override
	public int open() {
		LOG.debug("open called");
		setBlockOnOpen(true);
		super.open();

		return Window.OK;
	}

	@Override
	protected void buttonPressed(final int buttonId) {
		setErrorMessage(null);

		// todo: reset auto-lock timer
		// todo: hide find dialog once locked and restore once unlocked again
		if (buttonId == FIND_BUTTON_ID) {
			findRecordAction.performSearch(getValue(), false,
					FindRecordAction.fullTextSubStringMatcher); // titleSubStringMatcher);
		} else if (buttonId == FIND_PREVIOUS_BUTTON_ID) {
			findRecordAction.performSearch(getValue(), true,
					FindRecordAction.fullTextSubStringMatcher); // titleSubStringMatcher);
		} else {
			close();
		}
	}

	@Override
	public String getValue() {
		return getText().getText();
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		LOG.debug("createButtonsForButtonBar called");
		// create buttons
		createButton(parent, FIND_BUTTON_ID,
				Messages.getString("FindDialog.Find"), true); //$NON-NLS-1$
		createButton(parent, FIND_PREVIOUS_BUTTON_ID,
				Messages.getString("FindDialog.FindPrevious"), false); //$NON-NLS-1$
		// Todo: somehow the default JFace labels don't work... :-(
		//		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.getString("FindDialog.CancelButton"), false);
		getText().setFocus();
	}

	// Todo fix overly tight coupling from Action -> Dialog?
	public void setCallingAction(final FindRecordAction findRecordAction) {
		this.findRecordAction = findRecordAction;
	}

	/**
	 * This method is called whenever the lock state of the application changes.
	 * 
	 * @param o the observable LockState object.
	 * @param arg the Boolean value that the lock state has been set to.
	 */
	// todo move this method into a super-class (abstract LockStateObserver) if
	// we have another similar dialog
	public void update(final Observable o, final Object arg) {
		if ((o instanceof LockState) && (arg instanceof Boolean)) {
			// we expect do be called on the swt event thread, so we simply do:
			final boolean lockState = (Boolean) arg;
			getShell().setVisible(!lockState);
			// shell.setActive(); // always??
			final int i = 0;
		}
	}
}
