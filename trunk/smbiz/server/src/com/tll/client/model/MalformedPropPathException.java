/**
 * The Logic Lab
 * @author jpk
 * Feb 17, 2008
 */
package com.tll.client.model;

/**
 * MalformedPropPathException - Indicates a general un-parseable property path
 * error.
 * @author jpk
 */
@SuppressWarnings("serial")
public class MalformedPropPathException extends PropertyPathException {

	/**
	 * Constructor
	 * @param propPath
	 */
	public MalformedPropPathException(final String propPath) {
		super("Unable to resolve property path: " + propPath);
	}

}
