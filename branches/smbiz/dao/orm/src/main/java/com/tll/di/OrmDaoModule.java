package com.tll.di;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.dao.IEntityDao;
import com.tll.dao.hibernate.PrimaryKeyGenerator;
import com.tll.model.key.IPrimaryKeyGenerator;

/**
 * OrmDaoModule
 * @author jpk
 */
public class OrmDaoModule extends CompositeModule {

	/**
	 * ConfigKeys - Configuration property keys for the dao module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		DB_NAME("db.name");

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
	 * HibernateModule
	 * @author jpk
	 */
	static final class HibernateModule extends GModule {

		/**
		 * Constructor
		 */
		public HibernateModule() {
			super();
			log.info("Employing Hibernate ORM Dao");
		}
	
		@Override
		protected void configure() {
			final String dbName = Config.instance().getString(ConfigKeys.DB_NAME.getKey());

			// EntityManagerFactory
			bind(EntityManagerFactory.class).toInstance(Persistence.createEntityManagerFactory(dbName));

			// EntityManager
			bind(EntityManager.class).toProvider(new Provider<EntityManager>() {

				@Inject
				private EntityManagerFactory emf;

				public EntityManager get() {
					return emf.createEntityManager();
				}
			});

			// IPrimaryKeyGenerator
			bind(IPrimaryKeyGenerator.class).to(PrimaryKeyGenerator.class).in(Scopes.SINGLETON);

			// IEntityDao
			bind(IEntityDao.class).to(com.tll.dao.hibernate.EntityDao.class).in(Scopes.SINGLETON);
		}
	}

	@Override
	protected Module[] getModulesToBind() {
		return new Module[] {
			new DbDialectModule(), new HibernateModule() };
	}

}