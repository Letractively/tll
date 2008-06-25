/*
 * The Logic Lab 
 */
package com.tll;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.Assert;

import com.google.inject.Module;
import com.tll.criteria.Criteria;
import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.IEntityDao;
import com.tll.dao.JpaMode;
import com.tll.guice.DbShellModule;
import com.tll.listhandler.SearchResult;
import com.tll.model.IEntity;
import com.tll.model.key.PrimaryKey;

/**
 * DbTest - Test that supports raw transactions having an accessible
 * <code>jpaMode</code> member property.
 * @author jpk
 */
public abstract class DbTest extends TestBase {

	/**
	 * The JPA mode.
	 */
	protected JpaMode jpaMode;

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
	public DbTest() {
		this(null);
	}

	/**
	 * Constructor
	 * @param jpaMode The JpaMode to employ for this test.
	 */
	protected DbTest(JpaMode jpaMode) {
		super();
		this.jpaMode = jpaMode;
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		assert jpaMode != null : "The JpaMode must be specified for db supporting tests";
		if(jpaMode != JpaMode.NONE) {
			// IMPT: use the TEST db!
			if(jpaMode != JpaMode.MOCK) {
				modules.add(new DbShellModule());
			}
		}
	}

	@Override
	protected void afterClass() {
		super.afterClass();
		if(jpaMode != JpaMode.NONE && jpaMode != JpaMode.SPRING) {
			getEntityManager().close();
			getEntityManagerFactory().close();
		}
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
	 * Retrieves the entity from the db given a {@link IPrimaryKey}.
	 * <p>
	 * <Strong>IMPT NOTE: </strong> we use the dao find methodology as this
	 * ensures a db hit.
	 * @param key the primary key
	 * @return the entity from the db or <code>null</code> if not found.
	 */
	@SuppressWarnings("unchecked")
	protected static final <E extends IEntity, D extends IEntityDao<E>> E getEntityFromDb(D dao, PrimaryKey key) {
		Criteria<? extends E> criteria = new Criteria<E>((Class<E>) key.getType());
		criteria.getPrimaryGroup().addCriterion(key);
		try {
			return dao.findEntity(criteria);
		}
		catch(InvalidCriteriaException e) {
			Assert.fail("Unexpected invalid criteria exception occurred: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Generic find method intended to validate db ops.
	 * @param <E>
	 * @param <D>
	 * @param dao
	 * @param criteria
	 * @return List of search results
	 */
	protected static final <E extends IEntity, D extends IEntityDao<E>> List<SearchResult<E>> getEntitiesFromDb(D dao,
			ICriteria<? extends E> criteria) {
		try {
			return dao.find(criteria, null);
		}
		catch(InvalidCriteriaException e) {
			Assert.fail("Unexpected invalid criteria exception occurred: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Starts a new db transaction.
	 * @throws IllegalStateException When a transaction is already started.
	 */
	protected final void startNewTransaction() throws IllegalStateException {
		if(isTransStarted()) {
			throw new IllegalStateException("Transaction already started.");
		}
		if(jpaMode == JpaMode.SPRING) {
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			transStatus = getTransactionManager().getTransaction(def);
		}
		else if(jpaMode != JpaMode.NONE) {
			getEntityManager().getTransaction().begin();
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
	protected final void endTransaction() throws IllegalStateException {
		if(!isTransStarted()) {
			throw new IllegalStateException("No transaction in progress");
		}
		if(transCompleteFlag) {
			if(jpaMode == JpaMode.SPRING) {
				getTransactionManager().commit(transStatus);
			}
			else if(jpaMode != JpaMode.NONE) {
				getEntityManager().getTransaction().commit();
			}
			transCompleteFlag = false;
		}
		else {
			if(jpaMode == JpaMode.SPRING) {
				getTransactionManager().rollback(transStatus);
			}
			else if(jpaMode != JpaMode.NONE) {
				getEntityManager().getTransaction().rollback();
			}
		}
		transStarted = false;
		transStatus = null;
	}

}
