package com.tll.criteria;

import com.tll.client.IMarshalable;
import com.tll.client.model.IPropertyNameProvider;
import com.tll.model.schema.PropertyType;

/**
 * QueryParam - Needed to resolve query param types.
 * @author jpk
 */
public interface IQueryParam extends /*Serializable*/IPropertyNameProvider, IMarshalable {

	PropertyType getType();

	Object getValue();
}