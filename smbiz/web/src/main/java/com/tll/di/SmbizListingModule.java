/**
 * The Logic Lab
 * @author jpk
 * @since Jun 27, 2009
 */
package com.tll.di;

import com.tll.server.listing.IListingDataProviderResolver;
import com.tll.server.listing.IListingSearchTranslator;
import com.tll.server.listing.INamedQueryResolver;
import com.tll.server.listing.ListingModule;
import com.tll.server.rpc.listing.SmbizListingDataProviderResolver;
import com.tll.server.rpc.listing.SmbizListingSearchTranslator;
import com.tll.server.rpc.listing.SmbizNamedQueryResolver;


/**
 * SmbizListingModule
 * @author jpk
 */
public class SmbizListingModule extends ListingModule {

	@Override
	protected Class<? extends IListingDataProviderResolver> getListingDataProviderResolverImplType() {
		return SmbizListingDataProviderResolver.class;
	}

	@Override
	protected Class<? extends IListingSearchTranslator> getListingSearchTranslatorImplType() {
		return SmbizListingSearchTranslator.class;
	}

	@Override
	protected Class<? extends INamedQueryResolver> getNamedQueryResolverImplType() {
		return SmbizNamedQueryResolver.class;
	}
}
