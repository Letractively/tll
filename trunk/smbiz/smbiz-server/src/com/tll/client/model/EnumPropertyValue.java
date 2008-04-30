/**
 * The Logic Lab
 */
package com.tll.client.model;

import com.tll.model.schema.PropertyType;

/**
 * StringPropertyValue - Generic holder construct for entity properties.
 * @author jpk
 */
public class EnumPropertyValue extends StringPropertyValue {

	private String enumClassName;

	/**
	 * Constructor
	 */
	public EnumPropertyValue() {
	}

	/**
	 * Constructor
	 * @param enumClassName May not be <code>null</code>.
	 * @param name
	 * @param pdata
	 * @param value
	 */
	public EnumPropertyValue(String enumClassName, String name, PropertyData pdata, String value) {
		super(name, pdata, value);
		assert enumClassName != null;
		this.enumClassName = enumClassName;
	}

	@Override
	public PropertyType getType() {
		return PropertyType.ENUM;
	}

	@Override
	public IPropertyValue copy() {
		return new EnumPropertyValue(enumClassName, getPropertyName(), pdata, value);
	}

	public String getEnumClassName() {
		return enumClassName;
	}

	public void setEnumClassName(String enumClassName) {
		this.enumClassName = enumClassName;
	}

	public String getEnum() {
		return value;
	}

	public void setEnum(String value) {
		this.value = value;
	}
}
