package com.tll.client.validate;

import com.tll.client.ui.IWidgetProvider;
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
	 * Turn on or off the display the validation feedback.
	 * @param source the source
	 */
	void toggleErrorNotification(IWidgetProvider source);
	
	/**
	 * Life-cycle provision to clear out <em>all</em> errors and internal state.
	 */
	void clear();
}
