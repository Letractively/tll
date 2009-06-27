/**
 * The Logic Lab
 * @author jpk
 * @since Jun 25, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.server.rpc.listing.IListingDataProviderResolver;
import com.tll.server.rpc.listing.IListingSearchTranslator;
import com.tll.server.rpc.listing.INamedQueryResolver;
import com.tll.server.rpc.listing.test.TestListingDataProviderResolver;
import com.tll.server.rpc.listing.test.TestListingSearchTranslator;
import com.tll.server.rpc.listing.test.TestNamedQueryResolver;


/**
 * TestListingModule
 * @author jpk
 */
public class TestListingModule extends ListingModule {

	@Override
	protected void bindListingDataProviderResolver() {
		bind(IListingDataProviderResolver.class).to(TestListingDataProviderResolver.class).in(Scopes.SINGLETON);
	}

	@Override
	protected void bindListingSearchTranslator() {
		bind(IListingSearchTranslator.class).to(TestListingSearchTranslator.class).in(Scopes.SINGLETON);
	}

	@Override
	protected void bindNamedQueryResolver() {
		bind(INamedQueryResolver.class).to(TestNamedQueryResolver.class).in(Scopes.SINGLETON);
	}
}
