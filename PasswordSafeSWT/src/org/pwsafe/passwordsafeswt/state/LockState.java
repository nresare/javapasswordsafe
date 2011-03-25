/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.state;

import java.util.Observable;

/**
* An observable object to keep the locked state for the application, which allows child windows to be
* notified on (un)lock events.
*
* @author Tim Hughes
*
*/
public class LockState extends Observable {
    volatile boolean locked = false;

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        if(this.locked != locked) {
            this.locked = locked;
            setChanged();
            notifyObservers(locked);
        }
    }

}
