/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * Convenience class for access build version numbers for display
 * in the ui. Depends on a build.version properties file in the
 * classpath.
 * 
 * @author Glen Smith
 */
public class VersionInfo {

	private static final Log log = LogFactory.getLog(VersionInfo.class);
	
	/**
	 * Returns the version string (major.minor) for display in the UI.
	 * 
	 * @return a version string
	 */
	public static String getVersion() {

		String versionInfo = VersionInfo.class.getPackage().getImplementationVersion();
		if (versionInfo == null) 
			versionInfo = "?";
		return versionInfo;
		
	}
	

}
