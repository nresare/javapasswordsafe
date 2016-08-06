/*
 *  * $Id$
 *
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.datastore;

import java.util.List;
import java.util.Set;

import org.pwsafe.lib.exception.PasswordSafeException;
import org.pwsafe.lib.file.PwsFieldType;
import org.pwsafe.lib.file.PwsFile;

/**
 * Provides a CRUD style access to PwsBeans.
 * 
 * @author roxon
 * @see PwsEntryBean
 */
public interface PwsEntryStore {

	void setSparseFields(final Set<PwsFieldType> fieldTypes);

	List<PwsEntryBean> getSparseEntries();

	PwsEntryBean getEntry(final int anIndex);

	boolean addEntry(final PwsEntryBean anEntry) throws PasswordSafeException;

	boolean updateEntry(final PwsEntryBean anEntry);

	boolean removeEntry(final PwsEntryBean anEntry);

	void clear();

	/**
	 * 
	 * @return the PwwFile associated with the store-
	 */
	PwsFile getPwsFile();
}
