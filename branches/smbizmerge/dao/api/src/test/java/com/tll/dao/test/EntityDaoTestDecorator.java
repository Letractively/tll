package com.tll.dao.test;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.tll.criteria.Criteria;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.EntityNotFoundException;
import com.tll.dao.IEntityDao;
import com.tll.dao.IPageResult;
import com.tll.dao.NonUniqueResultException;
import com.tll.dao.SearchResult;
import com.tll.dao.Sorting;
import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.NameKey;
import com.tll.model.bk.IBusinessKey;

/**
 * EntityDaoTestDecorator - Decorates {@link IEntityDao} to:
 * <ol>
 * <li>Manage dao testing life-cycle and cleanup.
 * <li>Prevent the dao tests from degrading as the code base naturally changes
 * over time. I.e.: any IEntityDao method signature change is forced upon the
 * dao testing.
 * </ol>
 * @author jpk
 * @param <T> the raw dao impl type
 */
public class EntityDaoTestDecorator<T extends IEntityDao> implements IEntityDao {

	protected T rawDao;

	public IEntityDao getRawDao() {
		return rawDao;
	}

	public void setRawDao(T rawDao) {
		this.rawDao = rawDao;
	}

	@Override
	public int executeQuery(String queryName, IQueryParam[] params) {
		return rawDao.executeQuery(queryName, params);
	}

	@Override
	public <E extends IEntity> List<SearchResult> find(Criteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		return rawDao.find(criteria, sorting);
	}

	@Override
	public <E extends IEntity> List<E> findByPrimaryKeys(Class<E> entityType, Collection<Long> ids, Sorting sorting) {
		return rawDao.findByPrimaryKeys(entityType, ids, sorting);
	}

	@Override
	public <E extends IEntity> List<E> findEntities(Criteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		return rawDao.findEntities(criteria, sorting);
	}

	@Override
	public <E extends IEntity> E findEntity(Criteria<E> criteria) throws InvalidCriteriaException,
	EntityNotFoundException, NonUniqueResultException, DataAccessException {
		return rawDao.findEntity(criteria);
	}

	@Override
	public <E extends IEntity> List<Long> getPrimaryKeys(Criteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		return rawDao.getPrimaryKeys(criteria, sorting);
	}

	@Override
	public <E extends IEntity> IPageResult<SearchResult> getPage(Criteria<E> criteria, Sorting sorting, int offset,
			int pageSize) throws InvalidCriteriaException {
		return rawDao.getPage(criteria, sorting, offset, pageSize);
	}

	@Override
	public <E extends IEntity> E load(IBusinessKey<E> key) throws EntityNotFoundException, DataAccessException {
		return rawDao.load(key);
	}

	@Override
	public <N extends INamedEntity> N load(NameKey<N> nameKey) throws EntityNotFoundException,
	NonUniqueResultException, DataAccessException {
		return rawDao.load(nameKey);
	}

	@Override
	public <E extends IEntity> E load(Class<E> entityType, Long pk) throws EntityNotFoundException, DataAccessException {
		return rawDao.load(entityType, pk);
	}

	@Override
	public <E extends IEntity> List<E> loadAll(Class<E> entityType) throws DataAccessException {
		return rawDao.loadAll(entityType);
	}

	@Override
	public <E extends IEntity> E persist(E entity) throws DataAccessException {
		return rawDao.persist(entity);
	}

	@Override
	public <E extends IEntity> Collection<E> persistAll(Collection<E> entities) throws DataAccessException {
		return rawDao.persistAll(entities);
	}

	@Override
	public <E extends IEntity> void purge(E entity) throws DataAccessException {
		rawDao.purge(entity);
	}

	@Override
	public <E extends IEntity> void purge(Class<E> entityType, Long pk) throws EntityNotFoundException, DataAccessException {
		rawDao.purge(entityType, pk);
	}

	@Override
	public <E extends IEntity> void purgeAll(Collection<E> entities) throws DataAccessException {
		rawDao.purgeAll(entities);
	}

}