package com.tll.common.model;

import com.tll.model.PropertyMetadata;

/**
 * IPropertyValue - A single non-relational model property.
 * @author jpk
 */
public interface IPropertyValue extends IModelProperty {

	/**
	 * @return The property meta data
	 */
	PropertyMetadata getMetadata();

	/**
	 * Set the property value.
	 * @param value The value to be set
	 * @throws IllegalArgumentException When the value is invalid
	 */
	@Override
	void setValue(Object value) throws IllegalArgumentException;

	/**
	 * Clears the property value
	 */
	void clear();

	/**
	 * Copies this property value
	 * @return copied property value
	 */
	IPropertyValue copy();

}