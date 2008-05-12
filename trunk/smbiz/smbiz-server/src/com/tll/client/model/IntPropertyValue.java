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
	 * @param propertyName
	 * @param value
	 */
	public IntPropertyValue(String propertyName, Integer value) {
		this(propertyName, null, value);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param pdata
	 * @param value
	 */
	public IntPropertyValue(String propertyName, PropertyData pdata, Integer value) {
		super(propertyName, pdata);
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
		if(obj instanceof Integer) {
			setInteger((Integer) obj);
		}
		else if(obj instanceof Number) {
			setInteger(((Number) obj).intValue());
		}
		else {
			throw new IllegalArgumentException("The value must be an Integer");
		}
	}

	public Integer getInteger() {
		return value;
	}

	public void setInteger(Integer value) {
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
		final IntPropertyValue other = (IntPropertyValue) obj;
		if(value == null) {
			if(other.value != null) return false;
		}
		else if(!value.equals(other.value)) return false;
		return true;
	}

}
