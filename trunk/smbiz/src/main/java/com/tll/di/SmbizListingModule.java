/**
 * The Logic Lab
 * @author jpk
 * @since Jun 27, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.server.rpc.listing.IListingDataProviderResolver;
import com.tll.server.rpc.listing.IListingSearchTranslator;
import com.tll.server.rpc.listing.INamedQueryResolver;
import com.tll.server.rpc.listing.SmbizListingDataProviderResolver;
import com.tll.server.rpc.listing.SmbizListingSearchTranslator;
import com.tll.server.rpc.listing.SmbizNamedQueryResolver;


/**
 * SmbizListingModule
 * @author jpk
 */
public class SmbizListingModule extends ListingModule {

	@Override
	protected void bindListingDataProviderResolver() {
		bind(IListingDataProviderResolver.class).to(SmbizListingDataProviderResolver.class).in(Scopes.SINGLETON);
	}

	@Override
	protected void bindListingSearchTranslator() {
		bind(IListingSearchTranslator.class).to(SmbizListingSearchTranslator.class).in(Scopes.SINGLETON);
	}

	@Override
	protected void bindNamedQueryResolver() {
		bind(INamedQueryResolver.class).to(SmbizNamedQueryResolver.class).in(Scopes.SINGLETON);
	}
}
