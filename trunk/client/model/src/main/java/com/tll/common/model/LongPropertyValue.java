/**
 * The Logic Lab
 */
package com.tll.common.model;

import com.tll.model.schema.PropertyMetadata;
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
	 * @param propertyName
	 * @param value
	 */
	public LongPropertyValue(String propertyName, Long value) {
		this(propertyName, null, value);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param metadata
	 * @param value
	 */
	public LongPropertyValue(String propertyName, PropertyMetadata metadata, Long value) {
		super(propertyName, metadata);
		this.value = value;
	}

	public PropertyType getType() {
		return PropertyType.LONG;
	}

	public IPropertyValue copy() {
		return new LongPropertyValue(propertyName, metadata, value == null ? null : new Long(value.longValue()));
	}

	public final Object getValue() {
		return value;
	}

	public String asString() {
		return value == null ? null : value.toString();
	}

	@Override
	public void doSetValue(Object obj) {
		if(obj == null) {
			this.value = null;
		}
		else if(obj instanceof Integer) {
			this.value = Long.valueOf(((Integer) obj).longValue());
		}
		else if(obj instanceof Number) {
			this.value = Long.valueOf(((Number) obj).longValue());
		}
		else {
			throw new IllegalArgumentException("The value must be a Long");
		}
	}

	public Long getLong() {
		return value;
	}
}
