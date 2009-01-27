/**
 * The Logic Lab
 * @author jpk
 * Apr 5, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * ViewOpRequest
 * @author jpk
 */
public abstract class ViewOpRequest extends ViewRequestEvent {

	private final ViewKey viewKey;

	/**
	 * Constructor
	 * @param source
	 * @param viewKey
	 */
	public ViewOpRequest(Widget source, ViewKey viewKey) {
		super(source);
		this.viewKey = viewKey;
	}

	@Override
	public ViewKey getViewKey() {
		return viewKey;
	}

}
