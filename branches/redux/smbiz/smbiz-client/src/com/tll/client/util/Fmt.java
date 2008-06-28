/**
 * The Logic Lab
 * @author jpk Sep 2, 2007
 */
package com.tll.client.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;

/**
 * Fmt - String formatting constants and methods.
 * <P>
 * NOTE: We prescribe pre-baked global format directives to avoid having to
 * dynamically construct either DateFormats or NumberFormats which are
 * expensive. The downside is a limited set of available date and number formats
 * available to the client.
 * @see DateTimeFormat
 * @see NumberFormat
 * @author jpk
 */
public abstract class Fmt {

	public static enum DateFormat {
		TIMESTAMP,
		DATE,
		TIME;
	}

	public static enum DecimalFormat {
		PERCENT,
		CURRENCY,
		DECIMAL;
	}

	public static enum BooleanFormat {
		YES_NO,
		TRUE_FALSE;
	}

	/**
	 * Translates a global format to a local date format.
	 * @param format The global format
	 * @return The corresponding {@link DateFormat}
	 * @throws IllegalArgumentException When the given global format does not map
	 *         to a local date format.
	 */
	public static DateFormat getDateFormat(GlobalFormat format) {
		switch(format) {
			case DATE:
				return DateFormat.DATE;
			case TIME:
				return DateFormat.TIME;
			case TIMESTAMP:
				return DateFormat.TIMESTAMP;
			default:
				throw new IllegalArgumentException("Not a date format: " + format.toString());
		}
	}

	/**
	 * Translates a global format to a local decimal format.
	 * @param format The global format
	 * @return The corresponding {@link DecimalFormat}
	 * @throws IllegalArgumentException When the given global format does not map
	 *         to a local decimal format.
	 */
	public static DecimalFormat getDecimalFormat(GlobalFormat format) {
		switch(format) {
			case PERCENT:
				return DecimalFormat.PERCENT;
			case CURRENCY:
				return DecimalFormat.CURRENCY;
			case DECIMAL:
				return DecimalFormat.DECIMAL;
			default:
				throw new IllegalArgumentException("Not a decimal format: " + format.toString());
		}
	}

	public static final Map<DateFormat, DateTimeFormat> dateFormatBindings = new HashMap<DateFormat, DateTimeFormat>();

	public static final Map<DecimalFormat, NumberFormat> decimalFormatBindings =
			new HashMap<DecimalFormat, NumberFormat>();

	static {
		dateFormatBindings.put(DateFormat.DATE, DateTimeFormat.getShortDateFormat());
		dateFormatBindings.put(DateFormat.TIME, DateTimeFormat.getShortTimeFormat());
		dateFormatBindings.put(DateFormat.TIMESTAMP, DateTimeFormat.getShortDateTimeFormat());
		// default is timestamp
		dateFormatBindings.put(null, DateTimeFormat.getShortDateTimeFormat());

		decimalFormatBindings.put(DecimalFormat.CURRENCY, NumberFormat.getCurrencyFormat());
		decimalFormatBindings.put(DecimalFormat.PERCENT, NumberFormat.getPercentFormat());
		decimalFormatBindings.put(DecimalFormat.DECIMAL, NumberFormat.getDecimalFormat());
		// default is local dependant decimal format
		decimalFormatBindings.put(null, NumberFormat.getDecimalFormat());
	}

	/**
	 * Generic formatting utility method.
	 * @param value
	 * @param format
	 * @return Never <code>null</code> String.
	 */
	public static String format(Object value, GlobalFormat format) {
		if(value == null) return "";
		if(format != null) {
			switch(format) {
				case DATE:
					return date((Date) value, DateFormat.DATE);
				case TIME:
					return date((Date) value, DateFormat.TIME);
				case TIMESTAMP:
					return date((Date) value, DateFormat.TIMESTAMP);

				case CURRENCY:
					if(value instanceof Double) {
						return decimal(((Double) value).doubleValue(), DecimalFormat.CURRENCY);
					}
					else if(value instanceof Float) {
						return decimal(((Float) value).doubleValue(), DecimalFormat.CURRENCY);
					}

				case PERCENT:
					if(value instanceof Double) {
						return decimal(((Double) value).doubleValue(), DecimalFormat.PERCENT);
					}
					else if(value instanceof Float) {
						return decimal(((Float) value).doubleValue(), DecimalFormat.PERCENT);
					}

				case DECIMAL:
					if(value instanceof Double) {
						return decimal(((Double) value).doubleValue(), DecimalFormat.DECIMAL);
					}
					else if(value instanceof Float) {
						return decimal(((Float) value).doubleValue(), DecimalFormat.DECIMAL);
					}

				case BOOL_TRUEFALSE:
					return bool(((Boolean) value).booleanValue(), BooleanFormat.TRUE_FALSE);
				case BOOL_YESNO:
					return bool(((Boolean) value).booleanValue(), BooleanFormat.YES_NO);
			}
		}
		// resort to toString
		return value.toString();
	}

	/**
	 * Formats a {@link Date} to a String given a format directive.
	 * @param date
	 * @param format May be <code>null</code> in which case
	 *        {@link DateFormat#TIMESTAMP} formatting is used.
	 * @return Formatted date String (never <code>null</code>).
	 */
	public static String date(Date date, DateFormat format) {
		return date == null ? "" : dateFormatBindings.get(format).format(date);
	}

	/**
	 * Formats a decimal to a local dependant decimal formatted String.
	 * @param decimal
	 * @param format The decimal format. If <code>null</code>, default local
	 *        dependant decimal formatting is applied.
	 * @return A decimal formatted String.
	 */
	public static String decimal(double decimal, DecimalFormat format) {
		return decimalFormatBindings.get(format).format(decimal);
	}

	/**
	 * Formats a decimal to a currency formatted String
	 * @param decimal
	 * @return Currency formatted String
	 */
	public static String currency(double decimal) {
		return decimalFormatBindings.get(DecimalFormat.CURRENCY).format(decimal);
	}

	/**
	 * Formats a decimal to a percent formatted String
	 * @param decimal
	 * @return Percent formatted String
	 */
	public static String percent(double decimal) {
		return decimalFormatBindings.get(DecimalFormat.PERCENT).format(decimal);
	}

	/**
	 * Formats a boolean to a UI friendly string.
	 * @param b
	 * @param format
	 * @return Presentation worthy string
	 */
	public static String bool(boolean b, BooleanFormat format) {
		switch(format) {
			case YES_NO:
				return b ? "Yes" : "No";
			default:
			case TRUE_FALSE:
				return b ? "True" : "False";
		}
	}
}
