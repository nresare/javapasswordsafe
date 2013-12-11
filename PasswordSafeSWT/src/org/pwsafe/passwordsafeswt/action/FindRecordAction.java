/*
 * Copyright (c) 2010-2013 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.dialog.FindRecordDialog;
import org.pwsafe.passwordsafeswt.model.comparator.EqualsIgnoreCaseComparator;
import org.pwsafe.passwordsafeswt.model.comparator.FindMatcher;
import org.pwsafe.passwordsafeswt.model.comparator.FullTextSubStringMatcher;
import org.pwsafe.passwordsafeswt.model.comparator.TitleSubStringMatcher;

/**
 * Find action, called from the edit menu or using a Ctrl+f hot-key.
 * 
 * @author Tim Hughes
 */
public class FindRecordAction extends Action {

	static final Log log = LogFactory.getLog(FindRecordAction.class);

	private final SearchState searchState = new SearchState();
	public EqualsIgnoreCaseComparator resultsSorter = new EqualsIgnoreCaseComparator();

	private FindRecordDialog findDialog;
	public static final FindMatcher titleSubStringMatcher = new TitleSubStringMatcher();
	public static final FindMatcher fullTextSubStringMatcher = new FullTextSubStringMatcher();

	private class SearchState {
		private PwsEntryBean currentEntry;
		private LinkedList<PwsEntryBean> results;
		private String searchString;

		public PwsEntryBean getCurrentEntry() {
			return currentEntry;
		}

		public void setCurrentEntry(final PwsEntryBean currentEntry) {
			this.currentEntry = currentEntry;
		}

		/**
		 * Gets the current set of results or an empty list if there are none.
		 * 
		 * @return The current list of results.
		 */
		public LinkedList<PwsEntryBean> getResults() {
			if (results == null) {
				setResults(new LinkedList<PwsEntryBean>());
			}
			return results;
		}

		public void setResults(final LinkedList<PwsEntryBean> newResults) {
			results = newResults;
		}

		public int getResultCount() {
			return getResults().size();
		}

		public String getSearchString() {
			return searchString;
		}

		/**
		 * This updates the string that we're searching for and clears out the
		 * old results if it has actually changed.
		 * 
		 * @param newSearchString What the user wants to search for.
		 */
		public void setSearchString(final String newSearchString) {
			if (searchString != null && !searchString.equals(newSearchString)) {
				setResults(null);
				setCurrentEntry(null);
			}
			searchString = newSearchString;
		}
	}

	public FindRecordAction() {
		super(Messages.getString("FindAction.Label")); //$NON-NLS-1$
		setAccelerator(SWT.MOD1 | 'F');
		setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader()
				.getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_find.png"))); //$NON-NLS-1$
		setToolTipText(Messages.getString("FindAction.Tooltip")); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		log.debug("running find action");
		final PasswordSafeJFace app = PasswordSafeJFace.getApp();
		// app.getShell().addKeyListener( new FindKeyListener() );
		// app.getShell().getDisplay().addListener(SWT.KeyDown, (Listener)new
		// FindKeyListener()); causes class cast ex
		findDialog = new FindRecordDialog(app.getShell(), Messages.getString("FindAction.Label"),
				Messages.getString("FindAction.Message"), searchState.getSearchString(), null);
		// todo refactor to remove this overly-tight coupling? Comments or
		// opinions gratefully received.
		findDialog.setCallingAction(this);
		findDialog.open();
	}

	/**
	 * This figures out what to do with the search state and therefore with what
	 * the user wants from the search. It also gives feedback to the user about
	 * the outcome.
	 * 
	 * todo implement switching comparators todo would be nice to search from
	 * the currently highlighted TreeItem rather than the top todo reduce cyclic
	 * complexity of this
	 * 
	 * @param newSearchString The search string that is currently in the
	 *        dialog's text field.
	 * @param previous Are we doing a find-previous?
	 * @param equality Allows us to switch between a
	 *        sub-string/case-insensitive/etc search
	 */
	public void performSearch(final String newSearchString, final boolean previous,
			final FindMatcher equality) {
		log.debug("performSearch called");
		final PasswordSafeJFace app = PasswordSafeJFace.getApp();

		if (!newSearchString.equals(searchState.getSearchString())) {
			// the search string has changed so re-do the search
			searchState.setSearchString(newSearchString);
			searchState.setResults(findDataStoreEntries(equality));

			if (searchState.getResultCount() > 0) {
				sortResults(searchState.getResults(), resultsSorter);
				searchState.setCurrentEntry(searchState.getResults().get(0));
			} else {
				findDialog.setErrorMessage(Messages.getString("FindAction.NoResults"));
			}

		} else {
			// the search string hasn't changed so do a "find next"
			int index = searchState.getResults().indexOf(searchState.getCurrentEntry());

			if (searchState.getResultCount() == 1) {
				findDialog.setErrorMessage(Messages.getString("FindAction.NoMoreResults"));
			} else if (index == searchState.getResultCount() - 1 && !previous) {
				findDialog.setErrorMessage(Messages.getString("FindAction.BackToBeginning"));
				index = 0;
			} else if (index == 0 && previous) {
				findDialog.setErrorMessage(Messages.getString("FindAction.BackToEnd"));
				index = searchState.getResultCount() - 1;
			} else if (!previous) {
				index++;
			} else {
				index--;
			}

			searchState.setCurrentEntry(searchState.getResults().get(index));
		}

		if (searchState.getCurrentEntry() != null)
			// select entry
			app.highlightEntry(searchState.getCurrentEntry());
	}

	private LinkedList<PwsEntryBean> findDataStoreEntries(final FindMatcher matcher) {

		final PasswordSafeJFace app = PasswordSafeJFace.getApp();
		for (final PwsEntryBean entry : app.getPwsDataStore().getSparseEntries()) {
			if (matcher.matches(searchState.getSearchString(), entry)) {
				searchState.getResults().add(entry);
			}
		}
		return searchState.getResults();
	}

	private void sortResults(final LinkedList<PwsEntryBean> results, final Comparator resultsSorter) {
		Collections.sort(results, resultsSorter);
		// Collections.sort( searchState.getResults(),
		// PasswordSafeJFace.getApp().getSorter() );
	}

	private class FindKeyListener implements KeyListener {

		public void keyPressed(final KeyEvent keyEvent) {
			boolean findPrevious = false;
			if ((keyEvent.stateMask & SWT.SHIFT) != 0) {
				// shift pressed
				findPrevious = true;
			}

			// if( keyEvent.character == SWT.F3 ){
			if (keyEvent.keyCode == SWT.F3) {
				performSearch(findDialog.getValue(), findPrevious, fullTextSubStringMatcher);
			}
		}

		public void keyReleased(final KeyEvent keyEvent) {
			// do nothing
		}
	}

}
