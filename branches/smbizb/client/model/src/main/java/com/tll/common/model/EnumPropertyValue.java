/**
 * The Logic Lab
 */
package com.tll.common.model;

import com.tll.INameValueProvider;
import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;

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
	protected void doSetValue(Object val) {
		if(val != null && val instanceof Enum == false) {
			throw new IllegalArgumentException("The value must be an Enum");
		}
		this.value = (Enum<?>) val;
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
}
