/**
 * The Logic Lab
 * @author jpk
 * Feb 17, 2008
 */
package com.tll.client.model;

/**
 * PropertyPathException - Abstract exception class relating to property paths.
 * @author jpk
 */
abstract class PropertyPathException extends Exception {

	/**
	 * Constructor
	 */
	protected PropertyPathException(String message) {
		super(message);
	}

}
