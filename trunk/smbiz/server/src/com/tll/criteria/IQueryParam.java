package com.tll.criteria;

import com.tll.client.IMarshalable;
import com.tll.client.model.IPropertyNameProvider;
import com.tll.model.schema.PropertyType;

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