package com.tll.model;


/**
 * Indicates that the entity is related to a particular {@link Account}.
 * 
 * @author jpk
 */
public interface IAccountRelatedEntity {

	/**
	 * @return The id of the account to which this entity is related.
	 */
	Long accountId();
}
