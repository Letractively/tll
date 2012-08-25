/**
 * The Logic Lab
 * @author jpk
 * Feb 4, 2009
 */
package com.tll.server;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

/**
 * AppContextBootstrapper
 * @author jpk
 */
public class AppContextBootstrapper implements IBootstrapHandler {

	private static final Logger log = LoggerFactory.getLogger(AppContextBootstrapper.class);

	@Override
	public void startup(Injector injector, ServletContext servletContext) {
		log.debug("Bootstrapping the app context..");
		final AppContext ac = injector.getInstance(AppContext.class);
		servletContext.setAttribute(AppContext.KEY, ac);
	}

	@Override
	public void shutdown(ServletContext servletContext) {
		// no-op
	}

}
