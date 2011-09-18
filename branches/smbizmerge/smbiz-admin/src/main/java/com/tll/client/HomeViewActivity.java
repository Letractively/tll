/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.tll.client.place.HomeViewPlace;
import com.tll.client.rpc.ISiteStatisticsService;
import com.tll.client.rpc.ISiteStatisticsService.SiteStatisticsPayload;
import com.tll.client.rpc.ISiteStatisticsServiceAsync;
import com.tll.client.view.IHomeView;

/**
 * @author jpk
 */
public class HomeViewActivity extends AbstractActivity implements IHomeView.Presenter {

	private IClientFactory cf;

	/**
	 * Constructor
	 * @param place 
	 * @param cf 
	 */
	public HomeViewActivity(HomeViewPlace place, IClientFactory cf) {
		this.cf = cf;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
	
		final IHomeView view = cf.getViewFactory().getView(IHomeView.class);
		
		// fetch site stats
		ISiteStatisticsServiceAsync.Util.instance().getSiteStatitics(new AsyncCallback<ISiteStatisticsService.SiteStatisticsPayload>() {
			
			@Override
			public void onSuccess(SiteStatisticsPayload result) {
				view.setStats(result.getDto());
				panel.setWidget(view.asWidget());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Error fetching site stats", caught);
			}
		});
	}

	@Override
	public String mayStop() {
		return super.mayStop();
	}

	@Override
	public void onCancel() {
		super.onCancel();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void goTo(Place place) {
		cf.getPlaceController().goTo(place);
	}

}
