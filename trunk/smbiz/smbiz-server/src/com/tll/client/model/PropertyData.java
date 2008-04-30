/**
 * The Logic Lab
 * @author jpk
 * Nov 5, 2007
 */
package com.tll.client.model;

import com.tll.client.IMarshalable;
import com.tll.model.schema.PropertyType;

/**
 * PropertyData - "meta" info for a single property. (RPC version of FieldData).
 * @author jpk
 */
// TODO re-name to ModelMetaData (or something to this effect)
public class PropertyData implements IMarshalable {

	/**
	 * The property type.
	 */
	public PropertyType propertyType;

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
	 * @param required Is required?
	 * @param maxLen The max allowed String wise length
	 */
	public PropertyData(PropertyType propertyType, boolean required, int maxLen) {
		super();
		this.propertyType = propertyType;
		this.required = required;
		this.maxLen = maxLen;
	}

	public String descriptor() {
		return "Property Data";
	}
}
