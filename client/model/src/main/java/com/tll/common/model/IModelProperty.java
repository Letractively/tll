/**
 * The Logic Lab
 * @author jpk
 * Apr 16, 2008
 */
package com.tll.common.model;

import com.tll.IMarshalable;
import com.tll.common.bind.IBindable;
import com.tll.schema.IPropertyNameProvider;
import com.tll.schema.PropertyType;

/**
 * IModelProperty - Represents a single model property.
 * @author jpk
 */
public interface IModelProperty extends IPropertyNameProvider, IBindable, IMarshalable {

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

	/**
	 * Generic way to set the model property value.
	 * @param value The value to set
	 * @throws IllegalArgumentException
	 */
	void setValue(Object value) throws IllegalArgumentException;
}
