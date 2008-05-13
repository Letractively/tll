/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.client.model;

import com.tll.client.IMarshalable;
import com.tll.model.schema.PropertyType;

/**
 * PropertyData - "meta" or schema info for a single model property.
 * @author jpk
 */
public class PropertyData implements IMarshalable {

	/**
	 * The property type.
	 */
	public PropertyType propertyType;

	/**
	 * Is this property managed? When managed, the bound model properties' value
	 * is automatically set.
	 */
	public boolean managed;

	/**
	 * Flag for indicating required-ness.
	 */
	public boolean required = false;

	/**
	 * The max allowed length. If <code>-1</code>, this property is NOT
	 * defined.
	 */
	public int maxLen = -1;

	/**
	 * Constructor
	 */
	public PropertyData() {
		super();
	}

	/**
	 * Constructor
	 * @param propertyType The property type
	 * @param managed Is the property managed?
	 * @param required Is required?
	 * @param maxLen The max allowed String wise length
	 */
	public PropertyData(PropertyType propertyType, boolean managed, boolean required, int maxLen) {
		super();
		this.propertyType = propertyType;
		this.required = required;
		this.maxLen = maxLen;
	}

	public String descriptor() {
		return "Property Data";
	}
}
