package com.tll.model.key;

import com.tll.model.IEntity;

/**
 * IBusinessKeyDefinition - Defines a business key for given entity type.
 * @author jpk
 */
public interface IBusinessKeyDefinition {

	/**
	 * @return The entity type
	 */
	Class<? extends IEntity> getEntityClass();

	/**
	 * @return The key name.
	 */
	String getKeyName();

	/**
	 * @return The field names
	 */
	String[] getFieldNames();
}