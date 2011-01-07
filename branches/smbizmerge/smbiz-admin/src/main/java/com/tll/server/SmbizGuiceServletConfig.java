/**
 * The Logic Lab
 * @author jpk
 * @since Dec 30, 2010
 */
package com.tll.server;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.tll.SmbizDb4oPersistModule;
import com.tll.config.Config;
import com.tll.config.ConfigRef;

/**
 * @author jpk
 */
public class SmbizGuiceServletConfig extends GuiceServletContextListener {

	private static final Log log = LogFactory.getLog(SmbizGuiceServletConfig.class);

	/**
	 * @author jpk
	 */
	static class SmbizServletModule extends ServletModule {

		@Override
		protected void configureServlets() {

			// NoSecuritySessionContextFilter
			filter("/*").through(NoSecuritySessionContextFilter.class);

			// WebClientCacheFilter
			HashMap<String, String> cparams = new HashMap<String, String>(1);
			cparams.put("oneDayCacheFileExts", ".js .css .gif .jpg .png");
			filter("/*").through(WebClientCacheFilter.class, cparams);
		}

	}

	@Override
	protected Injector getInjector() {

		// load *all* found config properties
		// NOTE: this is presumed to be the first contact point with the config
		// instance!
		final Config config;
		try {
			config = Config.load(new ConfigRef(true));
		}
		catch(final IllegalArgumentException e) {
			throw new Error("Unable to load config: " + e.getMessage(), e);
		}
		log.debug("App config instance loaded");

		log.debug("Creating servlet injector..");
		Injector injector =
				Guice.createInjector(new SmbizDb4oPersistModule(config), new VelocityModule(), new SmbizServletModule());
		log.debug("Servlet injector created");
		return injector;
	}
}
