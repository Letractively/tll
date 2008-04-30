/**
 * The Logic Lab
 */
package com.tll.client.model;

import com.tll.model.schema.PropertyType;

/**
 * BooleanPropertyValue
 * @author jpk
 */
public class BooleanPropertyValue extends AbstractPropertyValue {

	protected Boolean value;

	/**
	 * Constructor
	 */
	public BooleanPropertyValue() {
		super();
	}

	/**
	 * Constructor
	 * @param name
	 * @param pdata
	 * @param value
	 */
	public BooleanPropertyValue(String name, PropertyData pdata, Boolean value) {
		super(name, pdata);
		this.value = value;
	}

	public String descriptor() {
		return "Boolean property";
	}

	public PropertyType getType() {
		return PropertyType.BOOL;
	}

	public void clear() {
		this.value = null;
	}

	public IPropertyValue copy() {
		return new BooleanPropertyValue(getPropertyName(), pdata, value == null ? null : new Boolean(value.booleanValue()));
	}

	public final Object getValue() {
		return value;
	}

	public void setValue(Object obj) {
		if(obj instanceof Boolean == false) {
			throw new IllegalArgumentException("The value must be a Boolean");
		}
		setBoolean((Boolean) obj);
	}

	public void setBoolean(Boolean value) {
		this.value = value;
	}

	public Boolean getBoolean() {
		return value;
	}
}
