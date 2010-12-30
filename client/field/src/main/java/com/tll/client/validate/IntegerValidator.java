package com.tll.client.validate;

/**
 * IntegerValidator
 * @author jkirton
 */
public class IntegerValidator implements IValidator {

	public static final IntegerValidator INSTANCE = new IntegerValidator();

	/**
	 * Constructor
	 */
	private IntegerValidator() {
	}

	@Override
	public Object validate(final Object value) throws ValidationException {
		if(value == null || value instanceof Integer) return value;
		try {
			return Integer.valueOf(value.toString());
		}
		catch(final NumberFormatException nfe) {
			throw new ValidationException("Value must be a numeric integer.");
		}
	}
}
