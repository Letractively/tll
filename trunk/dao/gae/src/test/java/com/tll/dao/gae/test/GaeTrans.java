/**
 * The Logic Lab
 * @author jpk
 * @since Jan 17, 2010
 */
package com.tll.dao.gae.test;

import javax.jdo.PersistenceManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.jdo.JdoTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.tll.dao.IDbTrans;

/**
 * GaeTrans
 * @author jpk
 */
public class GaeTrans implements IDbTrans {

	private static final Log log = LogFactory.getLog(GaeTrans.class);

	private final PersistenceManagerFactory pmf;

	/**
	 * The trans manager.
	 */
	private JdoTransactionManager tm;

	/**
	 * Used to check if a transaction is in progress only when using Spring
	 * transaction management.
	 */
	private TransactionStatus transStatus;

	/**
	 * Flag used in place of {@link #transStatus} when Spring transaction
	 * management is NOT employed.
	 */
	private boolean transStarted = false;

	/**
	 * Home-baked support for committing a transaction analagous to Spring's
	 * setComplete() test related method.
	 */
	private boolean transCompleteFlag = false;

	/**
	 * Constructor
	 * @param pmf the required persistence manager factory
	 */
	public GaeTrans(PersistenceManagerFactory pmf) {
		super();
		if(pmf == null) throw new IllegalArgumentException("No PersistenceManagerFactory specified.");
		this.pmf = pmf;
	}

	@Override
	public void startTrans() throws IllegalStateException {
		if(isTransStarted()) {
			throw new IllegalStateException("Transaction already started.");
		}
		final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		transStatus = getTransMgr().getTransaction(def);
		transStarted = true;
	}

	@Override
	public void endTrans() throws IllegalStateException {
		if(!isTransStarted()) {
			throw new IllegalStateException("No transaction in progress");
		}
		if(transCompleteFlag) {
			getTransMgr().commit(transStatus);
			transCompleteFlag = false;
		}
		else {
			getTransMgr().rollback(transStatus);
		}
		transStarted = false;
		transStatus = null;
	}

	@Override
	public void setComplete() throws IllegalStateException {
		transCompleteFlag = true;
	}

	@Override
	public boolean isTransStarted() {
		return transStarted;
	}

	/**
	 * @return The lazily instantiated db level trans manager.
	 */
	private JdoTransactionManager getTransMgr() {
		if(tm == null) {
			final JdoTransactionManager jdoTm = new JdoTransactionManager();
			jdoTm.setPersistenceManagerFactory(pmf);

			// set the transaction timeout
			final int timeout = 60 * 4; // 4 mins
			jdoTm.setDefaultTimeout(timeout);
			log.info("Set GAE default transaction timeout to: " + timeout);

			// validate configuration
			try {
				jdoTm.afterPropertiesSet();
			}
			catch(final Exception e) {
				throw new IllegalStateException(e);
			}

			this.tm = jdoTm;
		}
		return tm;
	}
}
