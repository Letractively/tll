package com.tll.model;

/**
 * This class should be used to create all new entities within the system. It will inject the
 * surrogate primary key into the object to provide an immutable key for the lifetime of an entity
 * in the application.
 * @author jpk
 */
public interface IEntityFactory {

	/**
	 * Creates an entity
	 * @param <E>
	 * @param entityClass
	 * @param generate Generate an id? This is necessary in order to later persist
	 *        the entity. Creating non-generated entities should only be used for
	 *        prototyping and when it it guaranteed the created entity will not be
	 *        subject to persistence.
	 * @return The created entity
	 * @throws IllegalStateException when the entity can't be created for any
	 *         reason.
	 */
	<E extends IEntity> E createEntity(Class<E> entityClass, boolean generate) throws IllegalStateException;

	/**
	 * Sets entity properties relating to generation. Specifically, sets the <code>IEntity#id</code>
	 * and <code>IEntity#generated</code> properties. Also, sets the
	 * <code>ITimeStampEntity#dateCreated</code> property if the entity implements
	 * {@link ITimeStampEntity}. if the entity
	 * @param <E>
	 * @param entity
	 */
	<E extends IEntity> void setGenerated(E entity);
}
