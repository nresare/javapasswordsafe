/*
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.model;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.lib.datastore.PwsEntryStore;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;

/**
 * Content provider for the tree.
 * 
 * @author Glen Smith
 */
public class PasswordTreeContentProvider implements ITreeContentProvider {

	private static final Log log = LogFactory.getLog(PasswordTreeContentProvider.class);

	PwsEntryStore dataStore;

	/**
	 * This class represents a group displayed in the tree.
	 */
	public static final class TreeGroup {

		private static final char GROUP_SEPARATOR = '.';
		String parent = "";
		String name = "";

		public TreeGroup(String groupPath) {
			if (groupPath == null) {
				groupPath = "";
			}
			if (isTopGroup(groupPath)) {
				this.name = groupPath;
			} else {
				final int lastDot = groupPath.lastIndexOf(GROUP_SEPARATOR);
				this.parent = groupPath.substring(0, lastDot);
				this.name = groupPath.substring(lastDot + 1);
			}
		}

		private boolean isTopGroup(final String groupPath) {
			return groupPath.lastIndexOf(GROUP_SEPARATOR) < 0;
		}

		/**
		 * @return the parent
		 */
		public String getParent() {
			return parent;
		}

		@Override
		public String toString() {
			return name;
		}

		public String getGroupPath () {
			return parent.length() == 0 ? name : parent + GROUP_SEPARATOR + name;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((parent == null) ? 0 : parent.hashCode());
			result = PRIME * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final TreeGroup other = (TreeGroup) obj;
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
				return false;
			}
			if (parent == null) {
				if (other.parent != null) {
					return false;
				}
			} else if (!parent.equals(other.parent)) {
				return false;
			}
			return true;
		}
	}

	/**
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(final Object parentElement) {
		final Set matchingRecs = new LinkedHashSet();
		if (parentElement instanceof TreeGroup) {
			final TreeGroup element = (TreeGroup) parentElement;
			final String parentGroup = element.getGroupPath();

			// return all entries matching this group...
			for (final PwsEntryBean theEntry : dataStore.getSparseEntries()) {
				String entryGroup = "";
				if (!"1".equals(theEntry.getVersion()))
					entryGroup = theEntry.getGroup();

				// TODO: This looks as if it breaks for V1 files
				if (parentGroup.equalsIgnoreCase(entryGroup)) {
					log.debug("Adding record");
					matchingRecs.add(theEntry);
				} else if (isChild(parentGroup, entryGroup)) {
					log.debug("Adding group");
					final int nextDot = entryGroup.indexOf('.', parentGroup.length() + 1);
					final int endOfGroup = nextDot > 0 ? nextDot : entryGroup.length();
					final String subGroup = entryGroup.substring(0, endOfGroup);
					matchingRecs.add(new TreeGroup(subGroup));
				}
			}
		}
		return matchingRecs.toArray();
	}

	private boolean isChild(final String stringParent, final String recGroup) {
		return recGroup.length() > stringParent.length() && recGroup.contains(".")
				&& recGroup.startsWith(stringParent)
				&& (recGroup.charAt(stringParent.length()) == '.');
	}

	/**
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public String getParent(final Object element) {
		if (element instanceof PwsEntryBean) {
			return ((PwsEntryBean) element).getGroup();
		} else if (element instanceof TreeGroup) {
			final TreeGroup theGroup = (TreeGroup) element;
			return theGroup.getParent();
		}
		return null;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(final Object node) {
		return node instanceof TreeGroup; // only
		// groups
		// have
		// children
	}

	/**
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(final Object inputElement) {
		final Set rootElements = new LinkedHashSet();
		if (inputElement instanceof PwsEntryStore) {
			dataStore = (PwsEntryStore) inputElement;
			for (final PwsEntryBean entry : dataStore.getSparseEntries()) {
				if ("1".equals(entry.getVersion())) {
					rootElements.add(entry);
				} else {
					String recGroup = "";
					if (entry.getGroup() != null) {
						recGroup = entry.getGroup();
					}
					if (recGroup.trim().length() == 0) { // empty group name
						rootElements.add(entry);
					} else { // add node for group name
						if (recGroup.indexOf('.') > 0) {
							recGroup = recGroup.substring(0, recGroup.indexOf('.'));
						}
						rootElements.add(new TreeGroup(recGroup));
					}
				}
			}

		}
		return rootElements.toArray();
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/**
	 * This is called when the view is changed from TreeView to TableView
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(final Viewer tv, final Object oldInput, final Object newInput) {
		final ISelection selection = tv.getSelection();
		if (newInput instanceof PwsEntryStore) {
			dataStore = (PwsEntryStore) newInput;
			tv.setSelection(selection);
			final PasswordSafeJFace app = PasswordSafeJFace.getApp();
			// app.setUrlCopyEnabled( app.getPwsFile() instanceof PwsFileV3 &&
			// app.getSelectedRecord().getUrl() != null );
			// log.debug( "setUrlCopyEnabled=" + (app.getSelectedRecord() !=
			// null) );
			if (app.getSelectedRecord() != null) {
				app.setUrlCopyEnabled(app.getSelectedRecord().getUrl() != null);
			}
		}
		if (log.isDebugEnabled())
			log.debug("Input changed fired");

	}

}