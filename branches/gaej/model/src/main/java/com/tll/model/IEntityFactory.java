package com.tll.model;

/**
 * This class should be used to create all new entities within the system. It will inject the
 * surrogate primary key into the object to provide an immutable key for the lifetime of an entity
 * in the application.
 * @author jpk
 */
public interface IEntityFactory {

	/**
	 * Creates an entity of the given type.  No properties are set. 
	 * @param <E>
	 * @param entityClass the type of entity to create
	 * @return Newly created entity
	 * @throws IllegalStateException when the entity can't be created for any
	 *         reason
	 */
	<E extends IEntity> E createEntity(Class<E> entityClass) throws IllegalStateException;

	/**
	 * Responsible for setting a datastore compliant primary key for the given entity instance. 
	 * @param <E>
	 * @param entity the entity instance whose primary key is to be set
	 */
	<E extends IEntity> void assignPrimaryKey(E entity);
}
