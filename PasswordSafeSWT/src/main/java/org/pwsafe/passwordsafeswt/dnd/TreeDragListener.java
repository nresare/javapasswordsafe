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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.passwordsafeswt.model.PasswordTreeContentProvider;

/**
 * Listens for drags in the tree view.
 * For now only the dragging of a bean is correctly handled.
 * 
 * @author roxon
 *
 */
public class TreeDragListener implements DragSourceListener {

	private static final Log log = LogFactory.getLog(TreeDragListener.class);

	private final StructuredViewer viewer;

	public TreeDragListener(final StructuredViewer viewer) {
		this.viewer = viewer;
	}

	public void dragFinished(final DragSourceEvent event) {
		log.debug("Finished Drag");
	}

	public void dragSetData(final DragSourceEvent event) {
		final IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();

		if (selection.getFirstElement() instanceof PwsEntryBean) {
			final PwsEntryBean firstElement = (PwsEntryBean) selection.getFirstElement();
			if (PwsEntryBeanTransfer.getInstance().isSupportedType(event.dataType)) {
				event.data = firstElement.getStoreIndex();
			}
		} else if (selection.getFirstElement() instanceof PasswordTreeContentProvider.TreeGroup) {
			event.doit = false; // disable for now
			final PasswordTreeContentProvider.TreeGroup group =
					(PasswordTreeContentProvider.TreeGroup) selection.getFirstElement();

			if (PwsEntryBeanTransfer.getInstance().isSupportedType(event.dataType)) {
				event.data = group.getGroupPath();
			}
			if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
				event.data = group.getGroupPath();
			}
		} else {
			log.warn("Unknown type: " + selection.getFirstElement().getClass());
		}
	}

	public void dragStart(final DragSourceEvent event) {
		log.debug("Start Drag");
	}
}
