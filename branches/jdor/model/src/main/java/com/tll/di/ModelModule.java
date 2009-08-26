/**
 * The Logic Lab
 * @author jpk
 * Jan 19, 2009
 */
package com.tll.di;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.model.EntityFactory;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityFactory;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.model.schema.ISchemaInfo;
import com.tll.model.schema.SchemaInfo;

/**
 * ModelModule
 * @author jpk
 */
public abstract class ModelModule extends AbstractModule {

	/**
	 * Binds an {@link IPrimaryKeyGenerator} impl
	 */
	protected abstract void bindPrimaryKeyGenerator();

	/**
	 * Responsible for binding an {@link IEntityAssembler} implmementation.
	 */
	protected abstract void bindEntityAssembler();

	@Override
	protected void configure() {
		// IPrimaryKeyGenerator
		bindPrimaryKeyGenerator();

		// IEntityFactory
		bind(IEntityFactory.class).to(EntityFactory.class).in(Scopes.SINGLETON);

		// ISchemaInfo
		bind(ISchemaInfo.class).to(SchemaInfo.class).in(Scopes.SINGLETON);

		// IEntityAssembler impl
		bindEntityAssembler();

		// ValidationFactory
		bind(ValidatorFactory.class).toProvider(new Provider<ValidatorFactory>() {

			@Override
			public ValidatorFactory get() {
				return Validation.buildDefaultValidatorFactory();
			}
		}).in(Scopes.SINGLETON);
	}
}
