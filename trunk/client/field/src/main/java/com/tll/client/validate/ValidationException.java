/**
 * The Logic Lab
 * @author jpk Apr 13, 2008
 */
package com.tll.client.validate;


/**
 * ValidationException - The one and only validation exception type.
 * @author jpk
 */
@SuppressWarnings("serial")
public final class ValidationException extends Exception {

	/**
	 * The sole error.
	 */
	private final IError error;

	/**
	 * Constructor
	 * @param error
	 */
	public ValidationException(IError error) {
		this.error = error;
	}

	/**
	 * Constructor - Creates a simple scalar (error message only) error.
	 * @param error the error message
	 */
	public ValidationException(final String error) {
		this.error = new Error(ErrorClassifier.CLIENT, error);
	}

	/**
	 * @return the sole error.
	 */
	public IError getError() {
		return error;
	}
}
