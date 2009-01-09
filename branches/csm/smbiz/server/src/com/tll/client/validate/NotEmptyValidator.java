package com.tll.client.validate;

/**
 * NotEmptyValidator
 * @author jkirton
 */
public class NotEmptyValidator implements IValidator {

	public static final NotEmptyValidator INSTANCE = new NotEmptyValidator();

	/**
	 * Constructor
	 */
	private NotEmptyValidator() {
	}

	public Object validate(Object value) throws ValidationException {
		final String s = value == null ? null : value.toString();
		if(s == null || s.length() < 1) {
			throw new ValidationException("Value cannot be empty.");
		}
		return value;
	}
}