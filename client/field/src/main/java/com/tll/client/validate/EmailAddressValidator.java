/**
 * The Logic Lab
 * @author jpk
 * Dec 15, 2008
 */
package com.tll.client.validate;

/**
 * EmailAddressValidator - Email address validation
 * <p>
 * Logic taken from: hibernate's EmailValidator class.
 * @author jpk
 */
public class EmailAddressValidator implements IValidator {

	public static final EmailAddressValidator INSTANCE = new EmailAddressValidator();

	private static final String ATOM = "[^\\x00-\\x1F^\\(^\\)^\\<^\\>^\\@^\\,^\\;^\\:^\\\\^\\\"^\\.^\\[^\\]^\\s]";
	private static final String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)*";
	private static final String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

	private static final String pattern = "^" + ATOM + "+(\\." + ATOM + "+)*@" + DOMAIN + "|" + IP_DOMAIN + ")$";

	/**
	 * Constructor
	 */
	private EmailAddressValidator() {
		super();
	}

	@Override
	public Object validate(final Object value) throws ValidationException {
		if(value == null) return null;
		final String string = value.toString();
		if(string.length() == 0) return string;
		if(!string.matches(pattern)) {
			throw new ValidationException("Invalid email address.");
		}
		return string;
	}
}
