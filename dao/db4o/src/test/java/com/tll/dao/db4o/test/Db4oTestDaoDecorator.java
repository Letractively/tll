package com.tll.dao.db4o.test;

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import com.db4o.ObjectContainer;
import com.tll.criteria.Criteria;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.EntityNotFoundException;
import com.tll.dao.IPageResult;
import com.tll.dao.NonUniqueResultException;
import com.tll.dao.SearchResult;
import com.tll.dao.db4o.Db4oEntityDao;
import com.tll.dao.test.EntityDaoTestDecorator;
import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.NameKey;
import com.tll.model.bk.IBusinessKey;
import com.tll.sort.Sorting;

/**
 * Db4oTestDaoDecorator - We use this decorator to force purging for all dao
 * calls. This ensures proper operation when the target object is not
 * currently referenceable in the JVM!
 * @author jpk
 */
public class Db4oTestDaoDecorator extends EntityDaoTestDecorator<Db4oEntityDao> {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(Db4oTestDaoDecorator.class);

	private void hook() {
		// we currently don't do anything
	}

	public ObjectContainer getObjectContainer() {
		return rawDao.getObjectContainer();
	}

	public void setObjectContainer(ObjectContainer oc) {
		rawDao.setObjectContainer(oc);
	}

	@Override
	public int executeQuery(String queryName, IQueryParam[] params) {
		return super.executeQuery(queryName, params);
	}

	@Override
	public <E extends IEntity> List<SearchResult> find(Criteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		final List<SearchResult> r = super.find(criteria, sorting);
		hook();
		return r;
	}

	@Override
	public <E extends IEntity> List<E> findByPrimaryKeys(Class<E> entityType, Collection<?> ids, Sorting sorting) {
		final List<E> r = super.findByPrimaryKeys(entityType, ids, sorting);
		hook();
		return r;
	}

	@Override
	public <E extends IEntity> List<E> findEntities(Criteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		final List<E> r = super.findEntities(criteria, sorting);
		hook();
		return r;
	}

	@Override
	public <E extends IEntity> E findEntity(Criteria<E> criteria) throws InvalidCriteriaException,
	EntityNotFoundException, NonUniqueResultException, DataAccessException {
		final E r = super.findEntity(criteria);
		hook();
		return r;
	}

	@Override
	public <E extends IEntity> List<?> getPrimaryKeys(Criteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		final List<?> r = super.getPrimaryKeys(criteria, sorting);
		hook();
		return r;
	}

	@Override
	public <E extends IEntity> IPageResult<SearchResult> getPage(Criteria<E> criteria, Sorting sorting, int offset,
			int pageSize) throws InvalidCriteriaException {
		final IPageResult<SearchResult> r = super.getPage(criteria, sorting, offset, pageSize);
		hook();
		return r;
	}

	@Override
	public <E extends IEntity> E load(IBusinessKey<E> key) throws EntityNotFoundException, DataAccessException {
		final E r =super.load(key);
		hook();
		return r;
	}

	@Override
	public <N extends INamedEntity> N load(NameKey<N> nameKey) throws EntityNotFoundException,
	NonUniqueResultException, DataAccessException {
		final N r = super.load(nameKey);
		hook();
		return r;
	}

	@Override
	public <E extends IEntity> E load(Class<E> entityType, Object pk) throws EntityNotFoundException, DataAccessException {
		final E r = super.load(entityType, pk);
		hook();
		return r;
	}

	@Override
	public <E extends IEntity> List<E> loadAll(Class<E> entityType) throws DataAccessException {
		final List<E> r = super.loadAll(entityType);
		hook();
		return r;
	}

	@Override
	public <E extends IEntity> E persist(E entity) throws DataAccessException {
		final E r = super.persist(entity);
		hook();
		return r;
	}

	@Override
	public <E extends IEntity> Collection<E> persistAll(Collection<E> entities) throws DataAccessException {
		final Collection<E> r = super.persistAll(entities);
		hook();
		return r;
	}

	@Override
	public <E extends IEntity> void purge(E entity) throws DataAccessException {
		super.purge(entity);
		hook();
	}

	@Override
	public <E extends IEntity> void purge(Class<E> entityType, Object pk) throws EntityNotFoundException, DataAccessException {
		super.purge(entityType, pk);
		hook();
	}

	@Override
	public <E extends IEntity> void purgeAll(Collection<E> entities) throws DataAccessException {
		super.purgeAll(entities);
		hook();
	}

}