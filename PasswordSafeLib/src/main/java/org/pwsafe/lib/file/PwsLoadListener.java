/*
 * $Id: PwsIntegerField.java 488 2013-12-11 23:22:25Z roxon $
 * 
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.file;

/**
 * Defines a listener for freshly loaded records, allowing to
 * process them before memory encryption.
 * 
 * @author roxon
 *
 */
public interface PwsLoadListener {

	/**
	 * Forwards a newly loaded record.
	 * 
	 * @param aRecord the newly loaded record.
	 */
	void loaded(final PwsRecord aRecord);

}
