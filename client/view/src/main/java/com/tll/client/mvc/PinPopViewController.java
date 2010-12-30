/**
 * The Logic Lab
 * @author jpk Jan 19, 2008
 */
package com.tll.client.mvc;

import com.tll.client.mvc.view.IViewRequest;
import com.tll.client.mvc.view.PinPopViewRequest;

/**
 * PinPopViewController - Handles {@link PinPopViewRequest}s.
 * @author jpk
 */
class PinPopViewController implements IController {

	@Override
	public boolean canHandle(final IViewRequest request) {
		return request instanceof PinPopViewRequest;
	}

	@Override
	public void handle(final IViewRequest request) {
		final PinPopViewRequest r = (PinPopViewRequest) request;
		if(r.isPop()) {
			// pop the view...
			ViewManager.get().popCurrentView();
		}
		else {
			// pin the view (deferring it to ensure we are clear of the history "pump"
			// since we subsequently fire a view change event)...
			ViewManager.get().pinPoppedView(r.getViewKey());
		}
	}

}