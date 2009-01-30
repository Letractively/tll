/**
 * The Logic Lab
 */
package com.tll.common.model;

import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;

/**
 * BooleanPropertyValue
 * @author jpk
 */
public class BooleanPropertyValue extends AbstractPropertyValue {

	private Boolean value;

	/**
	 * Constructor
	 */
	public BooleanPropertyValue() {
		super();
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param value
	 */
	public BooleanPropertyValue(String propertyName, Boolean value) {
		this(propertyName, null, value);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param metadata
	 * @param value
	 */
	public BooleanPropertyValue(String propertyName, PropertyMetadata metadata, Boolean value) {
		super(propertyName, metadata);
		this.value = value;
	}

	public PropertyType getType() {
		return PropertyType.BOOL;
	}

	public IPropertyValue copy() {
		return new BooleanPropertyValue(propertyName, metadata, value == null ? null : new Boolean(value.booleanValue()));
	}

	public final Object getValue() {
		return value;
	}

	@Override
	protected void doSetValue(Object obj) {
		if(obj != null && obj instanceof Boolean == false) {
			throw new IllegalArgumentException("The value must be a Boolean");
		}
		this.value = (Boolean) obj;
	}

	public Boolean getBoolean() {
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
		final BooleanPropertyValue other = (BooleanPropertyValue) obj;
		if(value == null) {
			if(other.value != null) return false;
		}
		else if(!value.equals(other.value)) return false;
		return true;
	}

}
