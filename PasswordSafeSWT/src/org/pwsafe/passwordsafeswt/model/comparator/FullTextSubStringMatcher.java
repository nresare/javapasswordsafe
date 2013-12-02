package org.pwsafe.passwordsafeswt.model.comparator;

import org.pwsafe.lib.datastore.PwsEntryBean;

/**
 * @author timmydog
 */
public class FullTextSubStringMatcher implements FindMatcher {
    public boolean matches(final String searchString, final PwsEntryBean entry) {
        boolean matches = false;
        String lowerCaseSearchString = searchString.toLowerCase();

        for (String fieldValue : entry.getFields().values()) {
            if (fieldValue == null)
                continue;
            matches = fieldValue.toLowerCase().contains(lowerCaseSearchString);
            if (matches)
                break;
        }

        return matches;
    }
}