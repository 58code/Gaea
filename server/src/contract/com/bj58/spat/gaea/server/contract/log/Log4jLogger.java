/*
 *  Copyright Beijing 58 Information Technology Co.,Ltd.
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package com.bj58.spat.gaea.server.contract.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.bj58.spat.gaea.server.contract.context.GaeaContext;

/**
 * This class is will log messages to the log file using log4j logging framework
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public final class Log4jLogger implements ILog {

	/** the log object to log to */
	private transient Logger logger = null;
	// ------------------------------------------------------------- Attributes

	/** The fully qualified name of the Log4JLogger class. */
	private static final String FQCN = Log4jLogger.class.getName();

	/**
	 * Constructor for creating a logger object using jdk 1.4 or higher logging.
	 * 
	 * @param cls
	 *            the class which wants to log
	 */
	public Log4jLogger(Class<?> cls) {
		logger = Logger.getLogger(cls);
	}
	
	/**
	 * 
	 * @return
	 */
	private String getLogMsg(String msg) {
		StringBuilder sbLog = new StringBuilder();
		sbLog.append(msg);
		GaeaContext context = GaeaContext.getFromThreadLocal();
		if(context != null) {
			sbLog.append("--");
			sbLog.append("remoteIP:");
			sbLog.append(context.getChannel().getRemoteIP());
			sbLog.append("--remotePort:");
			sbLog.append(context.getChannel().getRemotePort());
		}
		
		return sbLog.toString();
	}



	/**
	 * Logging a fine message
	 * 
	 * @param message
	 *            the message to log.
	 */
	public void fine(String message) {
		logger.log(FQCN, Level.DEBUG, getLogMsg(message), null);
	}

	/**
	 * Logging a config message.
	 * 
	 * @param message
	 *            the message to log
	 */
	public void config(String message) {
		logger.log(FQCN, Level.DEBUG, getLogMsg(message), null);
	}

	/**
	 * Logging a info message.
	 * 
	 * @param message
	 *            the message to log
	 */
	public void info(String message) {
		logger.log(FQCN, Level.INFO, getLogMsg(message), null);
	}

	/**
	 * Logging a warning message.
	 * 
	 * @param message
	 *            the message to log
	 */
	public void warning(String message) {
		logger.log(FQCN, Level.WARN, getLogMsg(message), null);
	}

	// ****************************************************
	// * The methods from log4j also implemented below *
	// ****************************************************

	/**
	 * Logging a debug message.
	 * 
	 * @param message
	 *            the message to log
	 */
	public void debug(String message) {
		logger.log(FQCN, Level.DEBUG, getLogMsg(message), null);
	}

	/**
	 * Logging a fatal message with the throwable message.
	 * 
	 * @param message
	 *            the message to log
	 * @param t
	 *            the exception
	 */
	public void fatal(String message, Throwable t) {
		logger.log(FQCN, Level.FATAL, getLogMsg(message), t);
	}

	/**
	 * Logging a debug message with the throwable message.
	 * 
	 * @param message
	 *            the message to log
	 * @param t
	 *            the exception
	 */
	public void debug(String message, Throwable t) {
		logger.log(FQCN, Level.DEBUG, getLogMsg(message), t);
	}

	/**
	 * Logging an info message with the throwable message.
	 * 
	 * @param message
	 *            the message to log
	 * @param t
	 *            the exception
	 */
	public void info(String message, Throwable t) {
		logger.log(FQCN, Level.INFO, getLogMsg(message), t);
	}

	/**
	 * Logging a warning message.
	 * 
	 * @param message
	 *            the message to log
	 */
	public void warn(String message) {
		logger.log(FQCN, Level.WARN, getLogMsg(message), null);
	}

	/**
	 * Logging a warning message with the throwable message.
	 * 
	 * @param message
	 *            the message to log
	 * @param t
	 *            the exception
	 */
	public void warn(String message, Throwable t) {
		logger.log(FQCN, Level.WARN, getLogMsg(message), t);
	}

	/**
	 * Logging an error message.
	 * 
	 * @param message
	 *            the message to log
	 */
	public void error(String message) {
		logger.log(FQCN, Level.ERROR, getLogMsg(message), null);
	}

	/**
	 * Logging an error message with the throwable message.
	 * 
	 * @param message
	 *            the message to log
	 * @param t
	 *            the exception
	 */
	public void error(String message, Throwable t) {
		logger.log(FQCN, Level.ERROR, getLogMsg(message), t);
	}

	public void error(Throwable e) {
		logger.log(FQCN, Level.ERROR, getLogMsg(""), e);
	}

	/**
	 * Logging a fatal message.
	 * 
	 * @param message
	 *            the message to log
	 */
	public void fatal(String message) {
		logger.log(FQCN, Level.FATAL, getLogMsg(message), null);
	}

}
