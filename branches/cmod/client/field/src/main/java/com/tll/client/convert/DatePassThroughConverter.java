/**
 * The Logic Lab
 * @author jpk
 * Jan 11, 2009
 */
package com.tll.client.convert;

import java.util.Date;

/**
 * DatePassThroughConverter
 * @author jpk
 */
public class DatePassThroughConverter implements IConverter<Date, Date> {

	public static final DatePassThroughConverter INSTANCE = new DatePassThroughConverter();

	/**
	 * Constructor
	 */
	private DatePassThroughConverter() {
		super();
	}

	public Date convert(Date o) throws IllegalArgumentException {
		return o;
	}

}
