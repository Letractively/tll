/**
 * The Logic Lab
 * @author jpk
 * @since Jan 17, 2010
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.model.EntityFactory;
import com.tll.model.IEntityAssembler;
import com.tll.model.IPrimaryKeyGenerator;

/**
 * ModelBuildModule - Wires up {@link EntityFactory} and
 * {@link IEntityAssembler} implementations.
 * <p>
 * We can't stick all model declared assets in one module since we may have
 * implementations outside of the base model scope. Primarily,
 * {@link IPrimaryKeyGenerator} which is usually datastore dependent!
 * @author jpk
 */
public class ModelBuildModule extends AbstractModule {

	private static final Log log = LogFactory.getLog(ModelBuildModule.class);

	private final Class<? extends IEntityAssembler> entityAssemlerImplType;

	/**
	 * Constructor
	 * @param entityAssemlerImplType Required entity assembler impl type
	 */
	public ModelBuildModule(Class<? extends IEntityAssembler> entityAssemlerImplType) {
		super();
		if(entityAssemlerImplType == null)
			throw new IllegalArgumentException("An entity assembler impl type must be specified.");
		this.entityAssemlerImplType = entityAssemlerImplType;
	}

	@Override
	protected void configure() {
		log.info("Employing Model build module...");

		// EntityFactory (optionally depends on ObjectGenerator)
		bind(EntityFactory.class).toProvider(new Provider<EntityFactory>() {

			@Inject(optional = true)
			IPrimaryKeyGenerator<?> pkg;

			@Override
			public EntityFactory get() {
				return new EntityFactory(pkg);
			}
		}).in(Scopes.SINGLETON);

		// IEntityAssembler
		bind(IEntityAssembler.class).to(entityAssemlerImplType).in(Scopes.SINGLETON);
	}
}
