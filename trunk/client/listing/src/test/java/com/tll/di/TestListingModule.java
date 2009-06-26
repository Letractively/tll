/**
 * The Logic Lab
 * @author jpk
 * @since Jun 25, 2009
 */
package com.tll.di;

import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.common.model.IEntityType;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.server.rpc.entity.IMarshalOptionsResolver;
import com.tll.server.rpc.entity.test.TestEntityTypeResolver;
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
	protected void bindEntityTypeResolver() {
		bind(IEntityTypeResolver.class).to(TestEntityTypeResolver.class).in(Scopes.SINGLETON);
	}

	@Override
	protected void bindMarshalOptionsResolver() {
		bind(IMarshalOptionsResolver.class).toProvider(new Provider<IMarshalOptionsResolver>() {

			@Override
			public IMarshalOptionsResolver get() {
				return new IMarshalOptionsResolver() {

					@Override
					public MarshalOptions resolve(IEntityType entityType) throws IllegalArgumentException {
						return MarshalOptions.NO_REFERENCES;
					}
				};
			}
		}).in(Scopes.SINGLETON);
	}

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
