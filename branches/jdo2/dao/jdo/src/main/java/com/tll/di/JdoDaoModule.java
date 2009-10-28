/**
 * The Logic Lab
 * @author jpk
 * @since Oct 27, 2009
 */
package com.tll.di;

import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.config.IConfigKey;
import com.tll.dao.IEntityDao;
import com.tll.dao.jdo.JdoEntityDao;
import com.tll.model.JdoPrimaryKeyGenerator;
import com.tll.model.key.IPrimaryKeyGenerator;

/**
 * JdoDaoModule
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

		bind(IPrimaryKeyGenerator.class).to(JdoPrimaryKeyGenerator.class).in(Scopes.SINGLETON);

		// PersistenceManagerFactory
		bind(PersistenceManagerFactory.class).toProvider(new Provider<PersistenceManagerFactory>() {

			@Override
			public PersistenceManagerFactory get() {
				final Properties props = config.asProperties(null);
				final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(props);
				// TODO fix
				//pmf.addInstanceLifecycleListener(new JdoEntityListener(), null);
				return pmf;
			}
		}).asEagerSingleton();

		// IEntityDao
		bind(IEntityDao.class).to(JdoEntityDao.class).in(Scopes.SINGLETON);
	}
}