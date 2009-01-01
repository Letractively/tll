/**
 * The Logic Lab
 */
package com.tll.client.model;

import com.tll.model.schema.PropertyMetadata;
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
	 * @param metadata
	 * @param value
	 */
	public FloatPropertyValue(String propertyName, PropertyMetadata metadata, Float value) {
		super(propertyName, metadata);
		this.value = value;
	}

	public PropertyType getType() {
		return PropertyType.FLOAT;
	}

	public IPropertyValue copy() {
		return new FloatPropertyValue(getPropertyName(), metadata, value == null ? null : new Float(value.floatValue()));
	}

	public final Object getValue() {
		return value;
	}

	@Override
	protected void doSetValue(Object obj) {
		if(obj == null) {
			this.value = null;
		}
		if(obj instanceof Float) {
			this.value = (Float) obj;
		}
		else if(obj instanceof Number) {
			this.value = ((Number) obj).floatValue();
		}
		else {
			throw new IllegalArgumentException("The value must be a Float");
		}
	}

	public Float getFloat() {
		return value;
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
