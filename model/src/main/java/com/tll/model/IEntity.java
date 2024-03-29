package com.tll.model;

import com.tll.IDescriptorProvider;
import com.tll.IMarshalable;
import com.tll.ITypeDescriptorProvider;

/**
 * IEntity - Fundamental type for all [server side] entities.
 * @author jpk
 */
public interface IEntity extends IMarshalable, IPersistable, IVersionSupport, IDescriptorProvider, ITypeDescriptorProvider {

	/**
	 * The name of the primary key field. This is solely the Java Bean property
	 * name and doesn't imply the name for the corresponding primary key field in
	 * a datastore.
	 */
	static final String PK_FIELDNAME = "id";

	/**
	 * @return The class of this entity. This method is preferred to
	 *         {@link Object#getClass()} method as the DAO layer may employ proxy
	 *         entity objects.
	 */
	Class<? extends IEntity> entityClass();

	/**
	 * @return The root entity class of this entity.
	 */
	Class<? extends IEntity> rootEntityClass();
	
	/**
	 * @return the entity type.
	 */
	String getEntityType();

	/**
	 * @return The surrogate primary key. Entity implementations are not required
	 *         to employ surrogate primary keys.
	 */
	Long getId();

	/**
	 * Sets the surrogate primary key.
	 * @param id The surrogate primary key to set
	 */
	void setId(Long id);

	/**
	 * True if the primary key for was assigned at object creation.<br>
	 * The generated status of an entity does *not* imply a specific persistence
	 * status rather it only means the primary key is fully set requring not other
	 * alteration. All generated entities start as transient instances, but are
	 * expected to be persisted shortly thereafter.
	 * @return true/false
	 */
	boolean isGenerated();

	/**
	 * This method <b>must only</b> be called when a new entity is created and the
	 * primary key is generated. It will set the primary key property and set the
	 * generated flag to true.
	 * @param pk the fully set primary key to set
	 */
	void setGenerated(Object pk);
}
