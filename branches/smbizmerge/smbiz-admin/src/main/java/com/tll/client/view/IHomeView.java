/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client.view;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * The "home page" view displayed upon user account login.
 * @author jpk
 */
public interface IHomeView extends IsWidget {

	public interface Presenter {
		
		void goTo(Place place);
	}
}
