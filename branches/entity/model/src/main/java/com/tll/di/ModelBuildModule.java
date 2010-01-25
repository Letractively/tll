/**
 * The Logic Lab
 * @author jpk
 * @since Jan 17, 2010
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityFactory;

/**
 * ModelBuildModule - Wires up {@link IEntityFactory} and
 * {@link IEntityAssembler} implementations.
 * <p>
 * We can't stick all model declared assets in one module since
 * {@link IEntityFactory} implementations are datastore dependent.
 * @author jpk
 */
public class ModelBuildModule extends AbstractModule {

	private static final Log log = LogFactory.getLog(ModelBuildModule.class);

	private final Class<? extends IEntityFactory<?>> entityFactoryImplType;

	private final Class<? extends IEntityAssembler> entityAssemlerImplType;

	/**
	 * Constructor
	 * @param entityFactoryImplType
	 * @param entityAssemlerImplType Required entity assembler impl type
	 */
	public ModelBuildModule(Class<? extends IEntityFactory<?>> entityFactoryImplType,
			Class<? extends IEntityAssembler> entityAssemlerImplType) {
		super();
		if(entityFactoryImplType == null)
			throw new IllegalArgumentException("An entity factory impl type must be specified.");
		if(entityAssemlerImplType == null)
			throw new IllegalArgumentException("An entity assembler impl type must be specified.");
		this.entityFactoryImplType = entityFactoryImplType;
		this.entityAssemlerImplType = entityAssemlerImplType;
	}

	@Override
	protected void configure() {
		log.info("Employing Model build module...");

		/*
		// IEntityFactory (optionally depends on IPrimaryKeyGenerator)
		bind(IEntityFactory.class).toProvider(new Provider<IEntityFactory>() {

			@Inject(optional = true)
			IPrimaryKeyGenerator<?> pkg;

			@Override
			public IEntityFactory get() {
				return new EntityFactory(pkg);
			}
		}).in(Scopes.SINGLETON);
		*/

		// IEntityFactory
		bind(IEntityFactory.class).to(entityFactoryImplType).in(Scopes.SINGLETON);

		// IEntityAssembler
		bind(IEntityAssembler.class).to(entityAssemlerImplType).in(Scopes.SINGLETON);
	}
}
