/**
 * The Logic Lab
 * @author jpk
 * Apr 5, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * StaticViewRequest - Show view request for views that are static. I.eThe {@link ViewKey} is
 * resolvable at compile time.
 * @author jpk
 */
public final class StaticViewRequest extends ShowViewRequest {

	/**
	 * Constructor
	 * @param source
	 * @param viewClass
	 */
	public StaticViewRequest(Widget source, ViewClass viewClass) {
		super(source, viewClass);
	}

	@Override
	protected int getViewId() {
		return 0;
	}

}
