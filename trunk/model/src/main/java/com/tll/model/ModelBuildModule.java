/**
 * The Logic Lab
 * @author jpk
 * @since Jan 17, 2010
 */
package com.tll.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

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

		// IEntityFactory
		bind(new TypeLiteral<IEntityFactory<?>>() {}).to(entityFactoryImplType).in(Scopes.SINGLETON);

		// IEntityAssembler
		bind(IEntityAssembler.class).to(entityAssemlerImplType).in(Scopes.SINGLETON);
	}
}
