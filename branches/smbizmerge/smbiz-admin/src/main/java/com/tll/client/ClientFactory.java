/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.tll.client.view.IViewFactory;
import com.tll.common.dto.SmbizEntityRequestFactory;

/**
 * Default {@link IClientFactory} impl.
 * @author jpk
 */
public class ClientFactory implements IClientFactory {

	private final EventBus eventBus = new SimpleEventBus();
	private final PlaceController placeController = new PlaceController(eventBus);
	private final PlaceHistoryHandler placeHistoryHandler;
	private final SmbizEntityRequestFactory erf = GWT.create(SmbizEntityRequestFactory.class);
	
  private final Messages messages = GWT.create(Messages.class);

	private final IViewFactory viewFactory = GWT.create(IViewFactory.class);

	/**
	 * Constructor
	 */
	public ClientFactory() {
		SmbizPlaceHistoryMapper historyMapper = GWT.create(SmbizPlaceHistoryMapper.class);
		placeHistoryHandler = new PlaceHistoryHandler(historyMapper);
	}

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public PlaceController getPlaceController() {
		return placeController;
	}

	public PlaceHistoryHandler getPlaceHistoryHandler() {
		return placeHistoryHandler;
	}

	@Override
	public SmbizEntityRequestFactory getCrudFactory() {
		return erf;
	}

	@Override
	public SmbizApp getSmbizApp() {
		return new SmbizApp(new SmbizShell());
	}

	@Override
	public IViewFactory getViewFactory() {
		return viewFactory;
	}

	@Override
	public Messages getMessages() {
		return messages;
	}
}
