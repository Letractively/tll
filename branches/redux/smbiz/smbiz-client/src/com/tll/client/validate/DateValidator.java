package com.tll.client.validate;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.tll.client.msg.Msg;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.client.util.Fmt;
import com.tll.client.util.Fmt.DateFormat;

/**
 * DateValidator
 * @author jpk
 */
public class DateValidator implements IValidator {

	public static final DateValidator TIMESTAMP_VALIDATOR =
			new DateValidator(Fmt.dateFormatBindings.get(DateFormat.TIMESTAMP));

	public static final DateValidator DATE_VALIDATOR = new DateValidator(Fmt.dateFormatBindings.get(DateFormat.DATE));

	public static final DateValidator TIME_VALIDATOR = new DateValidator(Fmt.dateFormatBindings.get(DateFormat.TIME));

	private final DateTimeFormat dateFormat;

	/**
	 * Constructor
	 * @param pattern
	 */
	public DateValidator(String pattern) {
		if(pattern == null) {
			throw new IllegalArgumentException("A date format must be specified.");
		}
		dateFormat = DateTimeFormat.getFormat(pattern);
	}

	/**
	 * Constructor
	 * @param dateFormat
	 */
	public DateValidator(DateTimeFormat dateFormat) {
		super();
		if(dateFormat == null) {
			throw new IllegalArgumentException("A date pattern must be specified.");
		}
		this.dateFormat = dateFormat;
	}

	public Object validate(Object value) throws ValidationException {
		if(value == null || value instanceof Date) return value;

		final String s = value.toString();
		// HACK
		if(s.length() == 0) return null;
		// END HACK

		try {
			return dateFormat.parse(s);
		}
		catch(IllegalArgumentException e) {
			throw new ValidationException(new Msg("Must be a date of format: '" + dateFormat.getPattern() + "'.",
					MsgLevel.ERROR));
		}
	}

	@Override
	public final int hashCode() {
		return dateFormat.getPattern().hashCode();
	}

	@Override
	public final boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final DateValidator other = (DateValidator) obj;
		return other.dateFormat.getPattern().equals(dateFormat.getPattern());
	}
}
