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

	public boolean canHandle(IViewRequest request) {
		return request instanceof UnloadViewRequest;
	}

	@Override
	public void handle(IViewRequest request) {
		final UnloadViewRequest r = (UnloadViewRequest) request;

		ViewManager.get().unloadView(r.getViewKey(), r.isRemoveFromCache());
		/*
		// unload the view (deferring it to ensure we are clear of the history "pump" since we subsequently fire a view change event)...
		DeferredCommand.addCommand(new Command() {

			public void execute() {
				ViewManager.get().unloadView(r.getViewKey(), r.isRemoveFromCache());
			}
		});
		*/
	}
}
