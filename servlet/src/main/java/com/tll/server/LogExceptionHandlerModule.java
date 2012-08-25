/**
 * The Logic Lab
 * @author jpk
 * @since Apr 28, 2009
 */
package com.tll.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * EmailExceptionHandlerModule
 * @author jpk
 */
public class LogExceptionHandlerModule extends AbstractModule {

	private static final Logger log = LoggerFactory.getLogger(LogExceptionHandlerModule.class);

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
