package com.tll.client.validate;

import com.tll.client.ui.IWidgetRef;

/**
 * IValidationFeedback - Definition for handling {@link IError}s.
 * @author jpk
 */
// TODO change to IErrorHandler ?
public interface IValidationFeedback {

	/**
	 * Handles a validation error.
	 * @param source the error source
	 * @param error the error
	 */
	void handleError(IWidgetRef source, IError error);

	/**
	 * Resolves (clears) all validation errors for the given source.
	 * @param source
	 */
	void resolveError(IWidgetRef source);
}
