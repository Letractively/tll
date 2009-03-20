package com.tll.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;

import com.tll.criteria.ICriteria;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.NameKey;
import com.tll.model.key.PrimaryKey;

/**
 * IEntityDao - DAO definition for {@link IEntity}s.
 * <p>
 * <b>NOTE:</b> All dao methods are subject to throwing unchecked exceptions.
 * These exceptions <em>should</em> (but hasn't been verified) all derive from
 * {@link PersistenceException}.
 * @author jpk
 */
public interface IEntityDao extends IDao {

	/**
	 * Loads a single entity specified by a primary key.
	 * @param <E> The entity type
	 * @param key the primary key
	 * @return the entity
	 * @throws EntityNotFoundException
	 */
	<E extends IEntity> E load(PrimaryKey<E> key) throws EntityNotFoundException;

	/**
	 * Loads a single entity specified by a business key.
	 * @param <E> The entity type
	 * @param key the primary key
	 * @return the entity
	 * @throws EntityNotFoundException
	 */
	<E extends IEntity> E load(IBusinessKey<E> key) throws EntityNotFoundException;

	/**
	 * Loads the named entity by a given name.
	 * @param <N> The named entity type
	 * @param nameKey the name key
	 * @return the never <code>null</code> named entity (unless an exception is
	 *         thrown).
	 * @throws EntityNotFoundException When no entities satisfy the name key.
	 * @throws NonUniqueResultException When more than one entity satisfies the
	 *         given name key.
	 */
	<N extends INamedEntity> N load(NameKey<N> nameKey) throws EntityNotFoundException, NonUniqueResultException;

	/**
	 * Returns all the entities managed by this DAO. This method will only include
	 * those entities that are not in the deleted state.
	 * @param <E> The entity type
	 * @param entityType Then entity class
	 * @return Never <code>null</code> list of all found entities of the given
	 *         type which may be empty.
	 */
	<E extends IEntity> List<E> loadAll(Class<E> entityType);

	/**
	 * Creates or updates the specified entity in the persistence store.
	 * <p>
	 * IMPT:</b> The returned entity is the subsequent detached instance after the
	 * persist operation is performed. The entity parameter remains un-altered!
	 * @param <E> The entity type
	 * @param entity the entity to persist
	 * @return the merged persistence context instance of the entity.
	 */
	<E extends IEntity> E persist(E entity);

	/**
	 * Updates all the entities specifed in the input. This method should be used
	 * for batch updating because it is much more efficient than iterating
	 * manually over a large data set.
	 * @param <E> The entity type
	 * @param entities Collection of entities to be updated
	 * @return separate collection of the resultant merged entities or
	 *         <code>null</code> if the entities argument is <code>null</code>.
	 */
	<E extends IEntity> Collection<E> persistAll(Collection<E> entities);

	/**
	 * Physical deletion of the specified entity. Use this method with caution as
	 * the entity will be deleted forever!
	 * @param <E> The entity type
	 * @param entity the entity to be deleted
	 */
	<E extends IEntity> void purge(E entity);

	/**
	 * Physical deletion of all entities specified in the input. Use this method
	 * with caution as the entities will be deleted forever!
	 * @param <E> The entity type
	 * @param entities Collection of entities to be purged
	 */
	<E extends IEntity> void purgeAll(Collection<E> entities);

	/**
	 * Use when the expected result is a single entity.
	 * @param <E> The entity type
	 * @param criteria The criteria. May NOT be <code>null</code>.
	 * @return The non-<code>null</code> found entity.
	 * @throws InvalidCriteriaException When the criteria is <code>null</code> or
	 *         invalid.
	 * @throws EntityNotFoundException When no entities satisfy the given criteria
	 * @throws NonUniqueResultException When more than one entity satisfies the
	 *         given criteria
	 */
	<E extends IEntity> E findEntity(ICriteria<E> criteria) throws InvalidCriteriaException, EntityNotFoundException,
			NonUniqueResultException;

	/**
	 * Finds matching entities given criteria.
	 * @param <E> The entity type
	 * @param criteria May not be <code>null</code>.
	 * @param sorting
	 * @return List of entities or empty list if none found
	 * @throws InvalidCriteriaException When the criteria is <code>null</code> or
	 *         found to be invalid.
	 */
	<E extends IEntity> List<E> findEntities(ICriteria<E> criteria, Sorting sorting) throws InvalidCriteriaException;

	/**
	 * Generic criteria driven search method.
	 * <p>
	 * The results may either be scalar (non-entity result elements) or entity
	 * based depending on the given criteria.
	 * @param <E> The entity type
	 * @param criteria The criteria. May NOT be <code>null</code>.
	 * @param sorting The sorting directive. May be <code>null</code>.
	 * @return A never <code>null</code> list of matching search results which may
	 *         be empty if no matching results are found.
	 * @throws InvalidCriteriaException When the criteria is <code>null</code> or
	 *         found to be invalid.
	 */
	<E extends IEntity> List<SearchResult<E>> find(ICriteria<E> criteria, Sorting sorting)
			throws InvalidCriteriaException;

	/**
	 * Returns a list of entities that satisfy the given id list. This method will
	 * return an empty list if no entities match the criteria.
	 * @param <E> The entity type
	 * @param entityType The entity class type
	 * @param ids the list id entity ids
	 * @param sorting
	 * @return List of entities or empty list if none found
	 */
	<E extends IEntity> List<E> findByIds(Class<E> entityType, List<Integer> ids, Sorting sorting);

	/**
	 * Retrieves the ids of the entities that match the given criteria. Used for
	 * id based list handling.
	 * @param <E> The entity type
	 * @param criteria The criteria. May NOT be <code>null</code>.
	 * @param sorting The sorting directive. May be <code>null</code>.
	 * @return List of ids of matching entities, empty list if no matching
	 *         entities are found.
	 * @throws InvalidCriteriaException When the criteria is <code>null</code> or
	 *         found to be invalid.
	 */
	<E extends IEntity> List<Integer> getIds(ICriteria<E> criteria, Sorting sorting)
			throws InvalidCriteriaException;

	/**
	 * Retrieves entities from a collection of ids. Used for id based list
	 * handling.
	 * @param <E> The entity type
	 * @param entityClass The entity class the ids represent.
	 * @param ids List of ids of the entities to retrieve.
	 * @param sorting the sorting directive May be null in which case the sorting
	 *        of the results is "undefined".
	 * @return list of matching entities.
	 */
	<E extends IEntity> List<E> getEntitiesFromIds(Class<E> entityClass, Collection<Integer> ids,
			Sorting sorting);

	/**
	 * Returns a sub-set of results using record set paging.
	 * @param <E> The entity type
	 * @param criteria The required criteria that generates the record set
	 * @param sorting The required sorting directive that forces the retrieved
	 *        record set to be sorted. Sorting is required to be able to provide
	 *        deterministic results.
	 * @param offset The result set index where at which results are retrieved
	 * @param pageSize The number of results to retrieve at the given offset
	 * @return the page result
	 * @throws InvalidCriteriaException When the criteria is <code>null</code> or
	 *         found to be invalid or when the sorting directive is
	 *         <code>null</code>.
	 */
	<E extends IEntity> IPageResult<SearchResult<E>> getPage(ICriteria<E> criteria, Sorting sorting,
			int offset, int pageSize)
			throws InvalidCriteriaException;

	/**
	 * Executes a non-select named query.
	 * @param queryName
	 * @param params
	 * @return The number of affected entities.
	 */
	int executeQuery(String queryName, IQueryParam[] params);
}
