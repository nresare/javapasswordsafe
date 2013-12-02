package org.pwsafe.passwordsafeswt.model.comparator;

import org.pwsafe.lib.datastore.PwsEntryBean;

/**
 * Created by IntelliJ IDEA.
 * User: tim
 * Date: 27-Feb-2010
 * Time: 14:11:50
 * To change this template use File | Settings | File Templates.
 */
public interface FindMatcher {

    public boolean matches(final String searchString, final PwsEntryBean entry);
}
