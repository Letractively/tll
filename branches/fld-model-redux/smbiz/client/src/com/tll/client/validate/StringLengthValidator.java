package com.tll.client.validate;

/**
 * StringLengthValidator
 * @author jkirton
 */
public class StringLengthValidator implements IValidator {

	private final int min, max;

	/**
	 * Validates a String for min/max length.
	 * @param value The String to validate
	 * @param min The min allowed length or <code>-1</code> if not bounded by a
	 *        minimum length constraint.
	 * @param max The max allowed length or <code>-1</code> if not bounded by a
	 *        maximum length constraint.
	 * @return The validated Object
	 * @throws ValidationException When the String's length is out of bounds.
	 */
	public static Object validate(Object value, int min, int max) throws ValidationException {
		final int len = (value == null ? 0 : value.toString().length());
		if(min == -1 && max != -1) {
			if(len > max) throw new ValidationException("Value cannot exceed " + max + " characters.");
		}
		else if(min != -1 && max == -1) {
			if(len < min) throw new ValidationException("Value must be at least " + min + " characters.");
		}
		else if((len < min) || (len > max)) {
			throw new ValidationException("Value must be at least " + min + "and no more than " + max + " characters.");
		}
		return value;
	}

	/**
	 * Constructor
	 * @param minCharacters
	 * @param maxCharacters
	 */
	public StringLengthValidator(int minCharacters, int maxCharacters) {
		if(minCharacters < 0 || maxCharacters < minCharacters) {
			throw new IllegalArgumentException("Invalid min/max lengths");
		}
		this.min = minCharacters;
		this.max = maxCharacters;
	}

	public Object validate(Object value) throws ValidationException {
		return validate(value, min, max);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		StringLengthValidator other = (StringLengthValidator) obj;
		if(max != other.max) return false;
		if(min != other.min) return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + max;
		result = prime * result + min;
		return result;
	}
}
