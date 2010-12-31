/**
 * The Logic Lab
 * @author jpk Jan 19, 2008
 */
package com.tll.client.mvc;

import com.tll.client.mvc.view.IViewRequest;
import com.tll.client.mvc.view.ShowViewRequest;

/**
 * ShowViewController
 * @author jpk
 */
class ShowViewController implements IController {

	public boolean canHandle(IViewRequest request) {
		return request instanceof ShowViewRequest;
	}

	public void handle(IViewRequest request) {
		final ShowViewRequest r = (ShowViewRequest) request;
		ViewManager.get().setCurrentView(r.getViewInitializer());
	}
}