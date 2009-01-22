/*
 * The Logic Lab 
 */
package com.tll.di;

import com.google.inject.Module;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.ConfigKeys;
import com.tll.dao.DaoMode;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.dialect.MySqlDialectHandler;
import com.tll.dao.hibernate.PrimaryKeyGenerator;
import com.tll.model.MockEntityProvider;
import com.tll.model.MockPrimaryKeyGenerator;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.util.EnumUtil;

/**
 * DaoModule
 * @author jpk
 */
public class DaoModule extends CompositeModule {

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

	@Override
	protected Module[] getModulesToBind() {
		if(DaoMode.MOCK.equals(daoMode)) {
			return new Module[] { new MockDaoModule() };
		}
		return new Module[] { new HibernateDaoModule() };
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

			bind(MockEntityProvider.class).in(Scopes.SINGLETON);

			// bind(EntityGraph.class).in(Scopes.SINGLETON);

		}

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
			log.info("Employing ORM Dao");
		}

		@Override
		protected void configure() {
			// IPrimaryKeyGenerator
			// TODO move to model module?
			bind(IPrimaryKeyGenerator.class).to(PrimaryKeyGenerator.class).in(Scopes.SINGLETON);

			// IDbDialectHandler
			bind(IDbDialectHandler.class).to(MySqlDialectHandler.class).in(Scopes.SINGLETON);

			// bind(IPCHDao.class).to(PCHDao.class).in(Scopes.SINGLETON);
			// bind(ISiteStatisticsDao.class).to(SiteStatisticsDao.class).in(Scopes.SINGLETON);
		}

	}
}
