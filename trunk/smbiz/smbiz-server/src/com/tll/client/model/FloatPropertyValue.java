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
	 * @param propertyName
	 * @param value
	 */
	public FloatPropertyValue(String propertyName, Float value) {
		this(propertyName, null, value);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param pdata
	 * @param value
	 */
	public FloatPropertyValue(String propertyName, PropertyData pdata, Float value) {
		super(propertyName, pdata);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!super.equals(obj)) return false;
		if(getClass() != obj.getClass()) return false;
		final FloatPropertyValue other = (FloatPropertyValue) obj;
		if(value == null) {
			if(other.value != null) return false;
		}
		else if(!value.equals(other.value)) return false;
		return true;
	}

}
