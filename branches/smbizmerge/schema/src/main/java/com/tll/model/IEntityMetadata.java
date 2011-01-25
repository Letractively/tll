/**
 * The Logic Lab
 * @author jpk
 * @since Mar 14, 2010
 */
package com.tll.model;

/**
 * Holds key facts about a particular entity hierarchy (set of model types).
 * @author jpk
 */
public interface IEntityMetadata {

	/**
	 * Determines if the given class is an entity class.
	 * @param claz class to test
	 * @return true/false
	 */
	boolean isEntityType(Class<?> claz);
	
	/**
	 * Gets the entity id. (This avoids having to use reflection).
	 * @param entity entity instance
	 * @return entity id
	 */
	Object getId(Object entity);

	/**
	 * Gets the proper entity class given an object ref.
	 * @param entity the object ref
	 * @return the "proper" corresponding entity class.
	 */
	Class<?> getEntityClass(Object entity);

	/**
	 * Obtains the "root" entity class given an entity class.
	 * <p>
	 * The root entity class is relevant when we have an ORM related inheritance
	 * strategy applied to a family of like entities that extend from a common
	 * entity class.
	 * <p>
	 * E.g.: Asp, Isp, Merchant and Customer all extend from the Account entity.
	 * Therefore, the root entity is Account.
	 * @param entityClass An entity type to check
	 * @return The root entity type of the given entity type.
	 */
	Class<?> getRootEntityClass(Class<?> entityClass);

	/**
	 * Gets the entity type descriptor.
	 * @param entity entity instance
	 * @return presentation worthy descriptor
	 */
	String getEntityTypeDescriptor(Object entity);

	/**
	 * Gets a descriptor based on the current entity state as well as its type.
	 * @param entity entity instance
	 * @return presentation worthy descriptor
	 */
	String getEntityInstanceDescriptor(Object entity);
}
