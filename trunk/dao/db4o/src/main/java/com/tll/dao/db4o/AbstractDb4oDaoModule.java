package com.tll.dao.db4o;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springextensions.db4o.Db4oTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.dao.IEntityDao;
import com.tll.model.EntityMetadata;
import com.tll.model.IEntityFactory;
import com.tll.model.IEntityMetadata;

/**
 * AbstractDb4oDaoModule - Db4o dao impl module.
 * @author jpk
 */
public abstract class AbstractDb4oDaoModule extends AbstractModule {

	private static final int DEFAULT_TRANS_TIMEOUT = 60; // seconds

	private static final boolean DEFAULT_EMPLOY_SPRING_TRANSACTIONS = false;

	static final Logger log = LoggerFactory.getLogger(AbstractDb4oDaoModule.class);

	/**
	 * Db4oFile annotation
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({
		ElementType.FIELD, ElementType.PARAMETER })
	@BindingAnnotation
	public @interface Db4oFile {
	}

	private final Config config;

	/**
	 * Constructor
	 */
	public AbstractDb4oDaoModule() {
		this(null);
	}

	/**
	 * Constructor
	 * @param config
	 */
	public AbstractDb4oDaoModule(Config config) {
		super();
		this.config = config;
	}

	/**
	 * Opportunity for concrete impls to tailor the Configuration on an entity
	 * (object) level.<br>
	 * E.g.: setting updateDepth(...) and/or cascadeOnUpdate(...).
	 * @param c
	 */
	protected abstract void configureConfiguration(EmbeddedConfiguration c);

	/**
	 * @return The db4o named query translator implmentation type.
	 */
	protected abstract Class<? extends IDb4oNamedQueryTranslator> getNamedQueryTranslatorImpl();

	@Override
	protected void configure() {
		log.info("Loading db4o dao module...");

		// db40 db file URI
		bind(URI.class).annotatedWith(Db4oFile.class).toInstance(Db4oDbShell.resolveDb4oFileLocationFromConfig(config));

		// Configuration (db4o)
		// NOTE: we need to always generate a fresh instance to avoid db4o exception
		// being thrown
		bind(EmbeddedConfiguration.class).toProvider(new Provider<EmbeddedConfiguration>() {

			@Override
			public EmbeddedConfiguration get() {
				final EmbeddedConfiguration c = Db4oEmbedded.newConfiguration();
				// configure the db4o configuration
				configureConfiguration(c);
				return c;
			}

		}).in(Scopes.NO_SCOPE);

		// ObjectContainer
		bind(EmbeddedObjectContainer.class).toProvider(new Provider<EmbeddedObjectContainer>() {

			@Inject
			@Db4oFile
			URI db4oUri;

			@Inject
			Provider<EmbeddedConfiguration> c;

			@Override
			public EmbeddedObjectContainer get() {
				log.info("Creating db4o session for: " + db4oUri);
				return Db4oEmbedded.openFile(c.get(), db4oUri.getPath());
			}
		}).in(Scopes.SINGLETON);

		// determine whether we do spring transactions
		// this is necessary to avoid un-necessary instantiation of an
		// ObjectContainer instance
		// which locks the db4o db file which is problematic when working with the
		// db4o db shell
		final boolean dst =
				config == null ? DEFAULT_EMPLOY_SPRING_TRANSACTIONS : config.getBoolean(
						Db4oConfigKeys.DB_TRANS_BINDTOSPRING.getKey(), DEFAULT_EMPLOY_SPRING_TRANSACTIONS);
		if(dst) {
			log.info("Binding Spring's Db4oTransactionManager to Spring's @Transactional annotation..");
			// PlatformTransactionManager (for transactions)
			bind(PlatformTransactionManager.class).toProvider(new Provider<PlatformTransactionManager>() {

				@Inject
				EmbeddedObjectContainer oc;

				@Override
				public PlatformTransactionManager get() {
					final Db4oTransactionManager db4oTm = new Db4oTransactionManager(oc);

					// set the transaction timeout
					final int timeout =
							config == null ? DEFAULT_TRANS_TIMEOUT : config.getInt(Db4oConfigKeys.DB_TRANS_TIMEOUT.getKey(),
									DEFAULT_TRANS_TIMEOUT);
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
			}).asEagerSingleton();
			// IMPT: asEagerSingleton() to force binding trans manager to
			// @Transactional!
		}
		
		// IEntityMetadata (the IEntity flavored impl)
		bind(IEntityMetadata.class).to(EntityMetadata.class);

		// IEntityFactory
		bind(IEntityFactory.class).to(Db4oEntityFactory.class).in(Scopes.SINGLETON);

		// IDb4oNamedQueryTranslator
		bind(IDb4oNamedQueryTranslator.class).to(getNamedQueryTranslatorImpl()).in(Scopes.SINGLETON);

		// IEntityDao
		bind(IEntityDao.class).to(Db4oEntityDao.class).in(Scopes.SINGLETON);
	}
}
