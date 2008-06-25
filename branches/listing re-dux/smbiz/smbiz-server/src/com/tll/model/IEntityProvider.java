/**
 * The Logic Lab
 * @author jpk Dec 25, 2007
 */
package com.tll.model;

import java.util.List;

import com.tll.model.key.IPrimaryKey;

/**
 * IEntityProvider - Definition for an object to provide entities of particular
 * type.
 * @author jpk
 */
public interface IEntityProvider {

	/**
	 * Get the entity of the given type.
	 * @param key The primary key
	 * @return The entity if present or <code>null</code> if not.
	 */
	<E extends IEntity> E getEntity(IPrimaryKey<E> key);

	/**
	 * Does the entity exist in this provider?
	 * @param key
	 * @return true/false
	 */
	boolean hasEntity(IPrimaryKey<? extends IEntity> key);

	/**
	 * Get all entities of the given type and all entities whose type derives from
	 * the given type.
	 * @param <E>
	 * @param type
	 * @return The entity if present or <code>null</code> if not.
	 */
	<E extends IEntity> List<? extends E> getEntitiesByType(Class<E> type);

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
	<E extends IEntity> E getEntityByType(Class<E> type) throws IllegalStateException;
}
