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
import com.tll.model.IEntityProvider;
import com.tll.model.MockPrimaryKeyGenerator;
import com.tll.model.key.IPrimaryKeyGenerator;
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
		MOCK_ENTITYPROVIDER_CLASSNAME("mock.entityProvider.classname");

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
						final String mepcn = Config.instance().getString(ConfigKeys.MOCK_ENTITYPROVIDER_CLASSNAME.getKey());
						if(mepcn == null) {
							throw new IllegalStateException("No mock entity provider class name specified in the configuration");
						}
						try {
							binder.bind(IEntityProvider.class).to((Class<IEntityProvider>) Class.forName(mepcn)).in(Scopes.SINGLETON);
						}
						catch(ClassNotFoundException e) {
							throw new IllegalStateException("No entity provider implementation found for name: " + mepcn);
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
