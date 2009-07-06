package com.tll.di;

import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

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

/**
 * JdoDaoModule - ORM dao module.
 * @author jpk
 */
public class JdoDaoModule extends AbstractModule implements IConfigAware {

	private static final Log log = LogFactory.getLog(JdoDaoModule.class);

	/**
	 * ConfigKeys - Configuration property keys for the dao module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		JDO_PMF_CLASS("javax.jdo.PersistenceManagerFactoryClass"),
		JDO_CONN_DRIVER("javax.jdo.option.ConnectionDriverName"),
		JDO_CONN_URL("javax.jdo.option.ConnectionURL"),
		JDO_CONN_USERNAME("javax.jdo.option.ConnectionUserName"),
		JDO_CONN_PASSWORD("javax.jdo.option.ConnectionPassword");

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
	public JdoDaoModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public JdoDaoModule(Config config) {
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
		log.info("Employing JDO dao module.");

		// PersistenceManagerFactory
		bind(PersistenceManagerFactory.class).toProvider(new Provider<PersistenceManagerFactory>() {

			@Override
			public PersistenceManagerFactory get() {
				final Properties props = config.asProperties("javax.jdo");
				final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(props);
				return pmf;
			}
		}).asEagerSingleton();

		// PersistenceManager
		bind(PersistenceManager.class).toProvider(new Provider<PersistenceManager>() {

			@Inject
			private PersistenceManagerFactory pmf;

			public PersistenceManager get() {
				return pmf.getPersistenceManager();
			}
		});

		// IEntityDao
		bind(IEntityDao.class).to(com.tll.dao.jdo.EntityDao.class).in(Scopes.SINGLETON);
	}
}
