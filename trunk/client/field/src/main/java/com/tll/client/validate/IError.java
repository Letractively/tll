/**
 * The Logic Lab
 * @author jpk
 * Mar 4, 2009
 */
package com.tll.client.validate;


/**
 * IError
 * @author jpk
 */
public interface IError {

	/**
	 * Type - The error type.
	 * @author jpk
	 */
	static enum Type {
		/**
		 * A single error.
		 */
		SINGLE,
		/**
		 * A collection of errors.
		 */
		COMPOSITE;
	}

	/**
	 * @return The validation error type.
	 */
	Type getType();

	/**
	 * @return The "sourcing" (classifier) for this error.
	 */
	ErrorClassifier getClassifier();
}
