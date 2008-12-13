package com.tll.client.validate;

import com.google.gwt.i18n.client.NumberFormat;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;

/**
 * DecimalValidator
 * @author jpk
 */
public class DecimalValidator implements IValidator {

	public static final DecimalValidator CURRENCY_VALIDATOR =
			new DecimalValidator(Fmt.decimalFormatBindings.get(GlobalFormat.CURRENCY));

	public static final DecimalValidator PERCENT_VALIDATOR =
			new DecimalValidator(Fmt.decimalFormatBindings.get(GlobalFormat.PERCENT));

	public static final DecimalValidator DECIMAL_VALIDATOR =
			new DecimalValidator(Fmt.decimalFormatBindings.get(GlobalFormat.DECIMAL));

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
		catch(NumberFormatException nfe) {
			throw new ValidationException("Must be a decimal of format: '" + numberFormat.getPattern() + "'.");
		}
	}
}
