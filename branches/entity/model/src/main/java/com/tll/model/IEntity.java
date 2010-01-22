package com.tll.model;

import com.tll.IDescriptorProvider;
import com.tll.ITypeDescriptorProvider;

/**
 * IEntity - Fundamental type for all [server side] entities.
 * @author jpk
 */
public interface IEntity extends IPersistable, IVersionSupport, IDescriptorProvider, ITypeDescriptorProvider {

	/**
	 * The name of the id primary key field.
	 */
	static final String PK_FIELDNAME = "id";

	/**
	 * @return The <code>Class</code> of this entity. This method is preferred to
	 *         {@link Object#getClass()} method as the DAO layer may employ proxy
	 *         entity objects.
	 */
	Class<? extends IEntity> entityClass();

	/**
	 * @return The primary key
	 */
	IPrimaryKey getPrimaryKey();
	
	/**
	 * Sets the primary key.
	 * @param pk The primary key to set
	 */
	void setPrimaryKey(IPrimaryKey pk);
	
	/**
	 * True if the identifier for this object was assigned at creation.<br>
	 * Note that generated entities are expected to provide a constructor that
	 * takes the initial id as an argument.<br>
	 * Note that the generated status of an entity does *not* imply a specific
	 * persistence status. All generated entities start as transient instances,
	 * but are expected to be persisted shortly thereafter.
	 * @return true/false
	 */
	//boolean isGenerated();
	
	/**
	 * This method <b>must only</b> be called when a new entity is created and the
	 * id is generated. It will set the id and set the generated flag to true.
	 * @param pk the primary key to set
	 */
	//void setGenerated(IPrimaryKey pk);
}
