/*
 * Copyright (c) 2008-2009 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
// Created on 22.06.2007
package org.pwsafe.passwordsafeswt.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.program.Program;

/**
 * @author shamilbi
 */
public class IOUtils
{

    private static final Log LOG = LogFactory.getLog(IOUtils.class);

    public static void closeQuietly(InputStream in)
    {
        if (in == null)
        {
            return;
        }

        try
        {
            in.close();
        } catch (IOException e)
        {
            LOG.warn("InputStream.close() error", e);
        }
    }

    public static void close(OutputStream out) throws IOException
    {
        if (out == null)
        {
            return;
        }
        out.close();
    }

    public static void closeQuietly(OutputStream out)
    {
        try
        {
            close(out);
        } catch (IOException e)
        {
            LOG.warn("OutputStream.close() error", e);
        }
    }

    /**
     * Use swt to open a browser. If this does not work, 
     * and Java 6 is available, try to open a browser using
     * java.awt.Desktop class.
     * 
     * @param anUrl
     */
    public static void openBrowser (String anUrl) {
    	if (anUrl == null) {
    		return;
    	}
    	if (! anUrl.contains("://")) {
    		anUrl = "http://" + anUrl;
    	}
        try {
        	final boolean success = Program.launch(anUrl);
        	if (! success) {
        		LOG.warn("failed to launch a web browser for URL " + anUrl);
        	}
        	if (success)
        		return;
        } catch (Exception ioe) {
    		LOG.error("failed to launch a web browser for URL " + anUrl, ioe);
        }

		try {
			Class desktopClass = Class.forName("java.awt.Desktop"); //$NON-NLS-1$
			Method getDesktop = desktopClass.getMethod("getDesktop", null); //$NON-NLS-1$
			Object desktopObj = getDesktop.invoke(desktopClass, null);
			Method browseMethod = desktopClass.getMethod("browse", URI.class); //$NON-NLS-1$
			browseMethod.invoke(desktopObj, new URI(anUrl));
		} catch (Exception anEx) {
			LOG.warn("Can't open " + anUrl +  ", because of Exception " + anEx); //$NON-NLS-1$ //$NON-NLS-2$
		}

    }
    
    private IOUtils()
    {
        // only static methods
    }

}