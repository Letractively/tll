/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.tll.server.rpc.listing.IListingDataProviderResolver;
import com.tll.server.rpc.listing.IListingSearchTranslator;
import com.tll.server.rpc.listing.INamedQueryResolver;


/**
 * ListingModule
 * @author jpk
 */
public abstract class ListingModule extends AbstractModule {

	private static final Log log = LogFactory.getLog(ListingModule.class);

	/**
	 * Responsible for binding an {@link IListingDataProviderResolver}
	 * implementation.
	 */
	protected abstract void bindListingDataProviderResolver();

	/**
	 * Responsible for binding an {@link INamedQueryResolver} implementation.
	 */
	protected abstract void bindNamedQueryResolver();

	/**
	 * Responsible for binding an {@link IListingSearchTranslator} implementation.
	 */
	protected abstract void bindListingSearchTranslator();

	@Override
	protected final void configure() {
		log.info("Employing Listing module");

		// IEntityTypeResolver
		bindListingDataProviderResolver();

		// INamedQueryResolver
		bindNamedQueryResolver();

		// IListingSearchTranslator
		bindListingSearchTranslator();
	}

}
