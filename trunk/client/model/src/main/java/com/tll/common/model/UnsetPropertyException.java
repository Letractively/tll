/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.common.model;

/**
 * UnsetPropertyException - Indicates a model property does not exist.
 * @author jpk
 */
@SuppressWarnings("serial")
public final class UnsetPropertyException extends PropertyPathException {

	/**
	 * Constructor
	 * @param propPath
	 */
	public UnsetPropertyException(final String propPath) {
		super("Property: '" + propPath + "' does not exist.", propPath);
	}

	/**
	 * @return The unset property
	 */
	public String getUnsetProperty() {
		return getPropertyPath();
	}
}
