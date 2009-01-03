/**
 * The Logic Lab
 */
package com.tll.client.model;

import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;
import com.tll.util.INameValueProvider;

/**
 * StringPropertyValue - Generic holder construct for entity properties.
 * @author jpk
 */
@SuppressWarnings("unchecked")
public class EnumPropertyValue extends AbstractPropertyValue implements ISelfFormattingPropertyValue {

	private Enum<?> value;

	/**
	 * Constructor
	 */
	public EnumPropertyValue() {
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param value
	 */
	public EnumPropertyValue(String propertyName, Enum<?> value) {
		this(propertyName, null, value);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param metadata
	 * @param value
	 */
	public EnumPropertyValue(String propertyName, PropertyMetadata metadata, Enum<?> value) {
		super(propertyName, metadata);
		this.value = value;
	}

	public PropertyType getType() {
		return PropertyType.ENUM;
	}

	public IPropertyValue copy() {
		return new EnumPropertyValue(propertyName, metadata, value);
	}

	public Enum<?> getEnum() {
		return value;
	}

	@Override
	protected void doSetValue(Object value) {
		if(value != null && value instanceof Enum == false) {
			throw new IllegalArgumentException("The value must be an Enum");
		}
		this.value = (Enum<?>) value;
	}

	public Object getValue() {
		return value;
	}

	public String asString() {
		if(value instanceof INameValueProvider) {
			return ((INameValueProvider) value).getName();
		}
		return value == null ? null : value.name();
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
		final EnumPropertyValue other = (EnumPropertyValue) obj;
		if(value == null) {
			if(other.value != null) return false;
		}
		else if(!value.equals(other.value)) return false;
		return true;
	}

}
