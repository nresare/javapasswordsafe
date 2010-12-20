/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.util;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.preference.PreferenceStore;
import org.pwsafe.passwordsafeswt.preference.JpwPreferenceInitializer;

/**
 * Interface to all user preference activity (such as password policy, MRU, and
 * all that jazz).
 * 
 * @see JFacePreferences for a central place for accessing a preference store.
 *  
 * @author Glen Smith
 */
public class UserPreferences {

	private static final Log log = LogFactory.getLog(UserPreferences.class);

	private static UserPreferences prefs;

	private static final String MRU = "mru.";
	private static int MAX_MRU = 5;

	public static final String PROPS_DIR = ".passwordsafe";
	private static final String PREFS_FILENAME = "preferences.properties";
	
    private PreferenceStore prefStore;


	/**
	 * Private constructor enforces singleton.
	 * @throws IOException
	 */
	private UserPreferences() throws IOException {
		loadPreferences();
	}

    /**
     * Returns the name of the preference file.
     * 
     * @return the name of the preferences file.
     */
	public String getPreferencesFilename() {
		String userDir = System.getProperty("user.home") + File.separator + PROPS_DIR + File.separator + PREFS_FILENAME;
		return userDir;
	}

	public IPreferenceStore getPreferenceStore() {
		return prefStore;
	}

    /**
     * Loads preferences from a properties file.
     * 
     * @throws IOException if there are problems loading the preferences file
     */
	private void loadPreferences() throws IOException {
//		props = new Properties();
		String userFile = getPreferencesFilename();
		if (log.isDebugEnabled())
			log.debug("Loading from [" + userFile + "]");
		File prefsFile = new File(userFile);
		if (!prefsFile.exists()) {
			File prefsDir = new File(System.getProperty("user.home") + File.separator + PROPS_DIR);
			if (!prefsDir.exists()) {
				prefsDir.mkdir();
			}
		}

		prefStore = new PreferenceStore(getPreferencesFilename());
		JFacePreferences.setPreferenceStore(prefStore);
		new  JpwPreferenceInitializer().initializeDefaultPreferences();
		
		if (prefsFile.exists()) {
			prefStore.load();
		}
		//TODO: Check what happens if no file exists?
			
		if (log.isDebugEnabled())
			log.debug("Loaded " + prefStore
					+ " preference settings from file");
	}

    /**
     * Saves the preference to a properties file.
     * 
     * @throws IOException if there are problems saving the file
     */
	public void savePreferences() throws IOException {

		String userFile = getPreferencesFilename();
		if (log.isDebugEnabled())
			log.debug("Saving to [" + userFile + "]");
		prefStore.save();
		if (log.isDebugEnabled())
			log.debug("Saved " + prefStore
					+ " preference settings from file");
	}


    /**
     * Sets the name of the most recently opened file.
     * 
     * @param fileName the name of the file
     */
	public void setMostRecentFilename(String fileName) {
        
        if (log.isDebugEnabled())
            log.debug("Setting most recently opened file to: [" + fileName +"]");
        
        try {
			loadPreferences();  // make sure we get the latest
		} catch (IOException ioe) {
			log.error("Couldn't load preferences", ioe);
		} 
		Set<String> newMRU = new LinkedHashSet<String>(13);
        newMRU.add(fileName);
        newMRU.addAll(getMRUFiles());		
		writeOutMruList(newMRU);
	}

	private void writeOutMruList(Collection<String> newMRU) {
		int mruCounter = 0;
		for (Iterator<String> iter = newMRU.iterator(); iter.hasNext()
				&& mruCounter <= MAX_MRU;) {
			final String nextFilename = iter.next();
			prefStore.setValue(MRU + ++mruCounter, nextFilename);
		}
        try {
			savePreferences();
		} catch (IOException e) {
			log.warn("Unable to save preferences file", e);
		}
	}

    /**
     * Returns an array of recently opened filename (most recent to oldest).
     * 
     * @return an array of recently opened filename, not null
     */
	public List<String> getMRUFiles() {

		List<String> allFiles = new LinkedList<String>();
		for (int i = 0; i <= MAX_MRU; i++) {
			String nextFile = prefStore.getString(MRU + i);
			if (nextFile != null && nextFile.length() > 0)
				allFiles.add(nextFile);
		}
		return allFiles;

	}
    
    /**
     * Convenience routine for getting most recently opened file.
     * 
     * @return the filename of the MRU file (or null if there is no MRU file)
     */
    public String getMRUFile() {
        
    	List<String> allMRU = getMRUFiles();
        if (allMRU.size() > 0) {
        	return allMRU.get(0);
        } else {
        	return null;
        }
        
    }
    
    public String getString(String propName) {
    	return prefStore.getString(propName);
    }
    
    public void setString(String propName, String propValue) {
    	prefStore.setValue(propName, propValue);
    }
    
    public boolean getBoolean(String propName) {
    	return prefStore.getBoolean(propName);
    }

    /**
     * Singleton creator.
     * 
     * @return a handle to the UserPreferences object for this user
     */
	public static synchronized UserPreferences getInstance() {
		if (prefs == null) {
			try {
				prefs = new UserPreferences();
			} catch (IOException e) {
				log.error("Couldn't load preferences file.", e);
			}
		}
		return prefs;
	}

	public static synchronized void reload() {
		prefs = null;
	}

	/**
	 * Removes a file from the most recently used list.
	 * 
	 * @param fileName
	 */
	public void removeMRUFile(final String fileName) {
		List<String> files =  getMRUFiles();
		if (files.contains(fileName)) {
			files.remove(files.indexOf(fileName));
			writeOutMruList(files);
		}
	}
	
}