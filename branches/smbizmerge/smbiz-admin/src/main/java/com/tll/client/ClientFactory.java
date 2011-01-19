/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.tll.client.view.IHomeView;
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

	private final IHomeView homeView = null;

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
		return new SmbizApp();
	}

	@Override
	public PlaceHistoryHandler getPlaceHistoryController() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IHomeView getHomeView() {
		return homeView;
	}

}
