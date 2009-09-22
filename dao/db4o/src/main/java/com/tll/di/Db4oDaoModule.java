package com.tll.di;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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
import com.tll.dao.IEntityDao;
import com.tll.dao.db4o.IDb4oNamedQueryTranslator;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.model.key.SimplePrimaryKeyGenerator;

/**
 * Db4oDaoModule - Db4o dao impl module.
 * @author jpk
 */
public abstract class Db4oDaoModule extends AbstractModule implements IConfigAware {

	public static final int DEFAULT_TRANS_TIMEOUT = 60;	// seconds

	public static final String DEFAULT_DB4O_FILENAME = "db4o";

	public static final boolean DEFAULT_DB4O_EMPLOY_SPRING_TRANSACTIONS = false;

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

	/**
	 * @return The db4o named query translator implmentation type.
	 */
	protected abstract Class<? extends IDb4oNamedQueryTranslator> getNamedQueryTranslatorImpl();

	@Override
	protected final void configure() {
		log.info("Loading db4o dao module...");

		// db40 db file URI
		final String path = config == null ? DEFAULT_DB4O_FILENAME : config.getString(ConfigKeys.DB4O_FILENAME.getKey());
		//if(path.indexOf('/') >= 0) thr
		try {
			// first attempt to load existing file
			final URL url = Db4oDaoModule.class.getClassLoader().getResource(path);
			URI uri = url == null ? null : url.toURI();
			if(uri == null) {
				// create in working dir
				final File f = new File(path);
				uri = f.toURI();
			}
			bind(URI.class).annotatedWith(Db4oFile.class).toInstance(uri);
		}
		catch(final URISyntaxException e) {
			throw new IllegalStateException(e);
		}

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
				log.info("Creating db4o session for: " + db4oUri);
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

		// IPrimaryKeyGenerator
		bind(IPrimaryKeyGenerator.class).to(SimplePrimaryKeyGenerator.class).in(Scopes.SINGLETON);

		// IDb4oNamedQueryTranslator
		bind(IDb4oNamedQueryTranslator.class).to(getNamedQueryTranslatorImpl()).in(Scopes.SINGLETON);

		// IEntityDao
		bind(IEntityDao.class).to(com.tll.dao.db4o.Db4oEntityDao.class).in(Scopes.SINGLETON);
	}

}