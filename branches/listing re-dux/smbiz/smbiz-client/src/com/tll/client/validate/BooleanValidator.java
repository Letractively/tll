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
	private BooleanValidator() {
		trueStr = DEFAULT_TRUE_STRING;
	}

	/**
	 * Constructor
	 * @param trueStr
	 */
	public BooleanValidator(String trueStr) {
		this.trueStr = trueStr;
	}

	public Object validate(Object value) {
		return (trueStr.equals(value)) ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((trueStr == null) ? 0 : trueStr.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final BooleanValidator other = (BooleanValidator) obj;
		if(trueStr == null) {
			if(other.trueStr != null) return false;
		}
		else if(!trueStr.equals(other.trueStr)) return false;
		return true;
	}

}
