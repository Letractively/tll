/**
 * The Logic Lab
 * @author jpk
 * @since Sep 14, 2009
 */
package com.tll.client.validate;

import com.tll.client.ui.IWidgetRef;


/**
 * AbstractErrorHandler
 * @author jpk
 */
public abstract class AbstractErrorHandler implements IErrorHandler {

	@Override
	public final void handleError(IWidgetRef source, IError error, int displayFlags) {
		final int ef = getDisplayType().flag();
		if((displayFlags & ef) == ef) {
			doHandleError(source, error);
		}
	}

	@Override
	public final void resolveError(IWidgetRef source, ErrorClassifier classifier, int displayFlags) {
		final int ef = getDisplayType().flag();
		if((displayFlags & ef) == ef) {
			doResolveError(source, classifier);
		}
	}

	/**
	 * Does the actual error handling.
	 * @param source
	 * @param error
	 */
	protected abstract void doHandleError(IWidgetRef source, IError error);

	/**
	 * Does the actual error resolving.
	 * @param source
	 * @param classifier
	 */
	protected abstract void doResolveError(IWidgetRef source, ErrorClassifier classifier);
}
