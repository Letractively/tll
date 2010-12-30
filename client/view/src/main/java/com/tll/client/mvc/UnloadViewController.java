/**
 * The Logic Lab
 * @author jpk Jan 19, 2008
 */
package com.tll.client.mvc;

import com.tll.client.mvc.view.IViewRequest;
import com.tll.client.mvc.view.UnloadViewRequest;

/**
 * UnloadViewController - Handles {@link UnloadViewRequest}s.
 * @author jpk
 */
class UnloadViewController implements IController {

	@Override
	public boolean canHandle(final IViewRequest request) {
		return request instanceof UnloadViewRequest;
	}

	@Override
	public void handle(final IViewRequest request) {
		final UnloadViewRequest r = (UnloadViewRequest) request;
		ViewManager.get().unloadView(r.getViewKey(), r.isDestroy(), r.isErradicate());
	}
}