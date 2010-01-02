/**
 * The Logic Lab
 */
package com.tll.common.model;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;
import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;

/**
 * StringPropertyValue - Generic holder construct for entity properties.
 * @author jpk
 */
@SuppressWarnings("unchecked")
public class EnumPropertyValue extends AbstractPropertyValue implements ISelfFormattingPropertyValue {

	/**
	 * This is a in-direct type ref to an actual {@link Enum} instance. This
	 * avoids the GWT compiler from seeking out <em>all</em> Enum types on the
	 * classpath which is what it does and this is un-desireable.
	 * <p>
	 * Therfore, if we want to marshal an enum by way of {@link EnumPropertyValue}
	 * s, it must implement the {@link IMarshalable} interface.
	 */
	private IMarshalable value;

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
		doSetValue(value);
	}

	public PropertyType getType() {
		return PropertyType.ENUM;
	}

	public IPropertyValue copy() {
		return new EnumPropertyValue(propertyName, metadata, (Enum<?>) value);
	}

	public Enum<?> getEnum() {
		return (Enum<?>) value;
	}

	@Override
	protected void doSetValue(Object val) {
		if(val != null && (val instanceof Enum == false || val instanceof IMarshalable == false)) {
			throw new IllegalArgumentException("The value must be both an Enum and implement IMarshalable ("
					+ val.getClass().getName() + ")");
		}
		this.value = (IMarshalable) val;
	}

	public Object getValue() {
		return value;
	}

	public String asString() {
		if(value instanceof INameValueProvider) {
			return ((INameValueProvider) value).getName();
		}
		return value == null ? null : ((Enum<?>) value).name();
	}
}
