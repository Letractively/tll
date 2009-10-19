/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.rpc.listing;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Injector;
import com.tll.server.IBootstrapHandler;


/**
 * ListingServiceBootstapper
 * @author jpk
 */
public class ListingContextBootstrapper implements IBootstrapHandler {

	private static final Log log = LogFactory.getLog(ListingContextBootstrapper.class);

	@Override
	public void startup(Injector injector, ServletContext servletContext) {
		final ListingContext c = injector.getInstance(ListingContext.class);
		servletContext.setAttribute(ListingContext.KEY, c);
		log.info("Listing context bootstrapped.");
	}

	@Override
	public void shutdown(ServletContext servletContext) {
	}
}
