/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.tll.client.view.IViewFactory;
import com.tll.common.dto.SmbizEntityRequestFactory;

/**
 * @author jpk
 */
public interface IClientFactory {

	EventBus getEventBus();
	PlaceController getPlaceController();
	//PlaceHistoryHandler getPlaceHistoryController();
	SmbizEntityRequestFactory getCrudFactory();
	SmbizApp getSmbizApp();
	
	Messages getMessages();
	
	IViewFactory getViewFactory();
}
