package com.tll.client.model;

import com.tll.model.schema.PropertyMetadata;

/**
 * IPropertyValue - A property binding that whose value is non-relational.
 * @author jpk
 */
public interface IPropertyValue extends IPropertyBinding {

	/**
	 * @return The property meta data
	 */
	PropertyMetadata getMetadata();

	/**
	 * Set the property value.
	 * @param value The value to be set
	 */
	void setValue(Object value);

	/**
	 * Clears the property value
	 */
	void clear();

	/**
	 * Copies this property value
	 */
	IPropertyValue copy();

}