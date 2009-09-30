/**
 * The Logic Lab
 * @author jpk
 * @since Jun 25, 2009
 */
package com.tll.di;

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
	protected Class<? extends IListingDataProviderResolver> getListingDataProviderResolverImplType() {
		return TestListingDataProviderResolver.class;
	}

	@Override
	protected Class<? extends IListingSearchTranslator> getListingSearchTranslatorImplType() {
		return TestListingSearchTranslator.class;
	}

	@Override
	protected Class<? extends INamedQueryResolver> getNamedQueryResolverImplType() {
		return TestNamedQueryResolver.class;
	}
}
