/**
 * The Logic Lab
 * @author jpk Dec 24, 2007
 */
package com.tll.client.model;

/**
 * PropertyPathHelper
 * @author jpk
 */
public abstract class PropertyPathHelper {

	/**
	 * Calculates the property path. Never returns <code>null</code>. Support
	 * for <code>null</code> or empty is considered for all arguments.
	 * <p>
	 * FORMAT: property path = <code>parentPropPath</code> + '.' +
	 * <code>propName</code>
	 * @param parentPropPath Assumed to NOT end in a dot. May be <code>null</code>.
	 * @param propName Assumed to NOT have prefixing/suffixing dots. May be
	 *        <code>null</code>.
	 * @return The calculated property path.
	 */
	public static String getPropertyPath(String parentPropPath, String propName) {
		return (parentPropPath == null || parentPropPath.length() < 1) ? (propName == null ? "" : propName)
				: (propName == null || propName.length() < 1) ? parentPropPath : parentPropPath + '.' + propName;
	}

	/**
	 * Chains the given arguments together to form the corresponding property
	 * path. Support for <code>null</code> or empty is considered for all
	 * arguments.
	 * @param grandParentPropPath The parent property path to
	 *        <code>parentPropPath</code>.
	 * @param parentPropPath The parent property path to <code>propName</code>.
	 * @param propName The property name.
	 * @return The calculated property path.
	 * @see #getPropertyPath(String, String) for formatting details.
	 */
	public static String getPropertyPath(String grandParentPropPath, String parentPropPath, String propName) {
		return getPropertyPath(getPropertyPath(grandParentPropPath, parentPropPath), propName);
	}
}
