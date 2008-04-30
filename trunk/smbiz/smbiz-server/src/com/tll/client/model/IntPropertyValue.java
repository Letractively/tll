/**
 * The Logic Lab
 */
package com.tll.client.model;

import com.tll.model.schema.PropertyType;

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
	 * @param name
	 * @param pdata
	 * @param value
	 */
	public IntPropertyValue(String name, PropertyData pdata, Integer value) {
		super(name, pdata);
		this.value = value;
	}

	public String descriptor() {
		return "Integer property";
	}

	public PropertyType getType() {
		return PropertyType.INT;
	}

	public void clear() {
		this.value = null;
	}

	public IPropertyValue copy() {
		return new IntPropertyValue(getPropertyName(), pdata, value == null ? null : new Integer(value.intValue()));
	}

	public final Object getValue() {
		return value;
	}

	public String asString() {
		return value == null ? null : value.toString();
	}

	public void setValue(Object obj) {
		if(obj instanceof Integer == false) {
			throw new IllegalArgumentException("The value must be an Integer");
		}
		setInteger((Integer) obj);
	}

	public Integer getInteger() {
		return value;
	}

	public void setInteger(Integer value) {
		this.value = value;
	}
}
