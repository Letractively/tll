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
	 * @param displayFlags filter to discriminate whether or not the error should
	 *        be handled by this implementation
	 */
	void handleError(IWidgetRef source, IError error, int displayFlags);

	/**
	 * Resolves (clears) all validation errors for the given source.
	 * @param source the field ref. May be <code>null</code>
	 * @param classifier the classifier which may be <code>null</code>
	 * @param displayFlags filter to discriminate whether or not the error should
	 *        be resolved by this implementation
	 */
	void resolveError(IWidgetRef source, ErrorClassifier classifier, int displayFlags);

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
