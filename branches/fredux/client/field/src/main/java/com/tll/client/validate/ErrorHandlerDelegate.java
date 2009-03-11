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
 * ErrorHandlerDelegate - Acts as a single error handler that actually
 * delegates calls to child handlers.
 * @author jpk
 */
public final class ErrorHandlerDelegate implements IErrorHandler {

	private final Set<IErrorHandler> handlers = new HashSet<IErrorHandler>();

	/**
	 * Constructor
	 * @param handlers The handlers to which validation feedback is delegated
	 */
	public ErrorHandlerDelegate(IErrorHandler... handlers) {
		for(final IErrorHandler handler : handlers) {
			if(handler != null) this.handlers.add(handler);
		}
	}

	@Override
	public void handleError(IWidgetRef source, IError error, int attribs) {
		for(final IErrorHandler handler : handlers) {
			handler.handleError(source, error, attribs);
		}
	}

	@Override
	public void resolveError(IWidgetRef source) {
		for(final IErrorHandler handler : handlers) {
			handler.resolveError(source);
		}
	}

	@Override
	public void clear() {
		for(final IErrorHandler handler : handlers) {
			handler.clear();
		}
	}
}