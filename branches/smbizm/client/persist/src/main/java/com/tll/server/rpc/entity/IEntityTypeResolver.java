/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.common.model.IEntityType;

/**
 * IEntityTypeResolver
 * @author jpk
 */
public interface IEntityTypeResolver {

	/**
	 * Resolves the given {@link Class} to an {@link IEntityType}.
	 * @param clz the entity class type
	 * @return the resolved {@link IEntityType}
	 * @throws IllegalArgumentException when the entity class type can't be
	 *         resolved.
	 */
	IEntityType resolveEntityType(Class<?> clz) throws IllegalArgumentException;

	/**
	 * Resolves the given {@link IEntityType} to a {@link Class}.
	 * @param entityType the entity type
	 * @return the resolved entity class type
	 * @throws IllegalArgumentException when the entity type can't be resolved.
	 */
	Class<?> resolveEntityClass(IEntityType entityType) throws IllegalArgumentException;

	/**
	 * Resolves the given [server-side] entity object instance to the proper
	 * entity class. This is necessary when considering how orm frameworks
	 * sometimes have proxy ojbects for loaded entities.
	 * @param entity the [server-side] entity instance
	 * @return the resolved entity class type
	 * @throws IllegalArgumentException when the entity type can't be resolved.
	 */
	// Class<?> resolveEntityClass(Object entity) throws IllegalArgumentException;
}
