package com.tll.client.validate;

/**
 * BooleanValidator
 * @author jkirton
 */
public class BooleanValidator implements IValidator {

	public static final BooleanValidator INSTANCE = new BooleanValidator();

	private static final String DEFAULT_TRUE_STRING = "true";

	/**
	 * The compare string for determining if the value resolves to true.
	 */
	private final String trueStr;

	/**
	 * Constructor
	 */
	public BooleanValidator() {
		trueStr = DEFAULT_TRUE_STRING;
	}

	/**
	 * Constructor
	 * @param trueStr
	 */
	public BooleanValidator(final String trueStr) {
		this.trueStr = trueStr;
	}

	@Override
	public Object validate(final Object value) {
		if(value == null || value instanceof Boolean) {
			return value;
		}
		return (trueStr.equals(value.toString())) ? Boolean.TRUE : Boolean.FALSE;
	}
}
