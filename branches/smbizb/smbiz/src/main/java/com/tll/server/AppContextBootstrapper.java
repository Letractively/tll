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
import com.tll.config.Config;
import com.tll.config.IConfigKey;

/**
 * AppContextBootstrapper
 * @author jpk
 */
public class AppContextBootstrapper implements IBootstrapHandler {

	/**
	 * ConfigKeys - Configuration property keys for the app context.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		ENVIRONMENT_PARAM("environment"),
		DEBUG_PARAM("debug");

		private final String key;

		/**
		 * Constructor
		 * @param key
		 */
		private ConfigKeys(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}
	
	private static final Log log = LogFactory.getLog(AppContextBootstrapper.class);

	@Override
	public void startup(Injector injector, ServletContext servletContext) {
		log.debug("Bootstrapping the app context..");
		final boolean debug = Config.instance().getBoolean(ConfigKeys.DEBUG_PARAM.getKey());
		final String environment = Config.instance().getString(ConfigKeys.ENVIRONMENT_PARAM.getKey());
		final AppContext c = new AppContext(debug, environment);
		servletContext.setAttribute(AppContext.KEY, c);
	}

	@Override
	public void shutdown(ServletContext servletContext) {
		// no-op
	}

}
