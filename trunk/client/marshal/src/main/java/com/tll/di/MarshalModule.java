/**
 * The Logic Lab
 * @author jpk
 * @since Jun 27, 2009
 */
package com.tll.di;

import com.google.inject.AbstractModule;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.server.rpc.entity.IMarshalOptionsResolver;


/**
 * MarshalModule
 * @author jpk
 */
public abstract class MarshalModule extends AbstractModule {

	/**
	 * Responsible for binding an {@link IMarshalOptionsResolver} implementation.
	 */
	protected abstract void bindMarshalOptionsResolver();

	/**
	 * Responsible for binding an {@link IEntityTypeResolver} implementation.
	 */
	protected abstract void bindEntityTypeResolver();

	@Override
	protected final void configure() {
		// IEntityTypeResolver
		bindEntityTypeResolver();

		// IMarshalOptionsResolver
		bindMarshalOptionsResolver();
	}
}
