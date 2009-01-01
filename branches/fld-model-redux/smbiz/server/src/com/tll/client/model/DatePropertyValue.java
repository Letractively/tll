/**
 * The Logic Lab
 */
package com.tll.client.model;

import java.util.Date;

import com.tll.model.schema.PropertyMetadata;
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
	 * @param propertyName
	 * @param value
	 */
	public DatePropertyValue(String propertyName, Date value) {
		super(propertyName, null);
		this.value = value;
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param metadata
	 * @param value
	 */
	public DatePropertyValue(String propertyName, PropertyMetadata metadata, Date value) {
		super(propertyName, metadata);
		this.value = value;
	}

	public PropertyType getType() {
		return PropertyType.DATE;
	}

	public IPropertyValue copy() {
		return new DatePropertyValue(getPropertyName(), metadata, value == null ? null : new Date(value.getTime()));
	}

	public final Object getValue() {
		return value;
	}

	@Override
	protected void doSetValue(Object obj) {
		if(obj != null && obj instanceof Date == false) {
			throw new IllegalArgumentException("The value must be a Date");
		}
		this.value = (Date) obj;
	}

	public Date getDate() {
		return value;
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
		final DatePropertyValue other = (DatePropertyValue) obj;
		if(value == null) {
			if(other.value != null) return false;
		}
		else if(!value.equals(other.value)) return false;
		return true;
	}

}
