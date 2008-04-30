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
	 * @param name
	 * @param pdata
	 * @param value
	 */
	public StringPropertyValue(String name, PropertyData pdata, String value) {
		super(name, pdata);
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
}
