/**
 * The Logic Lab
 * @author jpk
 * Feb 17, 2008
 */
package com.tll.client.model;

/**
 * NullNodeInPropPathException - Indicates a <code>null</code> path element
 * was encountered before reaching the end of a given property path while
 * attempting to resolve it against a given {@link Model}.
 * @author jpk
 */
@SuppressWarnings("serial")
class NullNodeInPropPathException extends PropertyPathException {

	/**
	 * Constructor
	 * @param propPath
	 * @param nullProp
	 */
	public NullNodeInPropPathException(final String propPath, final String nullProp) {
		super(nullProp + " is not set for path: " + propPath);
	}

	/**
	 * Constructor
	 * @param message
	 */
	/*
	public NullNodeInPropPathException(String message) {
		super(message);
	}
	*/
}
