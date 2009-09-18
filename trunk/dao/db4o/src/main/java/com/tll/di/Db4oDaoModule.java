package com.tll.di;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springextensions.db4o.Db4oTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.config.ConfigScope;
import com.db4o.config.Configuration;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.config.IConfigKey;
import com.tll.dao.IDbShell;
import com.tll.dao.IEntityDao;
import com.tll.dao.db4o.Db4oDbShell;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.model.key.SimplePrimaryKeyGenerator;

/**
 * MockDaoModule - MOCK dao impl module.
 * @author jpk
 */
public class Db4oDaoModule extends AbstractModule implements IConfigAware {

	public static final int DEFAULT_TRANS_TIMEOUT = 60;	// seconds

	public static final String DEFAULT_DB4O_FILENAME = "db4o";

	public static final boolean DEFAULT_DB4O_EMPLOY_SPRING_TRANSACTIONS = true;

	static final Log log = LogFactory.getLog(Db4oDaoModule.class);

	/**
	 * Db4oFile annotation
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( {
		ElementType.FIELD, ElementType.PARAMETER })
		@BindingAnnotation
		public @interface Db4oFile {
	}

	/**
	 * ConfigKeys.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		DB4O_FILENAME("db.db4o.filename"),
		DB_TRANS_TIMEOUT("db.transaction.timeout"),
		DB4O_EMPLOY_SPRING_TRANSACTIONS("db.db4o.springTransactions");

		private final String key;

		private ConfigKeys(String key) {
			this.key = key;
		}

		@Override
		public String getKey() {
			return key;
		}
	}

	Config config;

	/**
	 * Constructor
	 */
	public Db4oDaoModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public Db4oDaoModule(Config config) {
		super();
		this.config = config;
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	protected void configure() {
		log.info("Loading db4o dao module...");

		// db40 db file URI
		final String path = config == null ? DEFAULT_DB4O_FILENAME : config.getString(ConfigKeys.DB4O_FILENAME.getKey());
		final URI uri = new File(path).toURI();
		bind(URI.class).annotatedWith(Db4oFile.class).toInstance(uri);

		// Configuration (db4o)
		bind(Configuration.class).toProvider(new Provider<Configuration>() {

			@Override
			public Configuration get() {
				final Configuration c = Db4o.newConfiguration();
				c.generateVersionNumbers(ConfigScope.GLOBALLY);
				c.updateDepth(3);
				return c;
			}

		});

		// ObjectContainer
		bind(ObjectContainer.class).toProvider(new Provider<ObjectContainer>() {

			@Inject
			@Db4oFile
			URI db4oUri;

			@Inject
			Configuration c;

			@Override
			public ObjectContainer get() {
				return Db4o.openFile(c, db4oUri.getPath());
			}
		}).in(Scopes.SINGLETON);

		// determine whether we do spring transactions
		// this is necessary to avoid un-necessary instantiation of an ObjectContainer instance
		// which locks the db4o db file which is problematic when working with the db4o db shell
		final boolean dst = config == null ? DEFAULT_DB4O_EMPLOY_SPRING_TRANSACTIONS : config.getBoolean(ConfigKeys.DB4O_EMPLOY_SPRING_TRANSACTIONS.getKey(), DEFAULT_DB4O_EMPLOY_SPRING_TRANSACTIONS);
		if(dst) {
			log.info("Binding Spring's Db4oTransactionManager to Spring's @Transactional annotation..");
			// PlatformTransactionManager (for transactions)
			bind(PlatformTransactionManager.class).toProvider(new Provider<PlatformTransactionManager>() {

				@Inject
				ObjectContainer oc;

				@Override
				public PlatformTransactionManager get() {
					final Db4oTransactionManager db4oTm = new Db4oTransactionManager(oc);

					// set the transaction timeout
					final int timeout = config == null ? DEFAULT_TRANS_TIMEOUT : config.getInteger(ConfigKeys.DB_TRANS_TIMEOUT.getKey(), DEFAULT_TRANS_TIMEOUT);
					db4oTm.setDefaultTimeout(timeout);
					log.info("Set DB4O default transaction timeout to: " + timeout);

					// validate configuration
					try {
						db4oTm.afterPropertiesSet();
					}
					catch(final Exception e) {
						throw new IllegalStateException(e);
					}

					// required for AspectJ weaving of Spring's @Transactional annotation
					// (must be invoked PRIOR to an @Transactional method call
					AnnotationTransactionAspect.aspectOf().setTransactionManager(db4oTm);

					return db4oTm;
				}
			}).in(Scopes.SINGLETON);
		}

		// IDbShell
		bind(IDbShell.class).toProvider(new Provider<IDbShell>() {

			@Inject
			@Db4oFile
			URI db4oUri;

			@Inject(optional = true)
			Configuration c;

			@Inject(optional = true)
			IEntityGraphPopulator populator;

			@Override
			public IDbShell get() {
				return new Db4oDbShell(db4oUri, populator, c);
			}

		}).in(Scopes.SINGLETON);

		// IPrimaryKeyGenerator
		bind(IPrimaryKeyGenerator.class).to(SimplePrimaryKeyGenerator.class).in(Scopes.SINGLETON);

		// IEntityDao
		bind(IEntityDao.class).to(com.tll.dao.db4o.Db4oEntityDao.class).in(Scopes.SINGLETON);
	}

}