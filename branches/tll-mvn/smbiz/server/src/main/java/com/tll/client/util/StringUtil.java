/**
 * The Logic Lab
 * @author jpk Aug 30, 2007
 */
package com.tll.client.util;

/**
 * StringUtil - String formatting specific utility methods in the JavaScript
 * domain.
 * @author jpk
 */
public abstract class StringUtil {

	/**
	 * Tests for string emptiness (null or zero-length).
	 * @param s String
	 * @return boolean
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.length() < 1;
	}

	/**
	 * Abbreviates a string.
	 * @param str
	 * @param length
	 * @return {@link String}
	 */
	public static String abbr(String str, int length) {
		if(isEmpty(str) || str.length() <= length) {
			return str;
		}
		return str.substring(0, length - 3) + "...";
	}

	/**
	 * Finds a section in the input string and replaces it with the string snippet
	 * to replace with.
	 * @param str The original string.
	 * @param find The substring to find in the string.
	 * @param replace The substring to replace the found section with.
	 * @return The newly tailored string.
	 */
	public static String replace(String str, String find, String replace) {
		int strlen, findlen;
		char[] chars, replaceChars;
		if(str == null || ((strlen = (chars = str.toCharArray()).length) < (findlen = find.length()))) {
			return str;
		}
		replaceChars = replace.toCharArray();
		StringBuilder sb = new StringBuilder(strlen);
		int sCut = 0;
		for(int eCut; (eCut = str.indexOf(find, sCut)) != -1; sCut = eCut + findlen) {
			sb.append(chars, sCut, (eCut - sCut)).append(replaceChars);
		}
		return sCut > 0 ? sb.append(chars, sCut, (strlen - sCut)).toString() : str;
	}

	/**
	 * Convenience method that only takes a single variable argument.
	 * @param str the string with variables in it
	 * @param var the values to replace the variables with
	 * @return the updated string
	 * @see #replaceVariables(String, Object[])
	 */
	public static String replaceVariables(String str, Object var) {
		return replaceVariables(str, new Object[] { var });
	}

	/**
	 * Replaces variables within a string of the form "%x" where x is the index
	 * into the variable array + 1. For example, "test %1" will replace the %1
	 * with the value in the first element (0th index) of the variable array. This
	 * method will replace all occurrences of the variables within the string. If
	 * a variable is null, it will replace the variable with an empty string.
	 * @param str the string with variables in it
	 * @param vars the values to replace the variables with
	 * @return the updated string
	 */
	public static String replaceVariables(String str, Object[] vars) {
		int length = (vars == null ? 0 : vars.length);
		for(int i = 0; i < length; i++) {
			str = replace(str, "%" + (i + 1), vars[i] == null ? "" : vars[i].toString());
		}
		return str;
	}

	/**
	 * Converts a Presentation Style string to an ENUM_STYLE string.
	 * @param s The presentation style String
	 * @return Enum styled string
	 */
	public static String presentationToEnumStyle(String s) {
		if(s == null || s.length() < 1) return s;
		boolean priorWasLower = false;
		char[] chars = s.toCharArray();
		StringBuilder sb = new StringBuilder(chars.length + 32);
		for(int i = 0; i < chars.length; i++) {
			if(Character.isUpperCase(chars[i])) {
				if(priorWasLower && i > 0) {
					sb.append('_');
				}
			}
			sb.append(Character.toUpperCase(chars[i]));
			priorWasLower = Character.isLowerCase(chars[i]);
		}
		return sb.toString();
	}

	/**
	 * Converts an ENUM_STYLE string to a presentation worthy String.
	 * @param s The enum styled string
	 * @return Human friendly String.
	 */
	public static String enumStyleToPresentation(String s) {
		if(s == null || s.length() < 1) return s;
		boolean priorWasUnderscore = false;
		char[] chars = s.toCharArray();
		StringBuilder sb = new StringBuilder(chars.length + 32);
		sb.append(Character.toUpperCase(chars[0]));
		for(int i = 1; i < chars.length; i++) {
			if(priorWasUnderscore) {
				sb.append(' ');
			}
			char c = priorWasUnderscore ? Character.toUpperCase(chars[i]) : Character.toLowerCase(chars[i]);
			if(c != '_') {
				sb.append(c);
			}
			priorWasUnderscore = (c == '_');
		}
		return sb.toString();
	}
}
