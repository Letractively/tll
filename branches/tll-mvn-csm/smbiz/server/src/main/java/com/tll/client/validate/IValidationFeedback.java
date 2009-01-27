package com.tll.client.validate;

/**
 * IValidationFeedback - Serves to propagate validation feedback throughout a
 * particular artifact.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 */
public interface IValidationFeedback {

	/**
	 * Handles the validation exception.
	 * @param source
	 * @param exception
	 */
	void handleException(Object source, ValidationException exception);

	/**
	 * Resolve the validation error.
	 * @param source
	 */
	void resolve(Object source);
}
