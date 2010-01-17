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
import org.springframework.orm.jdo.JdoTransactionManager;
import org.springframework.orm.jdo.PersistenceManagerFactoryUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.config.IConfigKey;
import com.tll.dao.IEntityDao;
import com.tll.dao.gae.GaeEntityDao;
import com.tll.dao.gae.GaeTimestampListener;
import com.tll.model.ITimeStampEntity;
import com.tll.model.key.GaePrimaryKeyGenerator;
import com.tll.model.key.IPrimaryKeyGenerator;

/**
 * GaeDaoModule
 * @author jpk
 */
public class GaeDaoModule extends AbstractModule implements IConfigAware {

	/**
	 * ConfigKeys.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		DB_TRANS_TIMEOUT("db.transaction.timeout"),
		DB_TRANS_BINDTOSPRING("db.transaction.bindToSpringAtTransactional");

		private final String key;

		private ConfigKeys(String key) {
			this.key = key;
		}

		@Override
		public String getKey() {
			return key;
		}
	} // ConfigKeys

	private static final int DEFAULT_TRANS_TIMEOUT = 60; // seconds

	private static final boolean DEFAULT_EMPLOY_SPRING_TRANSACTIONS = false;

	protected static final Log log = LogFactory.getLog(GaeDaoModule.class);

	protected Config config;

	/**
	 * Constructor
	 */
	public GaeDaoModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public GaeDaoModule(Config config) {
		super();
		setConfig(config);
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}
	
	/**
	 * Binds the {@link PersistenceManagerFactory} instance.
	 */
	protected void bindPmf() {
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
					final Properties jdoProps = config.asProperties(null);
					final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(jdoProps);

					// add timestamp listener only for timestamp type entities :)
					pmf.addInstanceLifecycleListener(new GaeTimestampListener(), new Class<?>[] { ITimeStampEntity.class });

					return pmf;
				}
			}).asEagerSingleton();
		}
	}

	@Override
	protected final void configure() {
		log.info("Employing JDO dao module.");

		bindPmf();
		
		// create a PersistenceManager provider ensuring that we get the right one
		// based on the current app state!
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

		// IEntityDao
		bind(IEntityDao.class).to(GaeEntityDao.class).in(Scopes.SINGLETON);

		// IPrimaryKeyGenerator (depends on gaej dao)
		bind(IPrimaryKeyGenerator.class).to(GaePrimaryKeyGenerator.class).in(Scopes.SINGLETON);

		final boolean dst =
				config == null ? DEFAULT_EMPLOY_SPRING_TRANSACTIONS : config.getBoolean(ConfigKeys.DB_TRANS_BINDTOSPRING
						.getKey(), DEFAULT_EMPLOY_SPRING_TRANSACTIONS);
		if(dst) {
			log.info("Binding Spring's JdoTransactionManager to Spring's @Transactional annotation..");
			// PlatformTransactionManager (for transactions)
			bind(PlatformTransactionManager.class).toProvider(new Provider<PlatformTransactionManager>() {

				@Inject
				PersistenceManagerFactory pmf;

				@Override
				public PlatformTransactionManager get() {
					final JdoTransactionManager jdoTm = new JdoTransactionManager(pmf);

					// set the transaction timeout
					final int timeout =
							config == null ? DEFAULT_TRANS_TIMEOUT : config.getInt(ConfigKeys.DB_TRANS_TIMEOUT.getKey(),
									DEFAULT_TRANS_TIMEOUT);
					jdoTm.setDefaultTimeout(timeout);
					log.info("Set JDO default transaction timeout to: " + timeout);

					// validate configuration
					try {
						jdoTm.afterPropertiesSet();
					}
					catch(final Exception e) {
						throw new IllegalStateException(e);
					}

					// required for AspectJ weaving of Spring's @Transactional annotation
					// (must be invoked PRIOR to an @Transactional method call)
					AnnotationTransactionAspect.aspectOf().setTransactionManager(jdoTm);

					return jdoTm;
				}
			}).asEagerSingleton();
			// IMPT: asEagerSingleton() to force binding trans manager to
			// @Transactional!
		}
	}
}