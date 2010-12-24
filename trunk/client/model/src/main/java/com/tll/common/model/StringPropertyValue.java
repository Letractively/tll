/**
 * The Logic Lab
 */
package com.tll.common.model;

import com.tll.model.PropertyMetadata;
import com.tll.model.PropertyType;

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
		return new StringPropertyValue(propertyName, metadata, value);
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
}
