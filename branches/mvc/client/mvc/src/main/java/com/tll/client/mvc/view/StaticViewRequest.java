/**
 * The Logic Lab
 * @author jpk
 * Apr 5, 2008
 */
package com.tll.client.mvc.view;


/**
 * StaticViewRequest - Show view request for views that are static. I.eThe {@link ViewKey} is
 * resolvable at compile time.
 * @author jpk
 */
public final class StaticViewRequest extends ShowViewRequest {

	/**
	 * Constructor
	 * @param viewClass
	 */
	public StaticViewRequest(ViewClass viewClass) {
		super(viewClass);
	}

	@Override
	protected int getViewId() {
		return 0;
	}

}
