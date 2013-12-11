/*
 * $Id$
 * 
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib;

import org.apache.commons.logging.LogFactory;

/**
 * This class provides logging facilities using commons logging.
 * 
 * @author Kevin Preece
 */
public class Log {
	private int debugLevel;
	private final org.apache.commons.logging.Log logger;

	static {
		// DOMConfigurator.configure( "log-config.xml" );
	}

	private Log(final String name) {
		// logger = Logger.getLogger( name );
		logger = LogFactory.getLog(name);
		setDebugLevel(3);
	}

	/**
	 * Returns an instance of <code>Log</code> for the logger named
	 * <code>name</code>.
	 * 
	 * @param name the logger name.
	 * 
	 * @return An <code>Log</code> instance.
	 */
	public static Log getInstance(final String name) {
		return new Log(name);
	}

	/**
	 * Returns an instance of <code>Log</code> for the logger named
	 * <code>Class.name</code>.
	 * 
	 * @param name the logger name.
	 * 
	 * @return An <code>Log</code> instance.
	 */
	public static Log getInstance(final Class aClass) {
		return new Log(aClass.getName());
	}

	/**
	 * Writes a message at debug level 1
	 * 
	 * @param msg the message to issue.
	 */
	public void debug1(final String msg) {
		if (isDebug1Enabled()) {
			logger.debug(msg);
		}
	}

	/**
	 * Writes a message at debug level 2
	 * 
	 * @param msg the message to issue.
	 */
	public void debug2(final String msg) {
		if (isDebug2Enabled()) {
			logger.debug(msg);
		}
	}

	/**
	 * Writes a message at debug level 3
	 * 
	 * @param msg the message to issue.
	 */
	public void debug3(final String msg) {
		if (isDebug3Enabled()) {
			logger.debug(msg);
		}
	}

	/**
	 * Writes a message at debug level 4
	 * 
	 * @param msg the message to issue.
	 */
	public void debug4(final String msg) {
		if (isDebug4Enabled()) {
			logger.debug(msg);
		}
	}

	/**
	 * Writes a message at debug level 5
	 * 
	 * @param msg the message to issue.
	 */
	public void debug5(final String msg) {
		if (isDebug5Enabled()) {
			logger.debug(msg);
		}
	}

	/**
	 * Logs entry to a method.
	 * 
	 * @param method the method name.
	 */
	public void enterMethod(String method) {
		if (logger.isDebugEnabled()) {
			if (!method.endsWith(")")) {
				method = method + "()";
			}
			logger.debug("Entering method " + method);
		}
	}

	/**
	 * Writes a message at error level
	 * 
	 * @param msg the message to issue.
	 */
	public void error(final String msg) {
		logger.error(msg);
	}

	/**
	 * Writes a message at error level along with details of the exception
	 * 
	 * @param msg the message to issue.
	 * @param except the exeption to be logged.
	 */
	public void error(final String msg, final Throwable except) {
		logger.error(msg, except);
	}

	/**
	 * Logs the exception at a level of error.
	 * 
	 * @param except the <code>Exception</code> to log.
	 */
	public void error(final Throwable except) {
		logger.error("An Exception has occurred", except);
	}

	/**
	 * Returns the current debug level.
	 * 
	 * @return Returns the debugLevel.
	 */
	public int getDebugLevel() {
		return debugLevel;
	}

	/**
	 * Writes a message at info level
	 * 
	 * @param msg the message to issue.
	 */
	public void info(final String msg) {
		logger.info(msg);
	}

	/**
	 * Returns <code>true</code> if debuuging at level 1 is enabled,
	 * <code>false</code> if it isn't.
	 * 
	 * @return <code>true</code> if debuuging at level 1 is enabled,
	 *         <code>false</code> if it isn't.
	 */
	public boolean isDebug1Enabled() {
		return logger.isDebugEnabled();
	}

	/**
	 * Returns <code>true</code> if debuuging at level 2 is enabled,
	 * <code>false</code> if it isn't.
	 * 
	 * @return <code>true</code> if debuuging at level 2 is enabled,
	 *         <code>false</code> if it isn't.
	 */
	public boolean isDebug2Enabled() {
		return logger.isDebugEnabled() && (debugLevel >= 2);
	}

	/**
	 * Returns <code>true</code> if debuuging at level 3 is enabled,
	 * <code>false</code> if it isn't.
	 * 
	 * @return <code>true</code> if debuuging at level 3 is enabled,
	 *         <code>false</code> if it isn't.
	 */
	public boolean isDebug3Enabled() {
		return logger.isDebugEnabled() && (debugLevel >= 3);
	}

	/**
	 * Returns <code>true</code> if debuuging at level 4 is enabled,
	 * <code>false</code> if it isn't.
	 * 
	 * @return <code>true</code> if debuuging at level 4 is enabled,
	 *         <code>false</code> if it isn't.
	 */
	public boolean isDebug4Enabled() {
		return logger.isDebugEnabled() && (debugLevel >= 4);
	}

	/**
	 * Returns <code>true</code> if debuuging at level 5 is enabled,
	 * <code>false</code> if it isn't.
	 * 
	 * @return <code>true</code> if debuuging at level 5 is enabled,
	 *         <code>false</code> if it isn't.
	 */
	public boolean isDebug5Enabled() {
		return logger.isDebugEnabled() && (debugLevel >= 5);
	}

	/**
	 * Logs exit from a method.
	 * 
	 * @param method the method name.
	 */
	public void leaveMethod(String method) {
		if (logger.isDebugEnabled()) {
			if (!method.endsWith(")")) {
				method = method + "()";
			}
			logger.debug("Leaving method " + method);
		}
	}

	/**
	 * Sets the debug level.
	 * 
	 * @param debugLevel The debugLevel to set.
	 */
	public void setDebugLevel(int debugLevel) {
		if (debugLevel < 1) {
			debugLevel = 1;
		} else if (debugLevel > 5) {
			debugLevel = 5;
		}
		debugLevel = debugLevel;
	}

	/**
	 * Logs a message at the warning level.
	 * 
	 * @param msg the message to issue.
	 */
	public void warn(final String msg) {
		logger.warn(msg);
	}

}
