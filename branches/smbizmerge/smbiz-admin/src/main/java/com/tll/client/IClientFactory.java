/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.tll.client.view.IViewFactory;
import com.tll.common.dto.SmbizEntityRequestFactory;

/**
 * @author jpk
 */
public interface IClientFactory {

	EventBus getEventBus();

	PlaceController getPlaceController();

	PlaceHistoryHandler getPlaceHistoryHandler();

	SmbizEntityRequestFactory getCrudFactory();

	Messages getMessages();

	IViewFactory getViewFactory();
}
