/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.listing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.tll.config.Config;

/**
 * ListingModule
 * @author jpk
 */
public abstract class ListingModule extends AbstractModule {

	private static final Log log = LogFactory.getLog(ListingModule.class);

	protected Config config;

	/**
	 * Responsible for binding an {@link IListingDataProviderResolver}
	 * implementation.
	 */
	protected abstract Class<? extends IListingDataProviderResolver> getListingDataProviderResolverImplType();

	/**
	 * Responsible for binding an {@link IListingSearchTranslator} implementation.
	 */
	protected abstract Class<? extends IListingSearchTranslator> getListingSearchTranslatorImplType();

	@Override
	protected final void configure() {
		log.info("Employing Listing module");

		// IListingDataProviderResolver
		bind(IListingDataProviderResolver.class).to(getListingDataProviderResolverImplType()).in(Scopes.SINGLETON);

		// IListingSearchTranslator
		bind(IListingSearchTranslator.class).to(getListingSearchTranslatorImplType()).in(Scopes.SINGLETON);
	}

}
