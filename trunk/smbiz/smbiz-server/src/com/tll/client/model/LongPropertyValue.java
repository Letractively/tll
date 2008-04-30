/**
 * The Logic Lab
 */
package com.tll.client.model;

import com.tll.model.schema.PropertyType;

/**
 * StringPropertyValue - Generic holder construct for entity properties.
 * @author jpk
 */
public class LongPropertyValue extends AbstractPropertyValue implements ISelfFormattingPropertyValue {

	protected Long value;

	/**
	 * Constructor
	 */
	public LongPropertyValue() {
	}

	/**
	 * Constructor
	 * @param name
	 * @param pdata
	 * @param value
	 */
	public LongPropertyValue(String name, PropertyData pdata, Long value) {
		super(name, pdata);
		this.value = value;
	}

	public String descriptor() {
		return "Long property";
	}

	public PropertyType getType() {
		return PropertyType.LONG;
	}

	public void clear() {
		this.value = null;
	}

	public IPropertyValue copy() {
		return new LongPropertyValue(getPropertyName(), pdata, value == null ? null : new Long(value.longValue()));
	}

	public final Object getValue() {
		return value;
	}

	public String asString() {
		return value == null ? null : value.toString();
	}

	public void setValue(Object obj) {
		if(obj instanceof Long == false) {
			throw new IllegalArgumentException("The value must be a Long");
		}
		setLong((Long) obj);
	}

	public void setLong(Long value) {
		this.value = value;
	}

	public Long getLong() {
		return value;
	}
}
