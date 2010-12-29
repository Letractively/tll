package com.tll.service.entity;

import java.util.Collection;
import java.util.List;

import javax.validation.ConstraintViolationException;

import com.tll.criteria.Criteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.EntityExistsException;
import com.tll.dao.EntityNotFoundException;
import com.tll.listhandler.IListingDataProvider;
import com.tll.model.IEntity;
import com.tll.model.bk.IBusinessKey;
import com.tll.service.IService;
import com.tll.sort.Sorting;

/**
 * IEntityService - Entity service definition.
 * <p>
 * <b>NOTE: </b>All methods are subject to throwing a
 * <code>org.springframework.dao.DataAccessException</code>
 * @author jpk
 * @param <E> the entity type
 */
public interface IEntityService<E extends IEntity> extends IListingDataProvider, IService {

	/**
	 * Returns the class of the entity managed by this service.
	 * @return the entity class
	 */
	Class<E> getEntityClass();

	/**
	 * Load by primary key.
	 * @param pk the primary key of the entity to load
	 * @return the loaded entity
	 * @throws EntityNotFoundException
	 */
	E load(Object pk) throws EntityNotFoundException;

	/**
	 * Load by business key.
	 * @param key
	 * @return the loaded entity
	 * @throws EntityNotFoundException
	 */
	E load(IBusinessKey<E> key) throws EntityNotFoundException;

	/**
	 * Returns all of the entities in the system managed by this service.
	 * @return all of the entities
	 */
	List<? extends E> loadAll();

	/**
	 * Updates an instance of this entity. The input entity should have a valid
	 * id.
	 * @param entity the entity to persist
	 * @return the persisted entity
	 * @throws EntityExistsException if this entity violates a uniqueness
	 *         constraint
	 * @throws ConstraintViolationException When the entity validation check
	 *         fails.
	 */
	E persist(E entity) throws EntityExistsException, ConstraintViolationException;

	/**
	 * This method persists all of the entities within the collection in a batch
	 * fashion. Validation will be performed using this method.
	 * @param entities Collection of entities to update
	 * @return separate collection of the persisted entities or <code>null</code>
	 *         if the entities argument is <code>null</code>.
	 * @throws ConstraintViolationException When one or more entites are found to
	 *         be invalid.
	 */
	Collection<E> persistAll(Collection<E> entities) throws ConstraintViolationException;

	/**
	 * Removes the specified entity from the system. The input entity should have
	 * a valid id.
	 * @param entity the entity to purge
	 * @throws EntityNotFoundException
	 */
	void purge(E entity) throws EntityNotFoundException;

	/**
	 * Removes the collection of entities from the system. If an entity does not
	 * exist in the system, it will be ignored.
	 * @param entities the collection of entities to purge
	 */
	void purgeAll(Collection<E> entities);

	/**
	 * Finds entities given criteria.
	 * @param criteria
	 * @param sorting
	 * @return List of matching entities or an empty list if no matches found.
	 *         (Never <code>null</code>).
	 * @throws InvalidCriteriaException Upon malformed or invlaid criteria.
	 */
	List<E> findEntities(Criteria<E> criteria, Sorting sorting) throws InvalidCriteriaException;
}
