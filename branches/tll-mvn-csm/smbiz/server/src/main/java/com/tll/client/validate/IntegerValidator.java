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

	public Object validate(Object value) throws ValidationException {
		if(value == null || value instanceof Integer) return value;
		try {
			return Integer.valueOf(value.toString());
		}
		catch(NumberFormatException nfe) {
			throw new ValidationException("Must be an integer value.");
		}
	}
}
