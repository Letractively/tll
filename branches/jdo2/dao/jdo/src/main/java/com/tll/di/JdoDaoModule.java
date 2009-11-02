/**
 * The Logic Lab
 * @author jpk
 * @since Oct 27, 2009
 */
package com.tll.di;

import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.jdo.PersistenceManagerFactoryUtils;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.dao.IEntityDao;
import com.tll.dao.jdo.JdoEntityDao;

/**
 * JdoDaoModule <br>
 * <b>NOTE: </b>An <code>IPrimaryKeyGenerator</code> impl is <em>not</em> bound in
 * this module.
 * @author jpk
 */
public class JdoDaoModule extends AbstractModule implements IConfigAware {

	public static final String JDO_PMF_CLASS = "javax.jdo.PersistenceManagerFactoryClass";
	public static final String JDO_CONN_DRIVER = "javax.jdo.option.ConnectionDriverName";
	public static final String JDO_CONN_URL = "javax.jdo.option.ConnectionURL";
	public static final String JDO_CONN_USERNAME = "javax.jdo.option.ConnectionUserName";
	public static final String JDO_CONN_PASSWORD = "javax.jdo.option.ConnectionPassword";

	protected static final Log log = LogFactory.getLog(JdoDaoModule.class);

	/**
	 * Generates a new {@link Properties} instance containing jdo specification
	 * bootstrap properties from a {@link Config} instance containing
	 * {@link JdbcConfigKeys}.
	 * @param config a {@link Config} instance containing {@link JdbcConfigKeys}
	 * @return Newly created {@link Properties} instance
	 */
	public static Properties generateJdoSpecPropertiesFromConfig(Config config) {
		final Properties p = new Properties();
		p.setProperty(JDO_PMF_CLASS, "org.datanucleus.jdo.JDOPersistenceManagerFactory");
		final String dbType = config.getString(JdbcConfigKeys.DB_TYPE.getKey());
		if("mysql".equals(dbType)) {
			p.setProperty(JDO_CONN_DRIVER, "com.mysql.jdbc.Driver");
		}
		else
			throw new IllegalStateException("Unhandled db type: " + dbType);
		p.setProperty(JDO_CONN_URL, config.getString(JdbcConfigKeys.DB_URL.getKey()));
		p.setProperty(JDO_CONN_USERNAME, config.getString(JdbcConfigKeys.DB_USERNAME.getKey()));
		p.setProperty(JDO_CONN_PASSWORD, config.getString(JdbcConfigKeys.DB_PASSWORD.getKey()));

		return p;
	}

	protected Config config;

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
	protected final void configure() {
		log.info("Employing JDO dao module.");

		if(config == null) {
			// we assume we have a jdoconfig.xml on the classpath under META-INF/

			// PersistenceManagerFactory
			bind(PersistenceManagerFactory.class).toProvider(new Provider<PersistenceManagerFactory>() {

				@Override
				public PersistenceManagerFactory get() {
					final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory();
					return pmf;
				}
			}).asEagerSingleton();
		}
		else {
			// config override bootstrapping

			// PersistenceManagerFactory
			bind(PersistenceManagerFactory.class).toProvider(new Provider<PersistenceManagerFactory>() {

				@Override
				public PersistenceManagerFactory get() {
					final Properties jdoProps = generateJdoSpecPropertiesFromConfig(config);
					final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(jdoProps);
					return pmf;
				}
			}).asEagerSingleton();
		}

		// IEntityDao
		bind(IEntityDao.class).to(JdoEntityDao.class).in(Scopes.SINGLETON);

		// create a PersistenceManager provider ensuring that we get the right one based on the current app state!
		bind(PersistenceManager.class).toProvider(new Provider<PersistenceManager>() {

			@Inject
			PersistenceManagerFactory pmf;

			@Override
			public PersistenceManager get() {
				return PersistenceManagerFactoryUtils.getPersistenceManager(pmf, true);
			}
		}).in(Scopes.NO_SCOPE);
		// NOTE: we set NO_SCOPE so we always go through Spring's framework
		// as it manages the life-cycle of PersistenceManagers
	}
}