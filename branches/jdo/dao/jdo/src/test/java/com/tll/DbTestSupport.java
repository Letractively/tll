/*
 * The Logic Lab
 */
package com.tll;

import java.util.List;

import javax.jdo.PersistenceManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.jdo.JdoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.tll.config.Config;
import com.tll.criteria.Criteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.IEntityDao;
import com.tll.dao.SearchResult;
import com.tll.model.IEntity;
import com.tll.model.key.PrimaryKey;

/**
 * DbTestSupport - Enables db interaction as well as transaction support.
 * @author jpk
 */
public final class DbTestSupport {

	private static final Log log = LogFactory.getLog(DbTestSupport.class);

	/**
	 * Retrieves the entity from the db given a {@link PrimaryKey}.
	 * <p>
	 * <b>NOTE:</b> we use the dao find methodology as this ensures a db hit.
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
	public static final <E extends IEntity> List<SearchResult<?>> getEntitiesFromDb(IEntityDao dao, Criteria<E> criteria) {
		try {
			return dao.find(criteria, null);
		}
		catch(final InvalidCriteriaException e) {
			throw new IllegalStateException("Unexpected invalid criteria exception occurred: " + e.getMessage());
		}
	}

	private static final Log logger = LogFactory.getLog(DbTestSupport.class);

	/**
	 * The default transaction timeout in miliseconds.
	 */
	private static final int DEFAULT_TRANS_TIMEOUT_MILIS = 30000;

	/**
	 * The config instance to employ.
	 */
	private final Config config;

	private final PersistenceManagerFactory pmf;

	/**
	 * The *current* persistence manager from which transactions are generated and
	 * managed.
	 */
	private PlatformTransactionManager tm;

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
	 * @param config
	 * @param pmf the required {@link PersistenceManagerFactory} instance
	 */
	public DbTestSupport(Config config, PersistenceManagerFactory pmf) {
		super();
		if(config == null) throw new IllegalArgumentException("Null config");
		this.config = config;
		if(pmf == null) throw new IllegalArgumentException("Null pmf");
		this.pmf = pmf;
	}

	/**
	 * @return The lazily instantiated db level trans manager.
	 */
	private PlatformTransactionManager getTransMgr() {
		if(tm == null) {
			final JdoTransactionManager jdoTm = new JdoTransactionManager();

			// get the trans timeout val from the config
			final int timeout = config.getInt("db.transaction.timeout", DEFAULT_TRANS_TIMEOUT_MILIS);
			if(timeout <= 0) {
				throw new IllegalStateException("Invalid trans timeout: " + timeout);
			}

			try {
				jdoTm.setAutodetectDataSource(false);
				jdoTm.setPersistenceManagerFactory(pmf);
				jdoTm.setDefaultTimeout(timeout);
				logger.info("Set default JDO transaction timeout to: " + timeout);
			}
			catch(final Exception e) {
				throw new IllegalArgumentException(e.getMessage());
			}

			// ensure all is well
			jdoTm.afterPropertiesSet();

			this.tm = jdoTm;
		}
		return tm;
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
		transStatus = getTransMgr().getTransaction(def);
		transStarted = true;
		log.debug("New transaction created");
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
			getTransMgr().commit(transStatus);
			transCompleteFlag = false;
			log.debug("Transaction committed");
		}
		else {
			getTransMgr().rollback(transStatus);
		}
		transStarted = false;
		transStatus = null;
	}
}
