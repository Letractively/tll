package com.tll.client.validate;

import com.tll.client.ui.IWidgetRef;

/**
 * IErrorHandler - Definition for handling {@link IError}s.
 * @author jpk
 */
public interface IErrorHandler {
	
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
	
	/**
	 * Life-cycle provision to clear out <em>all</em> errors and internal state.
	 */
	void clear();
}
