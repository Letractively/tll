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
import com.tll.model.schema.ISchemaInfo;
import com.tll.model.schema.SchemaInfo;

/**
 * AbstractModelModule
 * @author jpk
 */
public abstract class AbstractModelModule extends AbstractModule {

	/**
	 * Responsible for binding an {@link IEntityAssembler} impl type.
	 */
	protected abstract Class<? extends IEntityAssembler> getEntityAssemblerImplType();

	//protected abstract Class<? extends IPrimaryKeyGenerator> getPrimaryKeyGeneratorImplType();

	@Override
	protected final void configure() {
		// IPrimaryKeyGenerator
		//bind(IPrimaryKeyGenerator.class).to(getPrimaryKeyGeneratorImplType()).in(Scopes.SINGLETON);

		// IEntityFactory
		bind(IEntityFactory.class).to(EntityFactory.class).in(Scopes.SINGLETON);

		// ISchemaInfo
		bind(ISchemaInfo.class).to(SchemaInfo.class).in(Scopes.SINGLETON);

		// IEntityAssembler impl
		bind(IEntityAssembler.class).to(getEntityAssemblerImplType()).in(Scopes.SINGLETON);

		// ValidationFactory
		bind(ValidatorFactory.class).toProvider(new Provider<ValidatorFactory>() {

			@Override
			public ValidatorFactory get() {
				return Validation.buildDefaultValidatorFactory();
			}
		}).in(Scopes.SINGLETON);
	}
}
