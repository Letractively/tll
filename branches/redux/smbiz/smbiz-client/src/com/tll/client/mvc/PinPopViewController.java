/**
 * The Logic Lab
 * @author jpk Jan 19, 2008
 */
package com.tll.client.mvc;

import com.google.gwt.user.client.History;
import com.tll.client.event.type.PinPopViewRequest;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.mvc.view.ViewKey;
import com.tll.client.ui.view.ViewContainer;

/**
 * PinPopViewController - Handles {@link PinPopViewRequest}s.
 * @author jpk
 */
class PinPopViewController extends AbstractController {

	public boolean canHandle(ViewRequestEvent request) {
		return request instanceof PinPopViewRequest;
	}

	@Override
	public void handle(ViewRequestEvent request) {
		PinPopViewRequest r = (PinPopViewRequest) request;
		ViewKey key;
		if(r.isPop()) {
			// pop the view...
			key = ViewManager.instance().popCurrentView();
			if(key != null) {
				// route through dispatcher to update history
				// Dispatcher.instance().dispatch(new SimpleViewRequest(r.getWidget(), key));
				History.newItem(key.getViewKeyHistoryToken());
			}
		}
		else {
			// pin the view...
			ViewContainer vc = ViewManager.instance().findView(r.getViewKey());
			assert vc != null;
			if(ViewManager.instance().closePoppedView(vc)) {
				onViewReady(vc.getView());
			}
		}
	}

}
