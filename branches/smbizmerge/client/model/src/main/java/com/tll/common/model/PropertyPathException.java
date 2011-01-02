/**
 * The Logic Lab
 * @author jpk
 * Feb 17, 2008
 */
package com.tll.common.model;

/**
 * PropertyPathException - Abstract exception class relating to property paths.
 * @author jpk
 */
@SuppressWarnings("serial")
public abstract class PropertyPathException extends Exception {

	private final String propPath;

	/**
	 * Constructor
	 * @param message The error message
	 * @param propPath The property path that raised the exception
	 */
	protected PropertyPathException(final String message, final String propPath) {
		super(message);
		this.propPath = propPath;
	}

	/**
	 * @return The property path that raised this exception
	 */
	public final String getPropertyPath() {
		return propPath;
	}
}
