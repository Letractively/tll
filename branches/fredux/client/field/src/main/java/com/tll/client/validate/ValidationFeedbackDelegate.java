/**
 * The Logic Lab
 * @author jpk
 * Mar 4, 2009
 */
package com.tll.client.validate;

import java.util.HashSet;
import java.util.Set;

import com.tll.client.ui.IWidgetRef;

/**
 * ValidationFeedbackDelegate - Acts as a single error handler that actually
 * delegates calls to child handlers.
 * @author jpk
 */
public class ValidationFeedbackDelegate implements IValidationFeedback {
	
	private final Set<IValidationFeedback> handlers = new HashSet<IValidationFeedback>();

	/**
	 * Constructor
	 * @param handlers The handlers to which validation feedback is delegated
	 */
	public ValidationFeedbackDelegate(IValidationFeedback... handlers) {
		for(final IValidationFeedback handler : handlers) {
			this.handlers.add(handler);
		}
	}

	@Override
	public void handleError(IWidgetRef source, IError error) {
		for(final IValidationFeedback handler : handlers) {
			handler.handleError(source, error);
		}
	}

	@Override
	public void resolveError(IWidgetRef source) {
		for(final IValidationFeedback handler : handlers) {
			handler.resolveError(source);
		}
	}
}
