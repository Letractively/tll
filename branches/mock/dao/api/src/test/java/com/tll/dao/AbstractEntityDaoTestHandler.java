/**
 * The Logic Lab
 * @author jpk
 * Jan 27, 2009
 */
package com.tll.dao;

import java.util.Set;

import org.testng.Assert;

import com.tll.criteria.IQueryParam;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.model.EntityBeanFactory;
import com.tll.model.IEntity;
import com.tll.model.INamedEntity;

/**
 * AbstractEntityDaoTestHandler
 * @param <E> The entity type
 * @author jpk
 */
public abstract class AbstractEntityDaoTestHandler<E extends IEntity> implements IEntityDaoTestHandler<E> {

	private IEntityDao entityDao;
	private EntityBeanFactory entityBeanFactory;

	@Override
	public void init(IEntityDao anEntityDao, EntityBeanFactory anEntityBeanFactory) {
		this.entityDao = anEntityDao;
		this.entityBeanFactory = anEntityBeanFactory;
	}

	/**
	 * Provides a fresh entity copy of a desired type.
	 * <p>
	 * Shortcut to {@link EntityBeanFactory#getEntityCopy(Class, boolean)}.
	 * @param <D>
	 * @param entityType
	 * @param makeUnique
	 * @return New entity copy
	 */
	protected final <D extends IEntity> D create(Class<D> entityType, boolean makeUnique) {
		return entityBeanFactory.getEntityCopy(entityType, makeUnique);
	}

	/**
	 * Shortcut to {@link EntityBeanFactory#getAllEntityCopies(Class)}.
	 * @param <D>
	 * @param entityType
	 * @return set of entities
	 */
	protected final <D extends IEntity> Set<D> getAll(Class<D> entityType) {
		return entityBeanFactory.getAllEntityCopies(entityType);
	}

	/**
	 * Persists the given entity to the datastore returning the persisted entity.
	 * @param <D>
	 * @param entity
	 * @return The persisted entity
	 */
	protected final <D extends IEntity> D persist(D entity) {
		return entityDao.persist(entity);
	}

	/**
	 * Purges the given entity from the datastore.
	 * @param <D>
	 * @param entity
	 */
	protected final <D extends IEntity> void purge(D entity) {
		entityDao.purge(entity);
	}

	/**
	 * Convenience method that creates an entity copy via the given mock entity
	 * factory the persists it via the given entity dao.
	 * @param <D>
	 * @param entityType
	 * @param makeUnique
	 * @return The created and persisted entity
	 */
	protected final <D extends IEntity> D createAndPersist(Class<D> entityType, boolean makeUnique) {
		return entityDao.persist(entityBeanFactory.getEntityCopy(entityType, makeUnique));
	}

	@Override
	public boolean supportsPaging() {
		return true; // true by default
	}

	@Override
	public void persistDependentEntities() {
		// base impl no-op
	}

	@Override
	public void purgeDependentEntities() {
		// base impl no-op
	}

	@Override
	public void makeUnique(E e) {
		EntityBeanFactory.makeBusinessKeyUnique(e);
	}

	@Override
	public final void teardownTestEntity(E e) {
		entityDao.purge(e);
	}

	@Override
	public void verifyLoadedEntityState(E e) throws Exception {
		if(e instanceof INamedEntity) {
			Assert.assertNotNull(((INamedEntity) e).getName(), "The name property is null");
		}
	}

	@Override
	public void alterTestEntity(E e) {
		if(e instanceof INamedEntity) {
			((INamedEntity) e).setName("altered");
		}
	}

	@Override
	public void verifyEntityAlteration(E e) throws Exception {
		if(e instanceof INamedEntity) {
			Assert.assertTrue("altered".equals(((INamedEntity) e).getName()), "Named entity alteration does not match");
		}
	}

	@Override
	public String toString() {
		return entityClass().toString();
	}

	@Override
	public ISelectNamedQueryDef[] getQueriesToTest() {
		// default is no named queries
		return null;
	}

	@Override
	public IQueryParam[] getParamsForTestQuery(ISelectNamedQueryDef qdef) {
		// default in no params
		return null;
	}

	@Override
	public Sorting getSortingForTestQuery(ISelectNamedQueryDef qdef) {
		// default no sorting
		return null;
	}
}
