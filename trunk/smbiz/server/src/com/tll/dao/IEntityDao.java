package com.tll.dao;

import java.util.Collection;
import java.util.List;

import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.model.IEntity;
import com.tll.model.key.BusinessKey;
import com.tll.model.key.PrimaryKey;

/**
 * IEntityDao - DAO definition for {@link IEntity}s.
 * @author jpk
 */
public interface IEntityDao<E extends IEntity> extends IDao {

	/**
	 * The class of the entity managed by this DAO.
	 * @return the entity class
	 */
	Class<E> getEntityClass();

	/**
	 * Loads a single entity specified by a primary key.
	 * @param key the primary key
	 * @return the entity
	 */
	E load(PrimaryKey<? extends E> key);

	/**
	 * Loads a single entity specified by a business key.
	 * @param key the primary key
	 * @return the entity
	 */
	E load(BusinessKey<? extends E> key);

	/**
	 * Returns all the entities managed by this DAO. This method will only include
	 * those entities that are not in the deleted state.
	 * @return all the entities managed by this DAO
	 */
	List<E> loadAll();

	/**
	 * Creates or updates the specified entity in the persistence store.
	 * <p>
	 * IMPT: The returned entity represents the proper detached instance as
	 * version increments have been done. The entity parameter remains un-altered!
	 * @param entity the entity to persist
	 * @return the merged persistence context instance of the entity.
	 */
	E persist(E entity);

	/**
	 * Updates all the entities specifed in the input. This method should be used
	 * for batch updating because it is much more efficient than iterating
	 * manually over a large data set.
	 * @param entities Collection of entities to be updated
	 * @return separate collection of the resultant merged entities or
	 *         <code>null</code> if the entities argument is <code>null</code>.
	 */
	Collection<E> persistAll(Collection<E> entities);

	/**
	 * Physical deletion of the specified entity. Use this method with caution as
	 * the entity will be deleted forever!
	 * @param entity the entity to be deleted
	 */
	void purge(E entity);

	/**
	 * Physical deletion of all entities specified in the input. Use this method
	 * with caution as the entities will be deleted forever!
	 * @param entities Collection of entities to be purged
	 */
	void purgeAll(Collection<E> entities);

	/**
	 * Finds matching entities given criteria.
	 * @param criteria May not be <code>null</code>.
	 * @param sorting
	 * @return List of entities or empty list if none found
	 * @throws InvalidCriteriaException When the criteria is <code>null</code>
	 *         or found to be invalid.
	 */
	List<E> findEntities(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException;

	/**
	 * Use when the expected result is a single entity.
	 * @param criteria The criteria. May NOT be <code>null</code>.
	 * @return The found entity or <code>null</code> if not found or if more
	 *         than one entity satisfies the criteria.
	 * @throws InvalidCriteriaException When the criteria is <code>null</code>
	 *         or found to be invalid.
	 */
	E findEntity(ICriteria<? extends E> criteria) throws InvalidCriteriaException;

	/**
	 * Returns a list of entities that satisfy the given criteria. This method
	 * will return an empty list if no entities match the criteria.
	 * @param criteria The criteria. May NOT be <code>null</code>.
	 * @param sorting The sorting directive. May be <code>null</code>.
	 * @return List of entities or empty list if none found
	 * @throws InvalidCriteriaException When the criteria is <code>null</code>
	 *         or found to be invalid.
	 */
	List<SearchResult<E>> find(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException;

	/**
	 * Returns a list of entities that satisfy the given id list. This method will
	 * return an empty list if no entities match the criteria.
	 * @param ids the list id entity ids
	 * @return List of entities or empty list if none found
	 */
	List<E> findByIds(List<Integer> ids, Sorting sorting);

	/**
	 * Retrieves the ids of the entities that match the given criteria. Used for
	 * id based list handling.
	 * @param criteria The criteria. May NOT be <code>null</code>.
	 * @param sorting The sorting directive. May be <code>null</code>.
	 * @return List of ids of matching entities, empty list if no matching
	 *         entities are found.
	 * @throws InvalidCriteriaException When the criteria is <code>null</code>
	 *         or found to be invalid.
	 */
	List<Integer> getIds(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException;

	/**
	 * Retrieves entities from a collection of ids. Used for id based list
	 * handling.
	 * @param entityClass The entity class the ids represent.
	 * @param ids List of ids of the entities to retrieve.
	 * @param sorting the sorting directive May be null in which case the sorting
	 *        of the results is "undefined".
	 * @return list of matching entities.
	 */
	List<E> getEntitiesFromIds(Class<? extends E> entityClass, Collection<Integer> ids, Sorting sorting);

	/**
	 * Returns a sub-set of results using record set paging.
	 * @param criteria The required criteria that generates the record set
	 * @param sorting The required sorting directive that forces the retrieved
	 *        record set to be sorted. Sorting is required to be able to provide
	 *        deterministic results.
	 * @param offset The result set index where at which results are retrieved
	 * @param pageSize The number of results to retrieve at the given offset
	 * @throws InvalidCriteriaException When the criteria is <code>null</code>
	 *         or found to be invalid or when the sorting directive is
	 *         <code>null</code>.
	 */
	IPageResult<SearchResult<E>> getPage(ICriteria<? extends E> criteria, Sorting sorting, int offset, int pageSize)
			throws InvalidCriteriaException;

	/**
	 * Flush the persistence context
	 */
	void flush();

	/**
	 * Clear the persistence context
	 */
	void clear();

}
