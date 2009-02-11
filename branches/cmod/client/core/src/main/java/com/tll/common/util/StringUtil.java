/**
 * The Logic Lab
 * @author jpk Aug 30, 2007
 */
package com.tll.common.util;

/**
 * StringUtil - String formatting specific utility methods in the JavaScript
 * domain.
 * @author jpk
 */
public abstract class StringUtil {

	/**
	 * Returns true if the input string is null or empty, false otherwise.
	 * @param str string to test
	 * @return true if null or empty, false otherwise.
	 */
	public static boolean isEmpty(final String str) {
		return (str == null || str.trim().length() == 0);
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
		if(vars != null) {
			for(int i = 0; i < vars.length; i++) {
				str = replace(str, "%" + (i + 1), vars[i] == null ? "" : vars[i].toString());
			}
		}
		return str;
	}

	/**
	 * Converts a Presentation Style string to an ENUM_STYLE string.
	 * @param s The presentation style String
	 * @return Enum styled string
	 */
	/*
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
	*/
	
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

	/**
	 * Converts a camelCased string to an ENUM_STYLE string.
	 * @param s
	 * @return {@link String}
	 */
	public static String camelCaseToEnumStyle(final String s) {
		if(s == null || s.length() < 1) return s;
		boolean priorWasLower = false;
		final char[] chars = s.toCharArray();
		final StringBuilder sb = new StringBuilder(chars.length + 32);
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
	 * Converts an ENUM_STYLE string to a camelCased string.
	 * @param s
	 * @return {@link String}
	 */
	public static String enumStyleToCamelCase(final String s) {
		return enumStyleToCamelCase(s, false);
	}

	/**
	 * Converts an ENUM_STYLE string to a camelCased string.
	 * @param s
	 * @param firstCharCapitalized Capitalize the first character in the string?
	 * @return {@link String}
	 */
	private static String enumStyleToCamelCase(final String s, final boolean firstCharCapitalized) {
		if(s == null || s.length() < 1) return s;
		boolean priorWasUnderscore = false;
		final char[] chars = s.toCharArray();
		final StringBuilder sb = new StringBuilder(chars.length + 32);
		sb.append(firstCharCapitalized ? Character.toUpperCase(chars[0]) : Character.toLowerCase(chars[0]));
		for(int i = 1; i < chars.length; i++) {
			final char c = priorWasUnderscore ? Character.toUpperCase(chars[i]) : Character.toLowerCase(chars[i]);
			if(c != '_') {
				sb.append(c);
			}
			priorWasUnderscore = (c == '_');
		}
		return sb.toString();
	}

	/**
	 * Converts an ENUM_STYLE string to a JavaClassNotation string.
	 * @param s
	 * @return {@link String}
	 */
	public static String enumStyleToJavaClassNotation(final String s) {
		return enumStyleToCamelCase(s, true);
	}

	/**
	 * Converts a JavaClassNotation string to an ENUM_STYLE string.
	 * @param s
	 * @return {@link String}
	 */
	public static String javaClassNotationToEnumStyle(final String s) {
		return camelCaseToEnumStyle(s);
	}

	/**
	 * Converts an OGNL token (Object Graph Notation Language) string into a
	 * user-presentable string. <br>
	 * E.g.:
	 * 
	 * <pre>
	 * &quot;bean1.bean2.bean3&quot; -&gt; &quot;Bean1 Bean2 Bean3&quot;
	 * </pre>
	 * @param str bean notation string
	 * @return a string representation that can be shown to a user.
	 */
	public static String ognlToPresentation(final String str) {
		// track whether we have encountered a period
		// default to true since we always want to capitalize the first character
		boolean foundDot = true;
		final StringBuilder result = new StringBuilder();
		for(int i = 0; i < str.length(); i++) {
			final char ch = str.charAt(i);
			if(foundDot) {
				// set flag to false and upper case
				foundDot = false;
				result.append(Character.toUpperCase(ch));
			}
			else if(ch == '.') {
				// add a space in place of the period and set flag to true
				result.append(" ");
				foundDot = true;
			}
			else if(Character.isLowerCase(ch)) {
				// just regurgitate the character
				result.append(ch);
			}
			else {
				// if uppercase, add a space
				result.append(" ");
				result.append(ch);
			}
		}
		return result.toString();
	}
}
