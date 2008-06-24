package com.tll.model;

import com.tll.model.key.BusinessKey;
import com.tll.model.key.PrimaryKey;
import com.tll.util.IDescriptorProvider;

/**
 * Base interface for all entities.
 * @author jpk
 */
public interface IEntity extends IPersistable, IVersionSupport, IDescriptorProvider {

	/**
	 * The name of the id primary key field
	 */
	public static final String PK_FIELDNAME = "id";

	/**
	 * @return The <code>Class</code> of this entity. This method is preferred
	 *         to {@link Object#getClass()} method as the DAO layer employs proxy
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
	 */
	boolean isGenerated();

	/**
	 * @return A state independent UI presentable name of the entity
	 */
	String typeName();

	/**
	 * @return The primary key for this entity.
	 */
	PrimaryKey getPrimaryKey();

	/**
	 * @return All defined business keys for this entity type holding the current
	 *         state of this entity.
	 * @throws BusinessKeyNotDefinedException When there are no defined business
	 *         keys for this entity type.
	 */
	BusinessKey[] getBusinessKeys() throws BusinessKeyNotDefinedException;
}
