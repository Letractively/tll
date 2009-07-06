package com.tll.model;

import com.tll.IDescriptorProvider;

/**
 * Base interface for all entities.
 * @author jpk
 */
public interface IEntity extends IPersistable, /*IVersionSupport, */IDescriptorProvider {

	/**
	 * The name of the id primary key field
	 */
	public static final String PK_FIELDNAME = "id";

	/**
	 * @return The <code>Class</code> of this entity. This method is preferred to
	 *         {@link Object#getClass()} method as the DAO layer employs proxy
	 *         entity objects.
	 */
	Class<? extends IEntity> entityClass();

	/**
	 * @return The id of the entity
	 */
	Integer getId();

	/**
	 * True if the identifier for this object was assigned at creation.<br>
	 * Note that generated entities are expected to provide a constructor that
	 * takes the initial id as an argument.<br>
	 * Note that the generated status of an entity does *not* imply a specific
	 * persistence status. All generated entities start as transient instances,
	 * but are expected to be persisted shortly thereafter.
	 * @return true/false
	 */
	boolean isGenerated();

	/**
	 * @return A state independent UI presentable name of the entity type.
	 */
	String typeName();
}
