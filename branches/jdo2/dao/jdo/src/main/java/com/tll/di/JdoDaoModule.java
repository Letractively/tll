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
import com.tll.dao.jdo.JdoTimestampListener;
import com.tll.model.ITimeStampEntity;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.model.key.JdoPrimaryKeyGenerator;

/**
 * JdoDaoModule
 * @author jpk
 */
public class JdoDaoModule extends AbstractModule implements IConfigAware {

	protected static final Log log = LogFactory.getLog(JdoDaoModule.class);

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
					final Properties jdoProps = config.asProperties(null);
					final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(jdoProps);

					// add timestamp listener only for timestamp type entities :)
					pmf.addInstanceLifecycleListener(new JdoTimestampListener(), new Class<?>[] {
						ITimeStampEntity.class
					});

					return pmf;
				}
			}).asEagerSingleton();
		}

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

		// IPrimaryKeyGenerator
		bind(IPrimaryKeyGenerator.class).to(JdoPrimaryKeyGenerator.class).in(Scopes.SINGLETON);

		// IEntityDao
		bind(IEntityDao.class).to(JdoEntityDao.class).in(Scopes.SINGLETON);
	}
}