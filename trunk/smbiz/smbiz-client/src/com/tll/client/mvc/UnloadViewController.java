/**
 * The Logic Lab
 * @author jpk Jan 19, 2008
 */
package com.tll.client.mvc;

import com.google.gwt.user.client.History;
import com.tll.client.event.type.UnloadViewRequest;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.mvc.view.AbstractView;
import com.tll.client.mvc.view.ViewKey;
import com.tll.client.ui.view.ViewContainer;

/**
 * UnloadViewController - Handles {@link UnloadViewRequest}s.
 * @author jpk
 */
class UnloadViewController extends AbstractController {

	public boolean canHandle(ViewRequestEvent request) {
		return request instanceof UnloadViewRequest;
	}

	@Override
	public void handle(ViewRequestEvent request) {
		UnloadViewRequest r = (UnloadViewRequest) request;

		final ViewKey viewKey = r.getViewKey();
		assert viewKey != null;

		// get the view to be unloaded
		ViewContainer viewToUnload = ViewManager.instance().findView(viewKey);
		if(viewToUnload == null) {
			throw new IllegalArgumentException("Unable to resolve existing view for unloading");
		}

		// unload the view
		AbstractView newestPinned = ViewManager.instance().unloadView(viewToUnload);
		if(newestPinned != null) {
			// Dispatcher.instance().dispatch(new SimpleViewRequest(r.getWidget(),
			// newestPinned.getViewKey()));
			History.newItem(newestPinned.getViewKey().getViewKeyHistoryToken());
		}
	}

}
