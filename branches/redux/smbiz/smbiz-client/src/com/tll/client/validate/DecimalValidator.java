package com.tll.client.validate;

import com.google.gwt.i18n.client.NumberFormat;
import com.tll.client.util.Fmt;
import com.tll.client.util.Fmt.DecimalFormat;

/**
 * DecimalValidator
 * @author jpk
 */
public class DecimalValidator implements IValidator {

	public static final DecimalValidator CURRENCY_VALIDATOR =
			new DecimalValidator(Fmt.decimalFormatBindings.get(DecimalFormat.CURRENCY));

	public static final DecimalValidator PERCENT_VALIDATOR =
			new DecimalValidator(Fmt.decimalFormatBindings.get(DecimalFormat.PERCENT));

	public static final DecimalValidator DECIMAL_VALIDATOR =
			new DecimalValidator(Fmt.decimalFormatBindings.get(DecimalFormat.DECIMAL));

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

	@Override
	public final int hashCode() {
		return numberFormat.getPattern().hashCode();
	}

	@Override
	public final boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final DecimalValidator other = (DecimalValidator) obj;
		return other.numberFormat.getPattern().equals(numberFormat.getPattern());
	}
}
