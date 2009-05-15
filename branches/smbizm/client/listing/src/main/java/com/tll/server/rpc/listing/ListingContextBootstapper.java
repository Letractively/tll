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
import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.server.IBootstrapHandler;
import com.tll.server.rpc.entity.MEntityContext;


/**
 * ListingServiceBootstapper
 * @author jpk
 */
public class ListingContextBootstapper implements IBootstrapHandler {

	/**
	 * ConfigKeys - Configuration property keys for the app context.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		LISTING_DATA_PROVIDER_RESOLVER_CLASSNAME("server.listingDataProviderResolver.classname");

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

	private static final Log log = LogFactory.getLog(ListingContextBootstapper.class);

	@Override
	public void startup(Injector injector, ServletContext servletContext, Config config) {
		IListingDataProviderResolver resolver;
		try {
			// instantiate the mentity service resolver
			final String cn = config.getString(ConfigKeys.LISTING_DATA_PROVIDER_RESOLVER_CLASSNAME.getKey());
			resolver = (IListingDataProviderResolver) Class.forName(cn).newInstance();
		}
		catch(final InstantiationException e) {
			throw new Error(e.getMessage(), e);
		}
		catch(final IllegalAccessException e) {
			throw new Error(e.getMessage(), e);
		}
		catch(final ClassNotFoundException e) {
			throw new Error(e.getMessage(), e);
		}

		final ListingContext c = new ListingContext(resolver);
		servletContext.setAttribute(MEntityContext.KEY, c);
		log.info("Listing context bootstrapped.");
	}

	@Override
	public void shutdown(ServletContext servletContext) {
	}
}
