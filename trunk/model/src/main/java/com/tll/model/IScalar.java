/**
 * 
 */
package com.tll.model;

import java.util.Map;

/**
 * IScalar - Definition for an arbitrary set of data retrieved from the data
 * store.
 * @author jpk
 */
public interface IScalar {

	/**
	 * @param propertyName
	 * @return The working property path
	 */
	// TODO make better
	String getPropertyPath(String propertyName);

	/**
	 * @return The type from which this scalar is derived. May be
	 *         <code>null</code>.
	 *         <p>
	 *         <strong>NOTE: </strong>This is used in the UI layer for generating
	 *         ref keys which is critical for MVC view resolution.
	 */
	Class<? extends IEntity> getRefType();

	/**
	 * @return The scalar results in map form whose keys are the query alias names
	 *         and the values are the corres. values.
	 */
	Map<String, Object> getTupleMap();
}
