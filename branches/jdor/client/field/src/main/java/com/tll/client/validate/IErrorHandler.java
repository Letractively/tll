package com.tll.client.validate;

import com.tll.client.ui.IWidgetRef;

/**
 * IErrorHandler - Definition for handling {@link IError}s.
 * @author jpk
 */
public interface IErrorHandler {

	/**
	 * @return The display type
	 */
	ErrorDisplay getDisplayType();

	/**
	 * Handles an error.
	 * @param source the error source
	 * @param error the error
	 */
	void handleError(IWidgetRef source, IError error);

	/**
	 * Resolves (clears) all validation errors for the given source.
	 * @param source the field ref. May be <code>null</code>
	 * @param classifier the classifier which may be <code>null</code>
	 */
	void resolveError(IWidgetRef source, ErrorClassifier classifier);

	/**
	 * Removes all errors found to be associated with the given classifier.
	 * @param classifier
	 */
	void clear(ErrorClassifier classifier);

	/**
	 * Removes <em>all</em> errors and internal state.
	 */
	void clear();
}
