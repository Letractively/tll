/**
 * The Logic Lab
 */
package com.tll.common.model;

import java.util.Date;

import com.tll.model.PropertyMetadata;
import com.tll.model.PropertyType;

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
	public DatePropertyValue(final String propertyName, final Date value) {
		super(propertyName, null);
		this.value = value;
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param metadata
	 * @param value
	 */
	public DatePropertyValue(final String propertyName, final PropertyMetadata metadata, final Date value) {
		super(propertyName, metadata);
		this.value = value;
	}

	@Override
	public PropertyType getType() {
		return PropertyType.DATE;
	}

	@Override
	public IPropertyValue copy() {
		return new DatePropertyValue(propertyName, metadata, value == null ? null : new Date(value.getTime()));
	}

	@Override
	public final Object getValue() {
		return value;
	}

	@Override
	protected void doSetValue(final Object obj) {
		if(obj != null && obj instanceof Date == false) {
			throw new IllegalArgumentException("The value must be a Date");
		}
		this.value = (Date) obj;
	}

	public Date getDate() {
		return value;
	}
}
