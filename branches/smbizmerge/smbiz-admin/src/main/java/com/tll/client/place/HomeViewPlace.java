/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * @author jpk
 */
public class HomeViewPlace extends Place {

	public static class Tokenizer implements PlaceTokenizer<HomeViewPlace> {

		@Override
		public HomeViewPlace getPlace(String token) {
			return new HomeViewPlace();
		}

		@Override
		public String getToken(HomeViewPlace place) {
			return "homeView";
		}
		
	}
}
