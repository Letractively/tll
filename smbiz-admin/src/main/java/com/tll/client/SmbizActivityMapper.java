/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.tll.client.place.HomeViewPlace;

/**
 * @author jpk
 */
public class SmbizActivityMapper implements ActivityMapper {

	private final IClientFactory clientFactory;

	/**
	 * Constructor
	 * @param clientFactory
	 */
	public SmbizActivityMapper(IClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	@Override
	public Activity getActivity(Place place) {
		if(place instanceof HomeViewPlace) {
			return new HomeViewActivity((HomeViewPlace) place, clientFactory);
		}
		return null;
	}

}
