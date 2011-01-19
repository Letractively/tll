/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.tll.client.place.HomeViewPlace;
import com.tll.client.view.IHomeView;

/**
 * @author jpk
 */
public class HomeViewActivity extends AbstractActivity implements IHomeView.Presenter {

	private IClientFactory cf;

	/**
	 * Constructor
	 */
	public HomeViewActivity(HomeViewPlace place, IClientFactory cf) {

	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
	}

	@Override
	public void goTo(Place place) {
		cf.getPlaceController().goTo(place);
	}

}
