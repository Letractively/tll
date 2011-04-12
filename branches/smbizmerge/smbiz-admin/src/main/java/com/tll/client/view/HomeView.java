/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.tll.common.dto.SiteStatisticsDto;

/**
 * @author jpk
 */
public class HomeView extends Composite implements IHomeView {
	
	public static class Clz extends ViewClass {

		@Override
		public String getName() {
			return IHomeView.class.getName();
		}

		@Override
		public IView newView() {
			return new HomeView();
		}
	}

	private static HomeViewUiBinder uiBinder = GWT.create(HomeViewUiBinder.class);

	interface HomeViewUiBinder extends UiBinder<Widget, HomeView> {
	}
	
	@UiField Label numIsps;
	@UiField Label numMerchants;
	@UiField Label numCustomers;
	@UiField Label numUsers;
	@UiField Label numAddresses;

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public HomeView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setStats(SiteStatisticsDto stats) {
		numIsps.setText(Integer.toString(stats.numIsps));
		numMerchants.setText(Integer.toString(stats.numMerchants));
		numCustomers.setText(Integer.toString(stats.numCustomers));
		numUsers.setText(Integer.toString(stats.numUsers));
		numAddresses.setText(Integer.toString(stats.numAddresses));
	}
}
