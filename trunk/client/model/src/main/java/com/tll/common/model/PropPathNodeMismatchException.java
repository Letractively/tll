/**
 * The Logic Lab
 * @author jpk
 * Feb 17, 2008
 */
package com.tll.common.model;

/**
 * PropPathNodeMismatchException - An unexpected {@link IModelProperty} type was
 * provided different from a prescribed expected type.
 * @author jpk
 */
@SuppressWarnings("serial")
public final class PropPathNodeMismatchException extends PropertyPathException {

	private final String property, actualType, expectedType;

	/**
	 * Constructor
	 * @param propPath
	 * @param property
	 * @param actualType
	 * @param expectedType
	 */
	PropPathNodeMismatchException(final String propPath, final String property, final String actualType,
			final String expectedType) {
		super(property + " in property path: " + propPath + " was expected to be of type " + expectedType
				+ " but was actually " + actualType, property);
		this.property = property;
		this.actualType = actualType;
		this.expectedType = expectedType;
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @return the actualType
	 */
	public String getActualType() {
		return actualType;
	}

	/**
	 * @return the expectedType
	 */
	public String getExpectedType() {
		return expectedType;
	}

}
