/**
 * The Logic Lab
 * @author jpk
 * Jan 19, 2009
 */
package com.tll.model.key;

import java.util.Collection;

import com.tll.model.BusinessKeyNotDefinedException;
import com.tll.model.IEntity;

/**
 * IBusinessKeyFactory
 * @author jpk
 */
public interface IBusinessKeyFactory {

	/**
	 * Provides all defined business key definitions for the given entity type.
	 * @param <E> The entity type
	 * @param entityClass The entity class
	 * @return All defined business key definitions
	 * @throws BusinessKeyNotDefinedException Whe no business keys are defined for
	 *         the given entity type.
	 */
	<E extends IEntity> IBusinessKeyDefinition<E>[] definitions(Class<E> entityClass)
			throws BusinessKeyNotDefinedException;

	/**
	 * Creates new and empty business keys for the given entity type.
	 * @param <E> The entity type
	 * @param entityClass The entity class
	 * @return Array of empty business keys.
	 * @throws BusinessKeyNotDefinedException When no business keys are defined
	 *         for the given entity type.
	 */
	<E extends IEntity> BusinessKey<E>[] create(Class<E> entityClass) throws BusinessKeyNotDefinedException;

	/**
	 * Creates a new and empty business key for the given entity type and business
	 * key name.
	 * @param <E> The entity type.
	 * @param entityClass The entity class
	 * @param businessKeyName The business key name
	 * @return The created business key
	 * @throws BusinessKeyNotDefinedException When no business keys exist for the
	 *         given entity type or no business key is found having the specified
	 *         name.
	 */
	<E extends IEntity> BusinessKey<E> create(Class<E> entityClass, String businessKeyName)
			throws BusinessKeyNotDefinedException;

	/**
	 * Creates all defined business keys with state extracted from the given
	 * entity.
	 * @param <E> The entity type
	 * @param entity The entity from which business keys are created
	 * @return Array of business keys with state extracted from the entity.
	 * @throws BusinessKeyNotDefinedException When no business keys are defined
	 *         for the given entity type.
	 */
	<E extends IEntity> BusinessKey<E>[] create(E entity) throws BusinessKeyNotDefinedException;

	/**
	 * Creates a single business key for the given entity type and business key
	 * name.
	 * @param <E> The entity type.
	 * @param entity The entity instance
	 * @param businessKeyName The business key name
	 * @return The created business key
	 * @throws BusinessKeyNotDefinedException When no business keys exist for the
	 *         given entity type or no business key is found having the specified
	 *         name.
	 */
	<E extends IEntity> BusinessKey<E> create(E entity, String businessKeyName) throws BusinessKeyNotDefinedException;

	/**
	 * Updates the the given entity with the property values held in the given
	 * business keys.
	 * @param <E> The entity type
	 * @param entity The entity instance
	 * @param bks The business keys whose state is applied to the given entity.
	 */
	<E extends IEntity> void apply(E entity, BusinessKey<E>[] bks);

	/**
	 * Ensures all entities w/in the collection are unique against oneanother
	 * based on the defined business keys for corresponding the entity type.
	 * @param <E> The entity type
	 * @param clctn The entity collection. May not be <code>null</code>.
	 * @return <code>true</code> if the entity collection elements are unique
	 *         against oneanother.
	 */
	<E extends IEntity> boolean isBusinessKeyUnique(Collection<E> clctn);
}