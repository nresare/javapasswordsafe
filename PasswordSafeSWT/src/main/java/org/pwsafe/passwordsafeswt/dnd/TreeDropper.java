/*
 * Copyright (c) 2013-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.dnd;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.lib.datastore.PwsEntryStore;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.model.PasswordTreeContentProvider;
import org.pwsafe.passwordsafeswt.model.PasswordTreeContentProvider.TreeGroup;

/**
 * Performs drops in the tree view.
 * For now only the drop of a bean on groups is correctly handled.
 * 
 * @author roxon
 *
 */
public final class TreeDropper extends ViewerDropAdapter {

	private static final Log log = LogFactory.getLog(TreeDropper.class);

	public TreeDropper(final Viewer viewer) {
		super(viewer);
	}

	@Override
	public boolean performDrop(final Object data) {

		if (data instanceof Integer) {
			return performDropOfPwsEntryBean((Integer) data);
		} else if (data instanceof String) {
			return performDropOfTreeGroup((String) data);
		} else {
			log.warn("Ignore drop of unknown type: " + data);
			return false;
		}
	}

	private boolean performDropOfPwsEntryBean(final Integer aDragSourceIndex) {
		Object target = getCurrentTarget();

		// allow drop on "root" null
		if (target == null) {
			target = new TreeGroup("");
		}

		String newGroup;
		if (target instanceof TreeGroup) {
			final PasswordTreeContentProvider.TreeGroup treeGroup =
					(PasswordTreeContentProvider.TreeGroup) target;
			newGroup = treeGroup.getGroupPath();
		} else if (target instanceof PwsEntryBean) {
			final PwsEntryBean sparseEntry = (PwsEntryBean) target;
			// ignore drop on itself
			if (sparseEntry.getStoreIndex() == aDragSourceIndex) {
				return false;
			}
			newGroup = sparseEntry.getGroup();
		} else {
			newGroup = "unknownTarget-" + String.valueOf(target);
		}

		updateGroup(aDragSourceIndex, newGroup);
		return true;
	}

	private void updateGroup(final Integer aTargetIndex, final String aNewGroup) {
		// naive impl: update datastore, update viewers
		final PwsEntryStore store = PasswordSafeJFace.getApp().getPwsDataStore();
		final PwsEntryBean entry = store.getEntry(aTargetIndex);
		entry.setGroup(aNewGroup);
		PasswordSafeJFace.getApp().updateRecord(entry);
	}

	private boolean performDropOfTreeGroup(final String aGroup) {
		final Object target = getCurrentTarget();

		if (target instanceof PasswordTreeContentProvider.TreeGroup) {
			final PasswordTreeContentProvider.TreeGroup treeGroup =
					(PasswordTreeContentProvider.TreeGroup) target;
			// ignore drop on itself
			if (treeGroup.getGroupPath() == aGroup) {
				return false;
			}
			// not implemented yet
			return false;
		} else if (target instanceof PwsEntryBean) {
			final PwsEntryBean sparseEntry = (PwsEntryBean) target;
			updateGroup(sparseEntry.getStoreIndex(), aGroup);
			return true;
		}
		return false;
	}

	@Override
	public boolean validateDrop(final Object target, final int operation,
			final TransferData type) {
		if (PwsEntryBeanTransfer.getInstance().isSupportedType(type)) {
			return true;
		}
		return TextTransfer.getInstance().isSupportedType(type);
	}

}
