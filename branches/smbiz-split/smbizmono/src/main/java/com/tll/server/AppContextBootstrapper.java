/**
 * The Logic Lab
 * @author jpk
 * Feb 4, 2009
 */
package com.tll.server;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Injector;

/**
 * AppContextBootstrapper
 * @author jpk
 */
public class AppContextBootstrapper implements IBootstrapHandler {

	private static final Log log = LogFactory.getLog(AppContextBootstrapper.class);

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
