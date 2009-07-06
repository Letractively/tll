/*
 * The Logic Lab
 */
package com.tll;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.tll.config.Config;
import com.tll.criteria.Criteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.IEntityDao;
import com.tll.dao.SearchResult;
import com.tll.db.IDbShell;
import com.tll.di.DbShellModule;
import com.tll.model.IEntity;
import com.tll.model.key.PrimaryKey;

/**
 * DbTestSupport - Enables db interaction as well as transaction support.
 * @author jpk
 */
public final class DbTestSupport {

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
	public static final <E extends IEntity> List<SearchResult<?>> getEntitiesFromDb(IEntityDao dao, Criteria<E> criteria) {
		try {
			return dao.find(criteria, null);
		}
		catch(final InvalidCriteriaException e) {
			throw new IllegalStateException("Unexpected invalid criteria exception occurred: " + e.getMessage());
		}
	}

	private static final Log log = LogFactory.getLog(DbTestSupport.class);

	/**
	 * The config instance to employ.
	 */
	private final Config config;

	/**
	 * The persistence manager that generates and manages the (local)
	 * transactions.
	 */
	private final PersistenceManager pm;

	/**
	 * The current transaction.
	 */
	private Transaction t;

	/**
	 * The {@link IDbShell}.
	 */
	private IDbShell dbShell;

	/**
	 * Manually managed flag to indicates a transaction is in progress.
	 */
	private boolean transStarted = false;

	/**
	 * Home-baked support for committing a transaction so as to mimmic
	 * transactions when testing mock dao api for example.
	 */
	private boolean transCompleteFlag = false;

	/**
	 * Constructor
	 * @param config
	 * @param pm if specified, jdo transactions will be employed. If
	 *        <code>null</code>, transacations will be nothing more than semantic
	 *        demarcations in the code.
	 */
	@Inject
	public DbTestSupport(Config config, PersistenceManager pm) {
		super();
		if(config == null) throw new IllegalArgumentException("Null config.");
		this.config = config;
		this.pm = pm;
	}

	/**
	 * Lazily provides a db shell.
	 * @return The {@link IDbShell} instance.
	 */
	public IDbShell getDbShell() {
		if(dbShell == null) {
			dbShell = Guice.createInjector(new DbShellModule(config)).getInstance(IDbShell.class);
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
		if(pm != null) {
			t = pm.currentTransaction();
			t.begin();
		}
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
			if(pm != null) {
				assert t != null && t.isActive();
				t.commit();
				t = null;
			}
			transCompleteFlag = false;
			log.debug("Transaction committed");
		}
		else {
			if(pm != null) {
				assert t != null && t.isActive();
				t.rollback();
				t = null;
			}
			log.debug("Transaction undone");
		}
		transStarted = false;
		t = null;
	}
}
