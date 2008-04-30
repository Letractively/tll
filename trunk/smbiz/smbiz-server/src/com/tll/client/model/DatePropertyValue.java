/**
 * The Logic Lab
 */
package com.tll.client.model;

import java.util.Date;

import com.tll.model.schema.PropertyType;

/**
 * StringPropertyValue
 * @author jpk
 */
public class DatePropertyValue extends AbstractPropertyValue {

	protected Date value;

	/**
	 * Constructor
	 */
	public DatePropertyValue() {
	}

	/**
	 * Constructor
	 * @param name
	 * @param pdata
	 * @param value
	 */
	public DatePropertyValue(String name, PropertyData pdata, Date value) {
		super(name, pdata);
		this.value = value;
	}

	public String descriptor() {
		return "Date property";
	}

	public PropertyType getType() {
		return PropertyType.DATE;
	}

	public void clear() {
		this.value = null;
	}

	public IPropertyValue copy() {
		return new DatePropertyValue(getPropertyName(), pdata, value == null ? null : new Date(value.getTime()));
	}

	public final Object getValue() {
		return value;
	}

	public void setValue(Object obj) {
		if(obj instanceof Date == false) {
			throw new IllegalArgumentException("The value must be a Date");
		}
		setDate((Date) obj);
	}

	public void setDate(Date value) {
		this.value = value;
	}

	public Date getDate() {
		return value;
	}
}
