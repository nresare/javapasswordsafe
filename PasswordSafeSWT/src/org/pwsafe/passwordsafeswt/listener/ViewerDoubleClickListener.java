/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.listener;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.pwsafe.passwordsafeswt.action.CopyPasswordAction;
import org.pwsafe.passwordsafeswt.action.EditRecordAction;
import org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants;
import org.pwsafe.passwordsafeswt.util.UserPreferences;

/**
 * Used for double click events on the tree or list viewer.
 * 
 * @author Glen Smith
 */
public class ViewerDoubleClickListener implements IDoubleClickListener {

	CopyPasswordAction cpa;
	EditRecordAction era;

	public ViewerDoubleClickListener() {
		cpa = new CopyPasswordAction();
		era = new EditRecordAction();
	}
	/**
	 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
	 */
	public void doubleClick(final DoubleClickEvent dce) {
		if (dce.getSelection() instanceof ITreeSelection) {
			final ITreeSelection treeSelection = (ITreeSelection) dce.getSelection();
			final AbstractTreeViewer treeViewer = (AbstractTreeViewer) dce.getSource();
			boolean state = treeViewer.getExpandedState(treeSelection.getPaths()[0]);
			treeViewer.setExpandedState(treeSelection.getPaths()[0], ! state);
		} 
		if (UserPreferences.getInstance().getBoolean(
				JpwPreferenceConstants.DOUBLE_CLICK_COPIES_TO_CLIPBOARD)) {
			cpa.run();
		} else {
			era.run();
		}
	}

}
