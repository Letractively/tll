package com.tll.di;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.dao.IEntityDao;
import com.tll.di.MockEntityFactoryModule.ConfigKeys;
import com.tll.model.EntityGraph;
import com.tll.model.IEntityGraphBuilder;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.model.key.MockPrimaryKeyGenerator;

/**
 * MockDaoModule
 * @author jpk
 */
public class MockDaoModule extends GModule {

	/**
	 * Constructor
	 */
	public MockDaoModule() {
		super();
		log.info("Employing MOCK Dao");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		
		// IEntityGraphBuilder
		final String egbcn = Config.instance().getString(ConfigKeys.ENTITY_GRAPH_BUILDER_CLASSNAME.getKey());
		if(egbcn == null) {
			throw new IllegalStateException("No entity graph builder class name specified in the configuration");
		}
		try {
			final Class<? extends IEntityGraphBuilder> clz = (Class<? extends IEntityGraphBuilder>) Class.forName(egbcn);
			bind(IEntityGraphBuilder.class).to(clz).in(Scopes.SINGLETON);
		}
		catch(final ClassNotFoundException e) {
			throw new IllegalStateException("No entity graph builder found for name: " + egbcn);
		}

		// EntityGraph
		bind(EntityGraph.class).toProvider(new Provider<EntityGraph>() {

			@Inject
			IEntityGraphBuilder builder;

			@Override
			public EntityGraph get() {
				return builder.buildEntityGraph();
			}
		}).in(Scopes.SINGLETON);
		
		// IPrimaryKeyGenerator
		bind(IPrimaryKeyGenerator.class).to(MockPrimaryKeyGenerator.class).in(Scopes.SINGLETON);

		// IEntityDao
		bind(IEntityDao.class).to(com.tll.dao.mock.EntityDao.class).in(Scopes.SINGLETON);
	}

}