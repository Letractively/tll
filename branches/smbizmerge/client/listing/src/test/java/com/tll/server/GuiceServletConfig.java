/**
 * The Logic Lab
 * @author jpk
 * @since Dec 30, 2010
 */
package com.tll.server;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.tll.server.listing.ListingRpcServlet;
import com.tll.server.listing.test.TestListingModule;
import com.tll.server.test.MockSessionFilter;

/**
 * @author jpk
 */
public class GuiceServletConfig extends GuiceServletContextListener {

	private static final Logger log = LoggerFactory.getLogger(GuiceServletConfig.class);
	
	static class ListingServletModule extends ServletModule {

		@Override
		protected void configureServlets() {

			// mock session filter
			filter("/*").through(MockSessionFilter.class);

			// WebClientCacheFilter
			HashMap<String, String> cparams = new HashMap<String, String>(1);
			cparams.put("oneDayCacheFileExts", ".js .css .gif .jpg .png");
			filter("/*").through(WebClientCacheFilter.class, cparams);
			
			// rpc listing service
			serve("/uitests/rpc/listing").with(ListingRpcServlet.class);
		}

	}

	@Override
	protected Injector getInjector() {

//		final Config config;
//		try {
//			config = Config.load(new ConfigRef(true));
//		}
//		catch(final IllegalArgumentException e) {
//			throw new Error("Unable to load config: " + e.getMessage(), e);
//		}

		log.debug("Creating servlet injector..");
		Injector injector =
				Guice.createInjector(new TestListingModule(), new ListingServletModule());
		log.debug("Servlet injector created");
		return injector;
	}
}
