package com.tll.util;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.DateValidator;
import org.apache.oro.text.perl.Perl5Util;

/**
 * Validation utility methods mostly RegExp definitions for common property
 * types.
 * @author jpk
 */
public final class ValidationUtil {

	// US phone number with or without dashes
	public static final String US_PHONE_REGEXP = "/^\\D?(\\d{3})\\D?\\D?(\\d{3})\\D?(\\d{4})$/";

	public static final String INTNL_PHONE_REGEXP = "/^[01]?[- .]?\\(?[2-9]\\d{2}\\)?[- .]?\\d{3}[- .]?\\d{4}$/";

	// US postalCode code with optional dash-four
	public static final String US_ZIPCODE_REGEXP = "/^\\d{5}(-\\d{4})?$/";

	// Two letter province abbreviations
	public static final String US_STATE_REGEXP =
			"/^(AL|AK|AS|AZ|AR|CA|CO|CT|DE|DC|FM|FL|GA|GU|HI|ID|IL|IN|IA|KS|KY|LA|ME|MH|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|MP|OH|OK|OR|PW|PA|PR|RI|SC|SD|TN|TX|UT|VT|VI|VA|WA|WV|WI|WY)$/";

	// private static final String US_DATE_REGEXP =
	// "/^(?:(?:(?:0?[13578]|1[02])(\\/|-|\\.)31)\\1|(?:(?:0?[1,3-9]|1[0-2])(\\/|-|\\.)(?:29|30)\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:0?2(\\/|-|\\.)29\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:(?:0?[1-9])|(?:1[0-2]))(\\/|-|\\.)(?:0?[1-9]|1\\d|2[0-8])\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$/";
	// Date in US format with support for leap years

	// credit card
	// private static final String CREDIT_CARD_REGEXP =
	// "/^((4\\d{3})|(5[1-5]\\d{2})|(6011))-?\\d{4}-?\\d{4}-?\\d{4}|3[4,7]\\d{13}$/";

	// 8 to 16 character password requiring numbers, lowercase letters, and
	// uppercase letters
	public static final String PASSWORD_REGEXP = "/^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$/";

	// ssn
	public static final String SSN_REGEXP = "/^\\d{3}-\\d{2}-\\d{4}$/";

	public static boolean isValidUsaStateAbbr(String s) {
		return (s == null) ? false : ("us".equals(s) || "usa".equals(s));
	}

	public static boolean isValidDate(String s, Locale locale) {
		return DateValidator.getInstance().isValid(s, locale);
	}

	public static boolean isValidPassword(String s) {
		return (s == null || s.length() < 8) ? false : (new Perl5Util()).match(PASSWORD_REGEXP, StringUtils.strip(s));
	}

	private ValidationUtil() {
	}
}
