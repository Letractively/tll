/**
 * The Logic Lab
 * @author jpk
 * Feb 4, 2009
 */
package com.tll.server.rpc.entity;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Injector;
import com.tll.server.IBootstrapHandler;

/**
 * PersistContextBootstrapper
 * @author jpk
 */
public class PersistContextBootstrapper implements IBootstrapHandler {

	private static final Log log = LogFactory.getLog(PersistContextBootstrapper.class);

	@Override
	public void startup(Injector injector, ServletContext servletContext) {
		log.info("Bootstrapping persist services..");

		// store the sole persist context
		final PersistContext context = injector.getInstance(PersistContext.class);
		servletContext.setAttribute(PersistContext.KEY, context);

		// create and store the sole delegate
		final PersistServiceDelegate delegate = injector.getInstance(PersistServiceDelegate.class);
		servletContext.setAttribute(PersistServiceDelegate.KEY, delegate);

		log.info("Persist services bootstrapped.");
	}

	@Override
	public void shutdown(ServletContext servletContext) {
		final PersistContext c =
			(PersistContext) servletContext.getAttribute(PersistContext.KEY);
		if(c != null && c.getPersistCache() != null) {
			log.debug("Shutting down entity service factory..");
			c.getPersistCache().shutdown();
			log.info("Entity service factory shut down.");
		}
	}

}
