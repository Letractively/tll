/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.config.IConfigKey;
import com.tll.server.rpc.listing.IListingDataProviderResolver;


/**
 * ListingModule
 * @author jpk
 */
public class ListingModule extends AbstractModule implements IConfigAware {

	private static final Log log = LogFactory.getLog(ListingModule.class);

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

	Config config;

	/**
	 * Constructor
	 */
	public ListingModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public ListingModule(Config config) {
		super();
		setConfig(config);
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		if(config == null) throw new IllegalStateException("No config instance specified.");
		log.info("Employing Listing module");

		String cn;

		// IEntityTypeResolver
		cn = config.getString(ConfigKeys.LISTING_DATA_PROVIDER_RESOLVER_CLASSNAME.getKey());
		if(cn == null) {
			throw new IllegalStateException("No listing data provider resolver class name specified in the configuration");
		}
		Class<? extends IListingDataProviderResolver> etrClass;
		try {
			etrClass = (Class<? extends IListingDataProviderResolver>) Class.forName(cn);
		}
		catch(final ClassNotFoundException e) {
			throw new IllegalStateException("No listing data provider found for name: " + cn);
		}
		bind(IListingDataProviderResolver.class).to(etrClass).in(Scopes.SINGLETON);
	}

}
