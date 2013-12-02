package org.pwsafe.passwordsafeswt.dialog;

/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.pwsafe.passwordsafeswt.action.FindRecordAction;


/**
 * Find dialog box.
 *
 * @author Tim Hughes
 */
public class FindRecordDialog extends InputDialog {

    private FindRecordAction findRecordAction;

    // todo: use org.pwsafe.lib.Log;?

    //todo move to somewhere more central
    private final static Log LOG = LogFactory.getLog(FindRecordDialog.class);
    private final static int FIND_BUTTON_ID = IDialogConstants.CLIENT_ID + 1;
    private final static int FIND_PREVIOUS_BUTTON_ID = IDialogConstants.CLIENT_ID + 2;


    public FindRecordDialog( Shell parentShell, String dialogTitle, String dialogMessage,
			String initialValue, IInputValidator validator) {

		super( parentShell, dialogTitle, dialogMessage, initialValue, validator );
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
	protected void buttonPressed( int buttonId ) {
        setErrorMessage(null);

        // todo: reset auto-lock timer
        // todo: hide find dialog once locked and restore once unlocked again
		if( buttonId == FIND_BUTTON_ID) {
			findRecordAction.performSearch(getValue(), false, FindRecordAction.fullTextSubStringMatcher); //titleSubStringMatcher);
		}
		else if( buttonId == FIND_PREVIOUS_BUTTON_ID ) {
            findRecordAction.performSearch(getValue(), true, FindRecordAction.fullTextSubStringMatcher); //titleSubStringMatcher);
        }
        else {
			close();
		}
	}


    @Override
    public String getValue() {
        return getText().getText();
    }


    @Override
	protected void createButtonsForButtonBar(Composite parent) {
		LOG.debug("createButtonsForButtonBar called");
		// create buttons
		Button findButton = createButton(parent, FIND_BUTTON_ID, Messages.getString("FindDialog.Find"), true);
        // todo: add F3 accelerator for find next and shift+F3 for previous
        createButton(parent, FIND_PREVIOUS_BUTTON_ID, Messages.getString("FindDialog.FindPrevious"), false);
		createButton(parent, IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL, false);
        getText().setFocus();
	}


    // Todo fix overly tight coupling from Action -> Dialog?
    public void setCallingAction(FindRecordAction findRecordAction) {
        this.findRecordAction = findRecordAction;
    }
}



