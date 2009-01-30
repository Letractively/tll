/**
 * The Logic Lab
 * @author jpk
 * Jan 11, 2009
 */
package com.tll.client.convert;

/**
 * DatePassThroughConverter
 * @author jpk
 */
public class BooleanPassThroughConverter implements IConverter<Boolean, Boolean> {

	public static final BooleanPassThroughConverter INSTANCE = new BooleanPassThroughConverter();

	/**
	 * Constructor
	 */
	private BooleanPassThroughConverter() {
		super();
	}

	public Boolean convert(Boolean o) throws IllegalArgumentException {
		return o;
	}

}
