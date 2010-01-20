/**
 * The Logic Lab
 * @author jpk
 * @since Sep 16, 2009
 */
package com.tll.dao.db4o.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springextensions.db4o.Db4oTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.db4o.ObjectContainer;
import com.google.inject.Inject;
import com.tll.dao.IDbTrans;

/**
 * Db4oTrans
 * @author jpk
 */
public class Db4oTrans implements IDbTrans {

	private static final Log log = LogFactory.getLog(Db4oTrans.class);

	/**
	 * The trans manager.
	 */
	private Db4oTransactionManager tm;

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

	private final ObjectContainer oc;

	/**
	 * Constructor
	 * @param oc The required db4o object container
	 */
	@Inject
	public Db4oTrans(ObjectContainer oc) {
		super();
		this.oc = oc;
	}

	/**
	 * Hook to [re-]set the {@link ObjectContainer} ref.
	 * @param oc the object container to set
	 */
	public void setObjectContainer(ObjectContainer oc) {
		getTransMgr().setObjectContainer(oc);
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
	private Db4oTransactionManager getTransMgr() {
		if(tm == null) {
			final Db4oTransactionManager db4oTm = new Db4oTransactionManager(oc);

			// set the transaction timeout
			final int timeout = 60 * 4; // 4 mins
			db4oTm.setDefaultTimeout(timeout);
			log.info("Set DB4O default transaction timeout to: " + timeout);

			// validate configuration
			try {
				db4oTm.afterPropertiesSet();
			}
			catch(final Exception e) {
				throw new IllegalStateException(e);
			}

			this.tm = db4oTm;
		}
		return tm;
	}
}
