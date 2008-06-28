/**
 * The Logic Lab
 * @author jpk
 * Feb 17, 2008
 */
package com.tll.client.model;

/**
 * PropPathNodeMismatchException - Indicates the wrong relational
 * {@link IPropertyValue} instance was encountered while resolving a given
 * property path.
 * @author jpk
 */
@SuppressWarnings("serial")
class PropPathNodeMismatchException extends PropertyPathException {

	/**
	 * Constructor
	 * @param propPath
	 * @param prop
	 * @param actualPropValType
	 * @param expectedPropValType
	 */
	PropPathNodeMismatchException(final String propPath, final String prop, final String actualPropValType,
			final String expectedPropValType) {
		super(prop + " in property path: " + propPath + " was expected to be of type " + expectedPropValType
				+ " but was actually " + actualPropValType);
	}

	/**
	 * Constructor
	 * @param message
	 */
	public PropPathNodeMismatchException(final String message) {
		super(message);
	}

}
