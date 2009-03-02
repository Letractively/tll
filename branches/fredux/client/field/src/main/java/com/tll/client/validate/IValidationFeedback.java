package com.tll.client.validate;

/**
 * IValidationFeedback - Serves to propagate validation feedback throughout a
 * particular artifact.
 * @author jpk
 * @param <T> the source type
 */
public interface IValidationFeedback<T> {

	/**
	 * Handles the validation exception.
	 * @param source
	 * @param exception
	 */
	void handleException(T source, ValidationException exception);

	/**
	 * Resolve the validation error.
	 * @param source
	 */
	void resolve(T source);
}
