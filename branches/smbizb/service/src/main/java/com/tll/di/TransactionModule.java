/**
 * The Logic Lab
 * @author jpk
 * Feb 7, 2009
 */
package com.tll.di;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.tll.config.Config;
import com.tll.config.IConfigKey;

/**
 * TransactionModule
 * @author jpk
 */
public final class TransactionModule extends GModule {

	/**
	 * ConfigKeys - Configuration property keys.
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
	
	@Override
	protected void configure() {
		final UserTransaction userTransaction = new com.atomikos.icatch.jta.UserTransactionImp();

		final com.atomikos.icatch.jta.UserTransactionManager tm = new com.atomikos.icatch.jta.UserTransactionManager();

		// set the transaction timeout
		final int timeout = Config.instance().getInt(ConfigKeys.DB_TRANS_TIMEOUT.getKey());
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

		// PlatformTransactionManager
		bind(PlatformTransactionManager.class).toInstance(ptm);
	}
}
