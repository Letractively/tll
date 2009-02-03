/**
 * The Logic Lab
 * @author jpk
 * Jan 27, 2009
 */
package com.tll.dao;

import java.util.Set;

import org.testng.Assert;

import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.mock.MockEntityFactory;

/**
 * AbstractEntityDaoTestHandler
 * @param <E> The entity type
 * @author jpk
 */
public abstract class AbstractEntityDaoTestHandler<E extends IEntity> implements IEntityDaoTestHandler<E> {

	private IEntityDao entityDao;
	private MockEntityFactory mockEntityFactory;
	
	@Override
	public void init(IEntityDao entityDao, MockEntityFactory mockEntityFactory) {
		this.entityDao = entityDao;
		this.mockEntityFactory = mockEntityFactory;
	}

	/**
	 * Provides a fresh entity copy of a desired type.
	 * <p>
	 * Shortcut to {@link MockEntityFactory#getEntityCopy(Class, boolean)}.
	 * @param <D>
	 * @param entityType
	 * @param makeUnique
	 * @return New entity copy
	 */
	protected final <D extends IEntity> D create(Class<D> entityType, boolean makeUnique) {
		return mockEntityFactory.getEntityCopy(entityType, makeUnique);
	}

	/**
	 * Shortcut to {@link MockEntityFactory#getAllEntityCopies(Class)}.
	 * @param <D>
	 * @param entityType
	 * @return
	 */
	protected final <D extends IEntity> Set<D> getAll(Class<D> entityType) {
		return mockEntityFactory.getAllEntityCopies(entityType);
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
	 * @param mockEntityFactory
	 * @param entityDao
	 * @return The created and persisted entity
	 */
	protected final <D extends IEntity> D createAndPersist(Class<D> entityType, boolean makeUnique) {
		return entityDao.persist(mockEntityFactory.getEntityCopy(entityType, makeUnique));
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
		MockEntityFactory.makeBusinessKeyUnique(e);
	}

	/*
	@Override
	public void assembleTestEntity(E e, IEntityDao entityDao, IEntityFactory entityFactory,
			MockEntityFactory mockEntityFactory) throws Exception {
		// base impl no-op
	}
	*/

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
}