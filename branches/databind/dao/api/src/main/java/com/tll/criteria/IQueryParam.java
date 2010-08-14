package com.tll.criteria;

import com.tll.IMarshalable;
import com.tll.schema.PropertyType;
import com.tll.util.IPropertyNameProvider;

/**
 * QueryParam - Needed to resolve query param types.
 * @author jpk
 */
public interface IQueryParam extends IPropertyNameProvider, IMarshalable {

	/**
	 * @return The property type of the query param necessary for server side type
	 *         coercion.
	 */
	PropertyType getType();

	/**
	 * @return The property value.
	 */
	Object getValue();
}