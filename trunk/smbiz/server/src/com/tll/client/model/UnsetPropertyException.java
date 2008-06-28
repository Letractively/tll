/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.client.model;

/**
 * UnsetPropertyException
 * @author jpk
 */
@SuppressWarnings("serial")
public class UnsetPropertyException extends PropertyPathException {

	/**
	 * Constructor
	 * @param propPath
	 */
	public UnsetPropertyException(final String propPath) {
		super("Property: '" + propPath + "' does not exist.");
	}

}
