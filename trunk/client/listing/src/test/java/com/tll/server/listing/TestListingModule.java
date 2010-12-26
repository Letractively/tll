/**
 * The Logic Lab
 * @author jpk
 * @since Jun 25, 2009
 */
package com.tll.server.listing;

import com.tll.server.listing.IListingDataProviderResolver;
import com.tll.server.listing.IListingSearchTranslator;
import com.tll.server.listing.ListingModule;
import com.tll.server.listing.test.TestListingDataProviderResolver;
import com.tll.server.listing.test.TestListingSearchTranslator;


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
}
