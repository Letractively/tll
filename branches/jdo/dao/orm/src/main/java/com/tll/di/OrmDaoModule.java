package com.tll.di;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.config.IConfigKey;
import com.tll.dao.IEntityDao;
import com.tll.dao.orm.PrimaryKeyGenerator;
import com.tll.model.key.IPrimaryKeyGenerator;

/**
 * OrmDaoModule - ORM dao module.
 * @author jpk
 */
public class OrmDaoModule extends AbstractModule implements IConfigAware {

	private static final Log log = LogFactory.getLog(DbShellModule.class);

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

	Config config;

	/**
	 * Constructor
	 */
	public OrmDaoModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public OrmDaoModule(Config config) {
		super();
		setConfig(config);
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	protected void configure() {
		if(config == null) throw new IllegalStateException("No config instance specified.");
		log.info("Employing ORM dao module.");

		// EntityManagerFactory
		bind(EntityManagerFactory.class).toProvider(new Provider<EntityManagerFactory>() {

			@Override
			public EntityManagerFactory get() {
				final String dbName = config.getString(ConfigKeys.DB_NAME.getKey());
				return Persistence.createEntityManagerFactory(dbName);
			}
		}).asEagerSingleton();

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
		bind(IEntityDao.class).to(com.tll.dao.orm.EntityDao.class).in(Scopes.SINGLETON);
	}
}
