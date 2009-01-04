/**
 * The Logic Lab
 * @author jpk
 * Dec 29, 2008
 */
package com.tll.client.bind.convert;

import java.util.Date;

import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;

/**
 * DateConverter
 * @author jpk
 */
public final class DateConverter implements IConverter<Date, Object> {

	public static final DateConverter DATE_CONVERTER = new DateConverter(GlobalFormat.DATE);

	public static final DateConverter TIME_CONVERTER = new DateConverter(GlobalFormat.TIME);

	public static final DateConverter TIMESTAMP_CONVERTER = new DateConverter(GlobalFormat.TIMESTAMP);

	/**
	 * Factory method for obtaining a pre-baked {@link DateConverter}.
	 * @param dateFormat
	 * @return The appropriate {@link DateConverter}
	 * @throws IllegalArgumentException When the given date format is
	 *         <code>null</code> or invalid.
	 */
	public static final DateConverter instance(GlobalFormat dateFormat) {
		switch(dateFormat) {
			case DATE:
				return DATE_CONVERTER;
			case TIME:
				return TIME_CONVERTER;
			case TIMESTAMP:
				return TIMESTAMP_CONVERTER;
		}
		throw new IllegalArgumentException("A valid date format must be specified.");
	}

	private final GlobalFormat dateFormat;

	/**
	 * Constructor
	 * @param dateFormat
	 */
	private DateConverter(GlobalFormat dateFormat) {
		super();
		if(dateFormat == null || !dateFormat.isDateFormat()) throw new IllegalArgumentException("Not a date format.");
		this.dateFormat = dateFormat;
	}

	public Date convert(Object o) {
		try {
			return (o == null || o instanceof Date) ? (Date) o : Fmt.getDateTimeFormat(dateFormat).parse(o.toString());
		}
		catch(Throwable e) {
			throw new IllegalArgumentException(e);
		}
	}

}
