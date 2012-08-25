/**
 * The Logic Lab
 * @author jpk Dec 25, 2007
 */
package com.tll.model;

import java.util.Set;

/**
 * IEntityProvider - Definition for an object to provide entities of particular
 * type or key.
 * @author jpk
 */
public interface IEntityProvider {

	/**
	 * Get the entity of the given type.
	 * @param <E>
	 * @param entityType The entity type
	 * @param key The primary key
	 * @return The entity if present or <code>null</code> if not.
	 */
	<E> E getEntity(Class<E> entityType, Object key);

	/**
	 * Get all entities of the given type and all entities whose type derives from
	 * the given type.
	 * @param <E>
	 * @param type
	 * @return All entities matching the given type
	 */
	<E> Set<E> getEntitiesByType(Class<E> type);

	/**
	 * Gets the single entity of the given type or the single entity whose type
	 * derives from the given type. If more than one match is found, an exception
	 * is thrown.
	 * @param <E>
	 * @param type The entity type
	 * @return The entity if present or <code>null</code> if not.
	 * @throws IllegalStateException When more than one entity exists that satisfy
	 *         the given type.
	 */
	<E> E getEntityByType(Class<E> type) throws IllegalStateException;
}
