package org.pwsafe.passwordsafeswt.model.comparator;

import org.pwsafe.lib.datastore.PwsEntryBean;

/**
 * @author timmydog
 */
public class TitleSubStringMatcher implements FindMatcher {
    public boolean matches(final String searchString, final PwsEntryBean entry) {

        return entry.getTitle().toLowerCase().contains(searchString.toLowerCase());
    }
}
