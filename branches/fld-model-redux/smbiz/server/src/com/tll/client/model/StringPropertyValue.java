/**
 * The Logic Lab
 */
package com.tll.client.model;

import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;

/**
 * StringPropertyValue
 * @author jpk
 */
public class StringPropertyValue extends AbstractPropertyValue implements ISelfFormattingPropertyValue {

	protected String value;

	/**
	 * Constructor
	 */
	public StringPropertyValue() {
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param value
	 */
	public StringPropertyValue(String propertyName, String value) {
		this(propertyName, null, value);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param metadata
	 * @param value
	 */
	public StringPropertyValue(String propertyName, PropertyMetadata metadata, String value) {
		super(propertyName, metadata);
		this.value = value;

	}

	public String descriptor() {
		return "String property";
	}

	public PropertyType getType() {
		return PropertyType.STRING;
	}

	public IPropertyValue copy() {
		return new StringPropertyValue(getPropertyName(), metadata, value);
	}

	public final Object getValue() {
		return value;
	}

	public String asString() {
		return value;
	}

	@Override
	protected void doSetValue(Object obj) {
		if(obj != null && obj instanceof String == false) {
			throw new IllegalArgumentException("The value must be a String");
		}
		this.value = (String) obj;
	}

	public String getString() {
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
		final StringPropertyValue other = (StringPropertyValue) obj;
		if(value == null) {
			if(other.value != null) return false;
		}
		else if(!value.equals(other.value)) return false;
		return true;
	}

}
