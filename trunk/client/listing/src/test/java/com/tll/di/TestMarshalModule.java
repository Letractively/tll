/**
 * The Logic Lab
 * @author jpk
 * @since Aug 30, 2009
 */
package com.tll.di;

import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.common.model.IEntityType;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.server.rpc.entity.IMarshalOptionsResolver;
import com.tll.server.rpc.entity.test.TestEntityTypeResolver;


/**
 * TestMarshalModule
 * @author jpk
 */
public class TestMarshalModule extends MarshalModule {

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
	protected void bindEntityTypeResolver() {
		bind(IEntityTypeResolver.class).to(TestEntityTypeResolver.class).in(Scopes.SINGLETON);
	}

}
