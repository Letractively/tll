/**
 * The Logic Lab
 * @author jpk
 * Jan 11, 2009
 */
package com.tll.common.convert;

import com.tll.INameValueProvider;

/**
 * EnumToStringConverter - Converts an arbitrary enum to a String considering
 * the possibility the enum may implement {@link INameValueProvider}.
 * @author jpk
 * @param <E> The enum type
 */
public final class EnumToStringConverter<E extends Enum<?>> implements IConverter<String, E> {

	/**
	 * Constructor
	 */
	public EnumToStringConverter() {
		super();
	}

	public String convert(E o) throws IllegalArgumentException {
		if(o == null) return null;
		if(o instanceof INameValueProvider) {
			return ((INameValueProvider<?>) o).getName();
		}
		return o.name();
	}

}
