/*
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.pwsafe.passwordsafeswt.util.IOUtils;

/**
 * Launches a browser to the passwordsafe website.
 * 
 * @author Glen Smith
 */
public class VisitPasswordSafeWebsiteAction extends Action {

	private static final Log log = LogFactory.getLog(VisitPasswordSafeWebsiteAction.class);

	public VisitPasswordSafeWebsiteAction() {
		super(Messages.getString("VisitPwWebsiteAction.Label")); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		new Thread() {
			@Override
			public void run() {

				final String url = "http://jpwsafe.sf.net/"; //$NON-NLS-1$
				IOUtils.openBrowser(url);
			}
		}.start();
	}

}