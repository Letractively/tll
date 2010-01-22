/**
 * 
 */
package com.tll.dao;

import java.util.List;

import com.tll.AbstractConfigAwareTest;
import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.criteria.Criteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.model.IEntity;
import com.tll.model.IPrimaryKey;

/**
 * AbstractDbAwareTest
 * @author jpk
 */
public abstract class AbstractDbAwareTest extends AbstractConfigAwareTest {

	/**
	 * Constructor
	 */
	public AbstractDbAwareTest() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public AbstractDbAwareTest(Config config) {
		super(config);
	}

	/**
	 * Constructor
	 * @param configRefs
	 */
	public AbstractDbAwareTest(ConfigRef... configRefs) {
		super(configRefs);
	}

	/**
	 * Manual loading of an entity from the db given a dao impl instance.
	 * @param <E>
	 * @param dao
	 * @param pk
	 * @return the entity from the db
	 */
	@SuppressWarnings("unchecked")
	protected final static <E extends IEntity> E getEntityFromDb(IEntityDao dao, IPrimaryKey pk) {
		final Criteria<E> criteria = new Criteria<E>((Class<E>) pk.getType());
		criteria.getPrimaryGroup().addCriterion(pk);
		try {
			return dao.findEntity(criteria);
		}
		catch(final InvalidCriteriaException e) {
			throw new IllegalStateException("Unexpected invalid criteria exception occurred: " + e.getMessage());
		}
	}

	/**
	 * Manual loading of entites from the db given a dao impl instance and a
	 * Criteria instance.
	 * @param <E>
	 * @param dao
	 * @param criteria
	 * @return the found entities satisfying the given criteria
	 */
	protected final static <E extends IEntity> List<SearchResult> getEntitiesFromDb(IEntityDao dao,
			Criteria<E> criteria) {
		try {
			return dao.find(criteria, null);
		}
		catch(final InvalidCriteriaException e) {
			throw new IllegalStateException("Unexpected invalid criteria exception occurred: " + e.getMessage());
		}
	}

	protected IDbTrans getDbTrans() {
		return injector.getInstance(IDbTrans.class);
	}

	protected IDbShell getDbShell() {
		return injector.getInstance(IDbShell.class);
	}

	protected final void startNewTransaction() {
		getDbTrans().startTrans();
	}

	protected final void setComplete() {
		getDbTrans().setComplete();
	}

	protected final void endTransaction() {
		getDbTrans().endTrans();
	}

	protected final boolean isTransStarted() {
		return getDbTrans().isTransStarted();
	}
}
