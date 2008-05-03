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
	 * @param propertyName
	 * @param value
	 */
	public EnumPropertyValue(String enumClassName, String propertyName, String value) {
		this(enumClassName, propertyName, null, value);
	}

	/**
	 * Constructor
	 * @param enumClassName May not be <code>null</code>.
	 * @param propertyName
	 * @param pdata
	 * @param value
	 */
	public EnumPropertyValue(String enumClassName, String propertyName, PropertyData pdata, String value) {
		super(propertyName, pdata, value);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((enumClassName == null) ? 0 : enumClassName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!super.equals(obj)) return false;
		if(getClass() != obj.getClass()) return false;
		final EnumPropertyValue other = (EnumPropertyValue) obj;
		if(enumClassName == null) {
			if(other.enumClassName != null) return false;
		}
		else if(!enumClassName.equals(other.enumClassName)) return false;
		return true;
	}

}
