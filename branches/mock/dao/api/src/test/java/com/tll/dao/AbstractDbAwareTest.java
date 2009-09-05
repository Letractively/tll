/**
 * 
 */
package com.tll.dao;

import java.util.List;

import com.tll.AbstractInjectedTest;
import com.tll.config.Config;
import com.tll.criteria.Criteria;
import com.tll.model.IEntity;
import com.tll.model.key.PrimaryKey;


/**
 * AbstractDbAwareTest
 * @author jpk
 */
public abstract class AbstractDbAwareTest extends AbstractInjectedTest {

	/**
	 * Manual loading of an entity from the db given a dao impl instance.
	 * @param <E>
	 * @param dao
	 * @param pk
	 * @return the entity from the db
	 */
	protected final static <E extends IEntity> E getEntityFromDb(IEntityDao dao, PrimaryKey<E> pk) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Manual loading of entites from the db given a dao impl instance and a Criteria instance.
	 * @param <E>
	 * @param dao
	 * @param pk
	 * @return the found entities satisfying the given criteria
	 */
	protected final static <E extends IEntity> List<SearchResult<?>> getEntitiesFromDb(IEntityDao dao, Criteria<E> criteria) {
		throw new UnsupportedOperationException();
	}
	
	protected final Config config;

	public AbstractDbAwareTest() {
		super();
		this.config = Config.load();
	}
	
	protected final IDbTrans getDbTrans() {
		return injector.getInstance(IDbTrans.class);
	}
	
	protected final IDbShell getDbShell() {
		return injector.getInstance(IDbShell.class);
	}
}
