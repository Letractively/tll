/**
 * The Logic Lab
 */
package com.tll.client.model;

import com.tll.model.schema.PropertyType;

/**
 * StringPropertyValue - Generic holder construct for entity properties.
 * @author jpk
 */
public class FloatPropertyValue extends AbstractPropertyValue {

	protected Float value;

	/**
	 * Constructor
	 */
	public FloatPropertyValue() {
	}

	/**
	 * Constructor
	 * @param name
	 * @param pdata
	 * @param value
	 */
	public FloatPropertyValue(String name, PropertyData pdata, Float value) {
		super(name, pdata);
		this.value = value;
	}

	public String descriptor() {
		return "Float property";
	}

	public PropertyType getType() {
		return PropertyType.FLOAT;
	}

	public void clear() {
		this.value = null;
	}

	public IPropertyValue copy() {
		return new FloatPropertyValue(getPropertyName(), pdata, value == null ? null : new Float(value.floatValue()));
	}

	public final Object getValue() {
		return value;
	}

	public void setValue(Object obj) {
		if(obj instanceof Float == false) {
			throw new IllegalArgumentException("The value must be a Float");
		}
		setFloat((Float) obj);
	}

	public Float getFloat() {
		return value;
	}

	public void setFloat(Float value) {
		this.value = value;
	}
}
