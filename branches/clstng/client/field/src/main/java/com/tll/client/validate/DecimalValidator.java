package com.tll.client.validate;

import com.google.gwt.i18n.client.NumberFormat;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;

/**
 * DecimalValidator
 * @author jpk
 */
public class DecimalValidator implements IValidator {

	private static final DecimalValidator CURRENCY_VALIDATOR =
			new DecimalValidator(Fmt.getDecimalFormat(GlobalFormat.CURRENCY));

	private static final DecimalValidator PERCENT_VALIDATOR =
			new DecimalValidator(Fmt.getDecimalFormat(GlobalFormat.PERCENT));

	private static final DecimalValidator DECIMAL_VALIDATOR =
			new DecimalValidator(Fmt.getDecimalFormat(GlobalFormat.DECIMAL));

	/**
	 * Factory method for obtaining a pre-baked {@link DecimalValidator}.
	 * @param numberFormat
	 * @return The appropriate {@link DecimalValidator}
	 * @throws IllegalArgumentException When the given number format is
	 *         <code>null</code> or invalid.
	 */
	public static final DecimalValidator get(GlobalFormat numberFormat) {
		switch(numberFormat) {
			case CURRENCY:
				return CURRENCY_VALIDATOR;
			case DECIMAL:
				return DECIMAL_VALIDATOR;
			case PERCENT:
				return PERCENT_VALIDATOR;
		}
		throw new IllegalArgumentException("A valid number format must be specified.");
	}

	private final NumberFormat numberFormat;

	/**
	 * Constructor
	 * @param numberFormat
	 */
	public DecimalValidator(NumberFormat numberFormat) {
		if(numberFormat == null) {
			throw new IllegalArgumentException("A number format must be specified.");
		}
		this.numberFormat = numberFormat;
	}

	/**
	 * Constructor
	 * @param pattern
	 */
	public DecimalValidator(String pattern) {
		if(pattern == null) {
			throw new IllegalArgumentException("A number pattern must be specified.");
		}
		numberFormat = NumberFormat.getFormat(pattern);
	}

	public Object validate(Object value) throws ValidationException {
		if(value == null || value instanceof Float || value instanceof Double) {
			return value;
		}
		try {
			return numberFormat.parse(value.toString());
		}
		catch(final NumberFormatException nfe) {
			throw new ValidationException("Value must be a decimal of format: '" + numberFormat.getPattern() + "'.");
		}
	}
}
