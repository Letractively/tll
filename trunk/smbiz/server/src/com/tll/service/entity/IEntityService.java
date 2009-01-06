package com.tll.service.entity;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.hibernate.validator.InvalidStateException;

import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.Sorting;
import com.tll.listhandler.IListHandlerDataProvider;
import com.tll.model.IEntity;
import com.tll.model.key.BusinessKey;
import com.tll.model.key.PrimaryKey;
import com.tll.service.IService;

/**
 * Base entity interface.
 * @author jpk
 * @param <E>
 */
public interface IEntityService<E extends IEntity> extends IListHandlerDataProvider<E>, IService {

	/**
	 * Returns the class of the entity managed by this service.
	 * @return the entity class
	 */
	Class<E> getEntityClass();

	/**
	 * Load by primary key.
	 * @param key
	 * @return the loaded entity
	 * @throws EntityNotFoundException
	 */
	E load(PrimaryKey<? extends E> key) throws EntityNotFoundException;

	/**
	 * Load by business key.
	 * @param key
	 * @return the loaded entity
	 * @throws EntityNotFoundException
	 */
	E load(BusinessKey<? extends E> key) throws EntityNotFoundException;

	/**
	 * Returns all of the entities in the system managed by this service.
	 * @return all of the entities
	 */
	List<E> loadAll();

	/**
	 * Returns a list of entities based on a list of ids.
	 * @param ids the ids of the entities to retrieve
	 * @param sorting
	 * @return List of entities
	 */
	List<E> loadByIds(List<Integer> ids, Sorting sorting);

	/**
	 * Updates an instance of this entity. The input entity should have a valid
	 * id.
	 * @param entity the entity to persist
	 * @return the persisted entity
	 * @throws EntityExistsException if this entity violates a uniqueness
	 *         constraint
	 * @throws InvalidStateException When the entity validation check fails.
	 */
	E persist(E entity) throws EntityExistsException, InvalidStateException;

	/**
	 * This method persists all of the entities within the collection in a batch
	 * fashion. Validation will be performed using this method.
	 * @param entities Collection of entities to update
	 * @return separate collection of the persisted entities or <code>null</code>
	 *         if the entities argument is <code>null</code>.
	 */
	Collection<E> persistAll(Collection<E> entities);

	/**
	 * Removes the specified entity from the system. The input entity should have
	 * a valid id.
	 * @param entity the entity to purge
	 */
	void purge(E entity);

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
	List<E> findEntities(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException;
}
