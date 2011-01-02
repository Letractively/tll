/**
 * The Logic Lab
 */
package com.tll.common.model;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;
import com.tll.model.PropertyMetadata;
import com.tll.model.PropertyType;

/**
 * StringPropertyValue - Generic holder construct for entity properties.
 * @author jpk
 */
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
	public EnumPropertyValue(final String propertyName, final Enum<?> value) {
		this(propertyName, null, value);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param metadata
	 * @param value
	 */
	public EnumPropertyValue(final String propertyName, final PropertyMetadata metadata, final Enum<?> value) {
		super(propertyName, metadata);
		doSetValue(value);
	}

	@Override
	public PropertyType getType() {
		return PropertyType.ENUM;
	}

	@Override
	public IPropertyValue copy() {
		return new EnumPropertyValue(propertyName, metadata, (Enum<?>) value);
	}

	public Enum<?> getEnum() {
		return (Enum<?>) value;
	}

	@Override
	protected void doSetValue(final Object val) {
		if(val != null && (val instanceof Enum == false || val instanceof IMarshalable == false)) {
			throw new IllegalArgumentException("The value must be both an Enum and implement IMarshalable ("
					+ val.getClass().getName() + ")");
		}
		this.value = (IMarshalable) val;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public String asString() {
		if(value instanceof INameValueProvider) {
			return ((INameValueProvider<?>) value).getName();
		}
		return value == null ? null : ((Enum<?>) value).name();
	}
}
