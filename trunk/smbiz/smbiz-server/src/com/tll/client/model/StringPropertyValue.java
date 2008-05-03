/**
 * The Logic Lab
 */
package com.tll.client.model;

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
	 * @param pdata
	 * @param value
	 */
	public StringPropertyValue(String propertyName, PropertyData pdata, String value) {
		super(propertyName, pdata);
		this.value = value;

	}

	public String descriptor() {
		return "String property";
	}

	public PropertyType getType() {
		return PropertyType.STRING;
	}

	public void clear() {
		this.value = null;
	}

	public IPropertyValue copy() {
		return new StringPropertyValue(getPropertyName(), pdata, value);
	}

	public final Object getValue() {
		return value;
	}

	public String asString() {
		return value;
	}

	public void setValue(Object obj) {
		if(obj instanceof String == false) {
			throw new IllegalArgumentException("The value must be a String");
		}
		setString((String) obj);
	}

	public String getString() {
		return value;
	}

	public void setString(String value) {
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
		final StringPropertyValue other = (StringPropertyValue) obj;
		if(value == null) {
			if(other.value != null) return false;
		}
		else if(!value.equals(other.value)) return false;
		return true;
	}

}
