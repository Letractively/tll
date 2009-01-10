/**
 * The Logic Lab
 * @author jpk
 * Apr 16, 2008
 */
package com.tll.client.model;

import com.tll.IMarshalable;
import com.tll.client.bind.IBindable;
import com.tll.client.bind.PropertyChangeSupport;
import com.tll.model.schema.PropertyType;

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

	/**
	 * Sets the <em>aggregated</em> {@link PropertyChangeSupport} instance
	 * relative to a designated <em>root</em> {@link Model}.
	 * @param changeSupport
	 */
	void setPropertyChangeSupport(PropertyChangeSupport changeSupport);
}
