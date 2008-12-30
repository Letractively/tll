/**
 * The Logic Lab
 * @author jpk
 * Dec 29, 2008
 */
package com.tll.client.renderer;

import java.util.Date;

import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;

/**
 * DateRenderer
 * @author jpk
 */
public final class DateRenderer implements IRenderer<Date, Object> {

	public static final DateRenderer DATE_RENDERER = new DateRenderer(GlobalFormat.DATE);

	public static final DateRenderer TIME_RENDERER = new DateRenderer(GlobalFormat.TIME);

	public static final DateRenderer TIMESTAMP_RENDERER = new DateRenderer(GlobalFormat.TIMESTAMP);

	/**
	 * Factory method for obtaining a pre-baked {@link DateRenderer}.
	 * @param dateFormat
	 * @return The appropriate {@link DateRenderer}
	 * @throws IllegalArgumentException When the given date format is
	 *         <code>null</code> or invalid.
	 */
	public static final DateRenderer instance(GlobalFormat dateFormat) {
		switch(dateFormat) {
			case DATE:
				return DATE_RENDERER;
			case TIME:
				return TIME_RENDERER;
			case TIMESTAMP:
				return TIMESTAMP_RENDERER;
		}
		throw new IllegalArgumentException("A valid date format must be specified.");
	}

	private final GlobalFormat dateFormat;

	/**
	 * Constructor
	 * @param dateFormat
	 */
	private DateRenderer(GlobalFormat dateFormat) {
		super();
		if(dateFormat == null || !dateFormat.isDateFormat()) throw new IllegalArgumentException("Not a date format.");
		this.dateFormat = dateFormat;
	}

	public Date render(Object o) {
		try {
			return (o == null || o instanceof Date) ? (Date) o : Fmt.getDateTimeFormat(dateFormat).parse(o.toString());
		}
		catch(Throwable e) {
			throw new IllegalArgumentException(e);
		}
	}

}
