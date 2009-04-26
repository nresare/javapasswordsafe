package org.pwsafe.lib.datastore;

import java.util.List;
import java.util.Set;

import org.pwsafe.lib.exception.PasswordSafeException;
import org.pwsafe.lib.file.PwsFieldType;

/**
 * Provides a CRUD style access to PwsBeans.
 * 
 * @author roxon
 * @see PwsEntryBean
 */
public interface PwsEntryStore {
	
	void setSparseFields (Set<PwsFieldType> fieldTypes);
	
	List<PwsEntryBean> getSparseEntries (); 
	
	PwsEntryBean getEntry (int anIndex);
	
	boolean addEntry (PwsEntryBean anEntry) throws PasswordSafeException;
	
	boolean updateEntry (PwsEntryBean anEntry);
	
	boolean removeEntry (PwsEntryBean anEntry);
	
	void clear ();
}
