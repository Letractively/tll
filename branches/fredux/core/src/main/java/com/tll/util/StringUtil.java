package com.tll.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

/**
 * Utilities related to String management and manipulation.
 * @author jpk
 */
public final class StringUtil {

	private static final String BASE_SPECIAL_CHARSET = "`~!@%^*()={}[]|\\;<>,?";
	private static final String ALL_SPECIAL_CHARSET = BASE_SPECIAL_CHARSET + "\"#&$-+_'/:";

	private StringUtil() {
	}

	/**
	 * Returns true if the input string is null or empty, false otherwise.
	 * @param str string to test
	 * @return true if null or empty, false otherwise.
	 */
	public static boolean isEmpty(final String str) {
		return (str == null || str.trim().length() == 0);
	}

	/**
	 * Tests whether two strings are equal. If both strings are null, they are
	 * considered equal by this method.
	 * @param s1 first string to compare
	 * @param s2 second string to compare
	 * @return true if the two strings are equal, false otherwise.
	 */
	public static boolean equals(final String s1, final String s2) {
		if(s1 == null) {
			if(s2 == null) {
				// both are null so the strings are equal
				return true;
			}
			// only s1 is null so they are not equal
			return false;
		}
		// we can safely perform equals on s1
		return s1.equals(s2);
	}

	/**
	 * Convenience method that only takes a single variable argument.
	 * @param str the string with variables in it
	 * @param var the values to replace the variables with
	 * @return the updated string
	 * @see #replaceVariables(String, Object[])
	 */
	public static String replaceVariables(final String str, final Object var) {
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
	public static String replaceVariables(String str, final Object[] vars) {
		if(vars != null) {
			final int length = vars.length;
			for(int i = 0; i < length; i++) {
				str = replace(str, "%" + (i + 1), vars[i] == null ? "" : vars[i].toString());
			}
		}
		return str;
	}

	/**
	 * Finds a section in the input string and replaces it with the string snippet
	 * to replace with.
	 * @param str The original string.
	 * @param find The substring to find in the string.
	 * @param replace The substring to replace the found section with.
	 * @return The newly tailored string.
	 */
	public static String replace(final String str, final String find, final String replace) {
		int strlen, findlen;
		char[] chars, replaceChars;
		if(str == null || ((strlen = (chars = str.toCharArray()).length) < (findlen = find.length()))) {
			return str;
		}
		replaceChars = replace.toCharArray();
		final StringBuilder sb = new StringBuilder(strlen);
		int sCut = 0;
		for(int eCut; (eCut = str.indexOf(find, sCut)) != -1; sCut = eCut + findlen) {
			sb.append(chars, sCut, (eCut - sCut)).append(replaceChars);
		}
		return sCut > 0 ? sb.append(chars, sCut, (strlen - sCut)).toString() : str;
	}

	/**
	 * Method that removes the base set of special characters from the input
	 * string. The base special character set consists of alphanumeric characters
	 * plus {#$&+-_"'/.:}. Whitespaces at the end of the string will also be
	 * removed.
	 * @param str the string to remove special characters
	 * @return the string without the base special characters
	 */
	public static String removeBaseSpecialChars(final String str) {
		return removeSpecialChars(str, BASE_SPECIAL_CHARSET);
	}

	/**
	 * Method that removes special characters from the input string. Special
	 * characters are defined to be any non-alphanumeric characters not including
	 * spaces and periods. Also, any whitespace at the ends of the string will be
	 * removed.
	 * @param str string to strip
	 * @return input string minus any special characters
	 */
	public static String removeAllSpecialChars(final String str) {
		return removeSpecialChars(str, ALL_SPECIAL_CHARSET);
	}

	/**
	 * Helper method that removes the special characters from a string that is
	 * found in the input String of characters. The characters are basically
	 * stripped from the input string.
	 * @param str the input string
	 * @param charset the set of characters to remove.
	 * @return input string with characters in the charset String removed
	 */
	private static String removeSpecialChars(String str, final String charset) {
		if(str == null) {
			return null;
		}
		// remove extra spaces and replace with single space
		str = str.replaceAll(" +", " ");
		final StringBuilder sb = new StringBuilder();
		for(int i = 0; i < str.length(); i++) {
			final char ch = str.charAt(i);
			if(charset.indexOf(ch) == -1) {
				sb.append(ch);
			}
		}
		return sb.toString().trim();
	}

	public static String arrayToString(final String[] arr, final String delimiter) {
		if(arr == null || arr.length == 0) {
			return null;
		}
		final StringBuilder result = new StringBuilder();
		boolean first = true;
		for(final String element : arr) {
			if(!first) {
				result.append(delimiter);
			}
			else {
				first = false;
			}
			result.append(element);
		}
		return result.toString();
	}

	public static String[] stringToArray(final String str, final String delimiter) {
		if(str == null) {
			return null;
		}
		final List<String> result = new ArrayList<String>();
		final StringTokenizer st = new StringTokenizer(str, delimiter);
		while(st.hasMoreTokens()) {
			result.add(st.nextToken());
		}
		return result.toArray(new String[result.size()]);
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
	 * Converts an ENUM_STYLE string to a camelCased string.
	 * @param s
	 * @return {@link String}
	 */
	public static String enumStyleToCamelCase(final String s) {
		return enumStyleToCamelCase(s, false);
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
	public static String formatBeanNotation(final String str) {
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

	/**
	 * Capitalizes the first char in the given camelCasedString inserting spaces
	 * between char sequences having a lower-case char to the left of an
	 * upper-case char.
	 * @param s The camelCasedString (Java convention).
	 * @return A presentation worthy {@link String}.
	 */
	public static String formatCamelCase(final String s) {
		if(s == null || s.length() < 1) return s;
		boolean priorWasLower = false;
		final char[] chars = s.toCharArray();
		final StringBuilder sb = new StringBuilder(chars.length + 32);
		sb.append(Character.toUpperCase(chars[0]));
		for(int i = 1; i < chars.length; i++) {
			final char c = chars[i];
			if(Character.isUpperCase(c)) {
				if(priorWasLower) {
					sb.append(' ');
				}
			}
			sb.append(c);
			priorWasLower = Character.isLowerCase(c);
		}
		return sb.toString();
	}

	/**
	 * Converts an "enum style" string to a more readable form. Specifically,
	 * converts underscores to spaces capitalizing the first char in the string
	 * and all chars immediately after any encountered underscore chars.
	 * @param str an "enum style" string
	 * @return a nice string
	 */
	public static String formatEnumValue(final String str) {
		return WordUtils.capitalizeFully(StringUtils.replace(str, "_", " "));
	}

}
