/*
 * The Logic Lab 
 */
package com.tll;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.SystemException;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.tll.config.Config;
import com.tll.criteria.Criteria;
import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.DaoMode;
import com.tll.dao.IEntityDao;
import com.tll.dao.SearchResult;
import com.tll.dao.jdbc.DbShell;
import com.tll.di.DbShellModule;
import com.tll.model.IEntity;
import com.tll.model.key.PrimaryKey;

/**
 * AbstractDbTest - Test that supports raw transactions having an accessible
 * <code>daoMode</code> member property.
 * @author jpk
 */
public abstract class AbstractDbTest extends AbstractInjectedTest {

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
	protected static final <E extends IEntity> E getEntityFromDb(IEntityDao dao, PrimaryKey<E> key) {
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
	protected static final <E extends IEntity> List<SearchResult<E>> getEntitiesFromDb(IEntityDao dao,
			ICriteria<E> criteria) {
		try {
			return dao.find(criteria, null);
		}
		catch(final InvalidCriteriaException e) {
			throw new IllegalStateException("Unexpected invalid criteria exception occurred: " + e.getMessage());
		}
	}

	/**
	 * Create a {@link DbShell} instance?
	 */
	private final boolean createDbShell;

	/**
	 * Add in-house transaction support?
	 */
	private final boolean adHocTransactions;

	/**
	 * The required dao mode for knowing how to handle transactions.
	 */
	private DaoMode daoMode;

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
	public AbstractDbTest() {
		this(null, false, false);
	}

	/**
	 * Constructor
	 * @param createDbShell
	 * @param adHocTransactions
	 * @see #AbstractDbTest(DaoMode, boolean, boolean)
	 */
	protected AbstractDbTest(boolean createDbShell, boolean adHocTransactions) {
		this(null, createDbShell, adHocTransactions);
	}

	/**
	 * Constructor
	 * @param daoMode The {@link DaoMode} to employ for this test. This is
	 *        necessary to know how to handle datastore transactions.
	 * @param createDbShell Make a {@link DbShell} available?
	 * @param adHocTransactions Add ad-hoc transaction support? If
	 *        <code>false</code>, the test is expected to provide transaction
	 *        support as this test depends on them when the <code>daoMode</code>
	 *        equals {@link DaoMode#ORM}.
	 */
	protected AbstractDbTest(DaoMode daoMode, boolean createDbShell, boolean adHocTransactions) {
		super();
		setDaoMode(daoMode);
		this.createDbShell = createDbShell;
		this.adHocTransactions = adHocTransactions;
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		assert daoMode != null : "The DaoMode must be specified for db supporting tests";
		if(daoMode.isDatastore() && adHocTransactions) {
			// add JTA trans support for testing
			modules.add(new Module() {

				@Override
				public void configure(Binder binder) {
					final com.atomikos.icatch.jta.UserTransactionManager tm =
							new com.atomikos.icatch.jta.UserTransactionManager();

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
					binder.bind(PlatformTransactionManager.class).toInstance(
							new JtaTransactionManager(new com.atomikos.icatch.jta.UserTransactionImp()));
				}
			});
		}
		if(createDbShell && (daoMode.isDatastore())) {
			modules.add(new DbShellModule());
		}
	}

	protected final DaoMode getDaoMode() {
		return daoMode;
	}

	protected final void setDaoMode(DaoMode daoMode) {
		if(this.daoMode != null || injector != null) {
			throw new IllegalStateException("The DAO mode has already been set.");
		}
		/*
		if(daoMode != null) {
			// update the config
			Config.instance().setProperty(DaoModule.ConfigKeys.DAO_MODE_PARAM.getKey(), daoMode.toString());
		}
		*/
		this.daoMode = daoMode;
	}

	/**
	 * <strong>NOTE: </strong>The {@link DbShell} is not available by default. It
	 * must be bound in a given module which is added via
	 * {@link #addModules(List)}.
	 * @return The injected {@link DbShell}
	 */
	protected final DbShell getDbShell() {
		return injector.getInstance(DbShell.class);
	}

	protected EntityManagerFactory getEntityManagerFactory() {
		return injector.getInstance(EntityManagerFactory.class);
	}

	protected EntityManager getEntityManager() {
		return injector.getInstance(EntityManager.class);
	}

	protected PlatformTransactionManager getTransactionManager() {
		return injector.getInstance(PlatformTransactionManager.class);
	}

	/**
	 * Starts a new db transaction.
	 * @throws IllegalStateException When a transaction is already started.
	 */
	protected void startNewTransaction() throws IllegalStateException {
		if(isTransStarted()) {
			throw new IllegalStateException("Transaction already started.");
		}
		if(daoMode == DaoMode.ORM) {
			final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			transStatus = getTransactionManager().getTransaction(def);
		}
		transStarted = true;
	}

	/**
	 * Marks the transaction underway as one to commit.
	 */
	protected final void setComplete() {
		transCompleteFlag = true;
	}

	/**
	 * @return <code>true</code> when a transaction is currently underway.
	 */
	protected final boolean isTransStarted() {
		return transStarted;
	}

	/**
	 * Ends the transaction currently underway either rolling back or committing
	 * depending on the state of the {@link #transCompleteFlag}.
	 * @throws IllegalStateException When a transaction is NOT currently underway.
	 */
	protected void endTransaction() throws IllegalStateException {
		if(!isTransStarted()) {
			throw new IllegalStateException("No transaction in progress");
		}
		if(transCompleteFlag) {
			if(daoMode == DaoMode.ORM) {
				getTransactionManager().commit(transStatus);
			}
			transCompleteFlag = false;
		}
		else {
			if(daoMode == DaoMode.ORM) {
				getTransactionManager().rollback(transStatus);
			}
		}
		transStarted = false;
		transStatus = null;
	}

}
