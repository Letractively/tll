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
	 * Tests two Strings for equality. Same as {@link String#equals(Object)}.
	 * Either argument may be <code>null</code>.
	 * @param a a String, or <code>null</code>
	 * @param b a String, or <code>null</code>
	 * @return <code>true</code> if
	 *         <code>(a == b || a != null && a.equals(b))</code>
	 */
	public static native boolean equals(String a, String b) /*-{
		return a == b;
	}-*/;

	// return s==null? null : s.test(/^\s+|\s+$/g);
	public static native boolean isWhitespace(String s)/*-{
							  var reg = /^\s+|\s+$/g
							  return s==null? null : reg.test(s);
							}-*/;

	public static native String trim(String s)/*-{
								try { return s.replace(/^\s+|\s+$/g, ""); } catch(e) { return s; }
							}-*/;

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
		StringBuffer sb = new StringBuffer(strlen);
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

	/*
	// pulled from apache commons-lang 2.3
	// private static boolean isDelimiter(char ch, char[] delimiters) {
	private static boolean isDelimiter(String ch, String delimiters) {
		if(delimiters == null) {
			return isWhitespace(ch);
		}
		for(int i = 0, isize = delimiters.length(); i < isize; i++) {
			if(ch == delimiters.substring(i, i + 1)) {
				return true;
			}
		}
		return false;
	}

	// pulled from apache commons-lang 2.3
	// public static String capitalize(String str, char[] delimiters) {
	public static String capitalize(String str, String delimiters) {
		int delimLen = (delimiters == null ? -1 : delimiters.length());
		if(str == null || str.length() == 0 || delimLen == 0) {
			return str;
		}
		int strLen = str.length();
		StringBuffer buffer = new StringBuffer(strLen);
		boolean capitalizeNext = true;
		for(int i = 0; i < strLen; i++) {
			// char ch = str.charAt(i);
			String ch = str.substring(i, i + 1);

			if(isDelimiter(ch, delimiters)) {
				buffer.append(ch);
				capitalizeNext = true;
			}
			else if(capitalizeNext) {
				// buffer.append(Character.toTitleCase(ch));
				buffer.append(ch.toUpperCase());
				capitalizeNext = false;
			}
			else {
				buffer.append(ch);
			}
		}
		return buffer.toString();
	}

	// pulled from apache commons-lang 2.3
	public static String capitalizeFully(String str) {
		return capitalizeFully(str, null);
	}

	// pulled from apache commons-lang 2.3
	public static String capitalizeFully(String str, String delimiters) {
		int delimLen = (delimiters == null ? -1 : delimiters.length());
		if(str == null || str.length() == 0 || delimLen == 0) {
			return str;
		}
		str = str.toLowerCase();
		return capitalize(str, delimiters);
	}
	*/

	/**
	 * Converts a Presentation Style string to an ENUM_STYLE string.
	 * @param s The presentation style String
	 * @return Enum styled string
	 */
	public static String presentationToEnumStyle(String s) {
		if(s == null || s.length() < 1) return s;
		boolean priorWasLower = false;
		char[] chars = s.toCharArray();
		StringBuffer sb = new StringBuffer(chars.length + 32);
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
		StringBuffer sb = new StringBuffer(chars.length + 32);
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
