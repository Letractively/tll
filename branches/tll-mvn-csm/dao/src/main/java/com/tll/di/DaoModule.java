/*
 * The Logic Lab 
 */
package com.tll.di;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.dao.DaoMode;
import com.tll.dao.IEntityDao;
import com.tll.dao.hibernate.PrimaryKeyGenerator;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.model.mock.IEntityGraphBuilder;
import com.tll.model.mock.MockPrimaryKeyGenerator;
import com.tll.util.EnumUtil;

/**
 * DaoModule
 * @author jpk
 */
public class DaoModule extends CompositeModule {

	/**
	 * ConfigKeys - Configuration property keys for the dao module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		DAO_MODE_PARAM("db.dao.mode"),
		ENTITY_GRAPH_BUILDER_CLASSNAME("entityGraphBuilder.classname");

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
	 * DAO mode override. <code>null</code> indicates the property will be gotten
	 * from the {@link Config} instance.
	 */
	private final DaoMode daoMode;

	/**
	 * Constructor
	 */
	public DaoModule() {
		this(null);
	}

	/**
	 * Constructor
	 * @param daoMode
	 */
	public DaoModule(DaoMode daoMode) {
		super();
		this.daoMode =
				daoMode == null ? EnumUtil.fromString(DaoMode.class, Config.instance().getString(
						ConfigKeys.DAO_MODE_PARAM.getKey())) : daoMode;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Module[] getModulesToBind() {
		if(DaoMode.MOCK == daoMode) {
			// ad hoc mock entity provider module
			return new Module[] {
				new Module() {

					@Override
					public void configure(Binder binder) {
						final String egbcn = Config.instance().getString(ConfigKeys.ENTITY_GRAPH_BUILDER_CLASSNAME.getKey());
						if(egbcn == null) {
							throw new IllegalStateException("No entity graph builder class name specified in the configuration");
						}
						try {
							Class<? extends IEntityGraphBuilder> clz = (Class<? extends IEntityGraphBuilder>) Class.forName(egbcn);
							binder.bind(IEntityGraphBuilder.class).to(clz).in(Scopes.SINGLETON);
						}
						catch(ClassNotFoundException e) {
							throw new IllegalStateException("No entity graph builder found for name: " + egbcn);
						}
					}
				}, new MockDaoModule() };
		}
		else if(DaoMode.ORM == daoMode) {
			return new Module[] {
				new DbDialectModule(), new HibernateDaoModule() };
		}
		throw new IllegalStateException("Unhandled dao mode: " + daoMode);
	}

	/**
	 * HibernateDaoModule
	 * @author jpk
	 */
	private static class HibernateDaoModule extends GModule {

		/**
		 * Constructor
		 */
		public HibernateDaoModule() {
			super();
			log.info("Employing Hibernate ORM Dao");
		}

		@Override
		protected void configure() {
			// IPrimaryKeyGenerator
			bind(IPrimaryKeyGenerator.class).to(PrimaryKeyGenerator.class).in(Scopes.SINGLETON);

			// IEntityDao
			bind(IEntityDao.class).to(com.tll.dao.hibernate.EntityDao.class).in(Scopes.SINGLETON);
		}

	}

	/**
	 * MockDaoModule
	 * @author jpk
	 */
	private static class MockDaoModule extends GModule {

		/**
		 * Constructor
		 */
		public MockDaoModule() {
			super();
			log.info("Employing MOCK Dao");
		}

		@Override
		protected void configure() {
			// IPrimaryKeyGenerator
			bind(IPrimaryKeyGenerator.class).to(MockPrimaryKeyGenerator.class).in(Scopes.SINGLETON);

			// IEntityDao
			bind(IEntityDao.class).to(com.tll.dao.mock.EntityDao.class).in(Scopes.SINGLETON);
		}

	}
}
