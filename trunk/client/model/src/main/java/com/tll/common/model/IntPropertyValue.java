/**
 * The Logic Lab
 */
package com.tll.common.model;

import com.tll.model.PropertyMetadata;
import com.tll.model.PropertyType;

/**
 * StringPropertyValue - Generic holder construct for entity properties.
 * @author jpk
 */
public class IntPropertyValue extends AbstractPropertyValue implements ISelfFormattingPropertyValue {

	protected Integer value;

	/**
	 * Constructor
	 */
	public IntPropertyValue() {
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param value
	 */
	public IntPropertyValue(String propertyName, Integer value) {
		this(propertyName, null, value);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param metadata
	 * @param value
	 */
	public IntPropertyValue(String propertyName, PropertyMetadata metadata, Integer value) {
		super(propertyName, metadata);
		this.value = value;
	}

	public PropertyType getType() {
		return PropertyType.INT;
	}

	public IPropertyValue copy() {
		return new IntPropertyValue(propertyName, metadata, value == null ? null : new Integer(value.intValue()));
	}

	public final Object getValue() {
		return value;
	}

	public String asString() {
		return value == null ? null : value.toString();
	}

	@Override
	protected void doSetValue(Object obj) {
		if(obj == null) {
			this.value = null;
		}
		else if(obj instanceof Integer) {
			this.value = (Integer) obj;
		}
		else if(obj instanceof Number) {
			this.value = Integer.valueOf(((Number) obj).intValue());
		}
		else {
			throw new IllegalArgumentException("The value must be an Integer");
		}
	}

	public Integer getInteger() {
		return value;
	}
}
