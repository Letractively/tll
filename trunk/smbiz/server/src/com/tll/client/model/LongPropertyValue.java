/**
 * The Logic Lab
 */
package com.tll.client.model;

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
			this.value = (Long) obj;
		}
		else if(obj instanceof Number) {
			this.value = ((Number) obj).longValue();
		}
		else {
			throw new IllegalArgumentException("The value must be a Long");
		}
	}

	public Long getLong() {
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
		final LongPropertyValue other = (LongPropertyValue) obj;
		if(value == null) {
			if(other.value != null) return false;
		}
		else if(!value.equals(other.value)) return false;
		return true;
	}

}
