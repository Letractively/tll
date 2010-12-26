/**
 * The Logic Lab
 * @author jpk
 * @since Jun 27, 2009
 */
package com.tll.di;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.tll.model.IEntityTypeResolver;
import com.tll.server.marshal.IMarshalOptionsResolver;

/**
 * MarshalModule
 * @author jpk
 */
public abstract class MarshalModule extends AbstractModule {

	/**
	 * Responsible for binding an {@link IMarshalOptionsResolver} implementation.
	 */
	protected abstract Class<? extends IMarshalOptionsResolver> getMarshalOptionsResolverImplType();

	/**
	 * Responsible for binding an {@link IEntityTypeResolver} implementation.
	 */
	protected abstract Class<? extends IEntityTypeResolver> getEntityTypeResolverImplType();

	@Override
	protected final void configure() {
		// IEntityTypeResolver
		bind(IEntityTypeResolver.class).to(getEntityTypeResolverImplType()).in(Scopes.SINGLETON);

		// IMarshalOptionsResolver
		bind(IMarshalOptionsResolver.class).to(getMarshalOptionsResolverImplType()).in(Scopes.SINGLETON);
	}
}
