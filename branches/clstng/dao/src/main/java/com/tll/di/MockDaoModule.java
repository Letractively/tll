package com.tll.di;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.dao.IEntityDao;
import com.tll.mock.model.EntityGraph;
import com.tll.mock.model.IEntityGraphBuilder;
import com.tll.mock.model.MockPrimaryKeyGenerator;
import com.tll.model.key.IPrimaryKeyGenerator;

/**
 * MockDaoModule
 * @author jpk
 */
public class MockDaoModule extends GModule {

	/**
	 * ConfigKeys - Configuration property keys for the dao module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		ENTITY_GRAPH_BUILDER_CLASSNAME("db.dao.entityGraphBuilder.classname");

		private final String key;

		/**
		 * Constructor
		 * @param key
		 */
		private ConfigKeys(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}

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
			Class<? extends IEntityGraphBuilder> clz = (Class<? extends IEntityGraphBuilder>) Class.forName(egbcn);
			bind(IEntityGraphBuilder.class).to(clz).in(Scopes.SINGLETON);
		}
		catch(ClassNotFoundException e) {
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