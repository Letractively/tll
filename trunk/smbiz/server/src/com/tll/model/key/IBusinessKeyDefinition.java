package com.tll.model.key;

import com.tll.model.IEntity;

/**
 * IBusinessKeyDefinition - Defines a business key for given entity type.
 * @author jpk
 */
public interface IBusinessKeyDefinition<E extends IEntity> {

	/**
	 * @return The entity type
	 */
	Class<E> getType();

	/**
	 * @return The business key name.
	 */
	String getBusinessKeyName();

	/**
	 * @return The OGNL formatted property names that make up this business key.
	 */
	String[] getPropertyNames();
}