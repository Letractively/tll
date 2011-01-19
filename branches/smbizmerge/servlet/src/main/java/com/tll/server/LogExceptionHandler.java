/**
 * The Logic Lab
 * @author jpk Feb 11, 2009
 */
package com.tll.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LogExceptionHandler - Logs exceptions.
 * @author jpk
 */
public class LogExceptionHandler implements IExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(LogExceptionHandler.class);

	@Override
	public void handleException(final Throwable t) {
		log.error(t.getMessage(), t);
	}
}
