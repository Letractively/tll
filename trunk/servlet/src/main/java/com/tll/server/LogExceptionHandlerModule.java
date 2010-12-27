/**
 * The Logic Lab
 * @author jpk
 * @since Apr 28, 2009
 */
package com.tll.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * EmailExceptionHandlerModule
 * @author jpk
 */
public class LogExceptionHandlerModule extends AbstractModule {

	private static final Log log = LogFactory.getLog(LogExceptionHandlerModule.class);

	/**
	 * Constructor
	 */
	public LogExceptionHandlerModule() {
		super();
	}

	@Override
	protected void configure() {
		log.info("Employing log exception handler.");
		bind(IExceptionHandler.class).to(LogExceptionHandler.class).in(Scopes.SINGLETON);
	}

}
