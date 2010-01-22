package com.tll.model;


/**
 * Indicates that the entity is related to a particular {@link Account}.
 * 
 * @author jpk
 */
public interface IAccountRelatedEntity {

	/**
	 * @return The primary key of the account to which this entity is related.
	 */
	IPrimaryKey accountKey();
}
