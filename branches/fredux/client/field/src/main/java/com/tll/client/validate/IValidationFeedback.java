package com.tll.client.validate;

import com.tll.client.ui.IWidgetProvider;

/**
 * IValidationFeedback - Serves to propagate validation feedback throughout a
 * particular artifact.
 * @author jpk
 * @param <T> the source type able to identify a widget
 */
public interface IValidationFeedback<T extends IWidgetProvider> {

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
