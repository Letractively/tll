package com.tll.model;

/**
 * Interface for entities that have names.
 * @author jpk
 */
public interface INamedEntity extends IEntity {
	public static final String NAME = "name";
	
	/**
	 * Returns the name for this entity.
	 * @return the name of this entity
	 */
	String getName();
	
	/**
	 * Sets the name for this entity.
	 * @param name the name of this entity
	 */
	void setName(String name);
}
