package com.tll.client.model;

/**
 * IPropertyValue - A property binding that whose value is non-relational.
 * @author jpk
 */
public interface IPropertyValue extends IPropertyBinding {

	/**
	 * @return The property meta data
	 */
	PropertyData getPropertyData();

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