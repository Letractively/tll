/**
 * The Logic Lab
 * @author jpk
 * Feb 17, 2008
 */
package com.tll.client.model;

/**
 * IndexOutOfRangeInPropPathException - Indicates a given index w/in a property
 * path is out of range.
 * @author jpk
 */
@SuppressWarnings("serial")
class IndexOutOfRangeInPropPathException extends PropertyPathException {

	/**
	 * Constructor
	 * @param propPath
	 * @param prop
	 * @param index
	 */
	IndexOutOfRangeInPropPathException(final String propPath, final String prop, final int index) {
		super("Property index : " + index + " for prop: '" + prop + "' is out of range in prop path: " + propPath);
	}

	/**
	 * Constructor
	 * @param message
	 */
	public IndexOutOfRangeInPropPathException(final String message) {
		super(message);
	}

}
