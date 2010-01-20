/**
 * The Logic Lab
 * @author jpk
 * Jan 27, 2009
 */
package com.tll.dao;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;

import com.tll.criteria.Criteria;
import com.tll.model.EntityBeanFactory;
import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.key.PrimaryKey;

/**
 * AbstractEntityDaoTestHandler
 * @param <E> The entity type
 * @author jpk
 */
public abstract class AbstractEntityDaoTestHandler<E extends IEntity> implements IEntityDaoTestHandler<E> {

	protected final Log log = LogFactory.getLog(getClass());

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
		log.debug("Creating " + (makeUnique ? "UNIQUE" : "NON-UNIQUE") + " entity of type: " + entityType);
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
	 * Loads an entity by primary key from the datastore.
	 * @param <D>
	 * @param key
	 * @return the loaded entity
	 */
	protected final <D extends IEntity> D load(PrimaryKey<D> key) {
		log.debug("Loading entity by primary key: " + key);
		return entityDao.load(key);
	}

	/**
	 * Persists the given entity to the datastore returning the persisted entity.
	 * @param <D>
	 * @param entity
	 * @return The persisted entity
	 */
	protected final <D extends IEntity> D persist(D entity) {
		log.debug("Persisting entity: " + entity);
		return entityDao.persist(entity);
	}

	/**
	 * Purges the given entity from the datastore.
	 * @param <D>
	 * @param entity
	 */
	protected final <D extends IEntity> void purge(D entity) {
		log.debug("Purging entity: " + entity);
		entityDao.purge(entity);
	}

	/**
	 * Purges the given entity from the datastore by primary key.
	 * @param <D>
	 * @param key
	 */
	protected final <D extends IEntity> void purge(PrimaryKey<D> key) {
		log.debug("Purging entity by primary key: " + key);
		entityDao.purge(key);
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
		purge(e);
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

	/*
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
	 */

	@Override
	public Criteria<E> getTestCriteria() {
		return null;
	}

	@Override
	public Sorting getTestSorting() {
		return null;
	}

	@Override
	public String getActualNameProperty() {
		// default
		return INamedEntity.NAME;
	}

	@Override
	public String toString() {
		return entityClass().toString();
	}
}
