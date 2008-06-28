/**
 * The Logic Lab
 * @author jpk
 * Apr 16, 2008
 */
package com.tll.client.model;

import com.tll.client.IMarshalable;
import com.tll.model.schema.PropertyType;

/**
 * IPropertyBinding - Represents a single model property.
 * @author jpk
 */
public interface IPropertyBinding extends IPropertyNameProvider, IMarshalable {

	/**
	 * @return The property type.
	 */
	PropertyType getType();

	/**
	 * Generic way to obtain the bound value for this property binding. Should
	 * only really be used for client/server marshaling.
	 * @return The raw bound value of the bound property.
	 */
	Object getValue();
}
