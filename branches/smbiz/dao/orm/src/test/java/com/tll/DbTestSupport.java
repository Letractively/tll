/*
 * The Logic Lab 
 */
package com.tll;

import java.util.List;

import javax.transaction.SystemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.google.inject.Guice;
import com.tll.config.Config;
import com.tll.criteria.Criteria;
import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.IEntityDao;
import com.tll.dao.SearchResult;
import com.tll.dao.jdbc.DbShell;
import com.tll.di.DbShellModule;
import com.tll.model.IEntity;
import com.tll.model.key.PrimaryKey;

/**
 * DbTestSupport - Test that supports raw transactions having an accessible
 * <code>daoMode</code> member property.
 * @author jpk
 */
public final class DbTestSupport {

	private static final Log logger = LogFactory.getLog(DbTestSupport.class);

	/**
	 * Retrieves the entity from the db given a {@link PrimaryKey}.
	 * <p>
	 * <Strong>IMPT NOTE: </strong> we use the dao find methodology as this
	 * ensures a db hit.
	 * @param dao
	 * @param <E>
	 * @param key the primary key
	 * @return the entity from the db or <code>null</code> if not found.
	 */
	public static final <E extends IEntity> E getEntityFromDb(IEntityDao dao, PrimaryKey<E> key) {
		final Criteria<E> criteria = new Criteria<E>(key.getType());
		criteria.getPrimaryGroup().addCriterion(key);
		try {
			return dao.findEntity(criteria);
		}
		catch(final InvalidCriteriaException e) {
			throw new IllegalStateException("Unexpected invalid criteria exception occurred: " + e.getMessage());
		}
	}

	/**
	 * Generic find method intended to validate db ops.
	 * @param <E>
	 * @param dao
	 * @param criteria
	 * @return List of search results
	 */
	public static final <E extends IEntity> List<SearchResult<E>> getEntitiesFromDb(IEntityDao dao, ICriteria<E> criteria) {
		try {
			return dao.find(criteria, null);
		}
		catch(final InvalidCriteriaException e) {
			throw new IllegalStateException("Unexpected invalid criteria exception occurred: " + e.getMessage());
		}
	}

	/**
	 * The {@link DbShell}.
	 */
	private DbShell dbShell;

	/**
	 * The trans manager.
	 */
	private final PlatformTransactionManager tm;

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
	 */
	public DbTestSupport() {
		final UserTransactionManager tm = new UserTransactionManager();

		// set the transaction timeout
		final int timeout = Config.instance().getInt("db.transaction.timeout");
		try {
			tm.setTransactionTimeout(timeout);
			logger.info("Set JTA transaction timeout to: " + timeout);
		}
		catch(final SystemException e) {
			throw new IllegalArgumentException(e.getMessage());
		}

		// PlatformTransactionManager
		this.tm = new JtaTransactionManager(new com.atomikos.icatch.jta.UserTransactionImp());
	}

	/**
	 * Lazily provides a db shell.
	 * @return The {@link DbShell} instance.
	 */
	public DbShell getDbShell() {
		if(dbShell == null) {
			dbShell = Guice.createInjector(new DbShellModule()).getInstance(DbShell.class);
		}
		return dbShell;
	}

	/**
	 * Starts a new db transaction.
	 * @throws IllegalStateException When a transaction is already started.
	 */
	public void startNewTransaction() throws IllegalStateException {
		if(isTransStarted()) {
			throw new IllegalStateException("Transaction already started.");
		}
		final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		transStatus = tm.getTransaction(def);
		transStarted = true;
	}

	/**
	 * Marks the transaction underway as one to commit.
	 */
	public void setComplete() {
		transCompleteFlag = true;
	}

	/**
	 * @return <code>true</code> when a transaction is currently underway.
	 */
	public boolean isTransStarted() {
		return transStarted;
	}

	/**
	 * Ends the transaction currently underway either rolling back or committing
	 * depending on the state of the {@link #transCompleteFlag}.
	 * @throws IllegalStateException When a transaction is NOT currently underway.
	 */
	public void endTransaction() throws IllegalStateException {
		if(!isTransStarted()) {
			throw new IllegalStateException("No transaction in progress");
		}
		if(transCompleteFlag) {
			tm.commit(transStatus);
			transCompleteFlag = false;
		}
		else {
			tm.rollback(transStatus);
		}
		transStarted = false;
		transStatus = null;
	}
}