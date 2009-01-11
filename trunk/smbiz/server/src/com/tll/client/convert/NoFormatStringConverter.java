/**
 * The Logic Lab
 * @author jpk
 * Jan 11, 2009
 */
package com.tll.client.convert;

import com.tll.client.util.GlobalFormat;

/**
 * NoFormatStringConverter
 * @author jpk
 */
public final class NoFormatStringConverter implements IFormattedConverter<String, String> {

	public static final NoFormatStringConverter INSTANCE = new NoFormatStringConverter();

	/**
	 * Constructor
	 */
	private NoFormatStringConverter() {
		super();
	}

	@Override
	public GlobalFormat getFormat() {
		return null;
	}

	@Override
	public String convert(String o) throws IllegalArgumentException {
		return o; // pass through
	}

}
