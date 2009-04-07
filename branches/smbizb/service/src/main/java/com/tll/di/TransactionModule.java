/**
 * The Logic Lab
 * @author jpk
 * Feb 7, 2009
 */
package com.tll.di;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.config.IConfigKey;

/**
 * TransactionModule
 * @author jpk
 */
public final class TransactionModule extends AbstractModule implements IConfigAware {

	static final Log log = LogFactory.getLog(TransactionModule.class);

	/**
	 * ConfigKeys - Configuration property keys.
	 * <p>
	 * <b>NOTE: </b>This module depends on a {@link Config} instance already in
	 * the injection context.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		DB_TRANS_TIMEOUT("db.transaction.timeout");

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
	public TransactionModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public TransactionModule(Config config) {
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
		log.info("Employing transaction module");
		// PlatformTransactionManager
		bind(PlatformTransactionManager.class).toProvider(new Provider<PlatformTransactionManager>() {

			@Override
			public PlatformTransactionManager get() {
				final UserTransaction userTransaction = new com.atomikos.icatch.jta.UserTransactionImp();

				final com.atomikos.icatch.jta.UserTransactionManager tm = new com.atomikos.icatch.jta.UserTransactionManager();

				// set the transaction timeout
				final int timeout = config.getInt(ConfigKeys.DB_TRANS_TIMEOUT.getKey());
				try {
					tm.setTransactionTimeout(timeout);
					log.info("Set JTA transaction timeout to: " + timeout);
				}
				catch(final SystemException e) {
					throw new IllegalArgumentException(e.getMessage());
				}

				// use the jta strategy for the PlatformTransactionManager impl
				final JtaTransactionManager ptm = new JtaTransactionManager(userTransaction);
				ptm.afterPropertiesSet(); // initialize it

				// required for AspectJ weaving of Spring's @Transactional annotation
				// (must be invoked PRIOR to an @Transactional method call
				AnnotationTransactionAspect.aspectOf().setTransactionManager(ptm);

				return ptm;
			}
		}).in(Scopes.SINGLETON);
	}
}
