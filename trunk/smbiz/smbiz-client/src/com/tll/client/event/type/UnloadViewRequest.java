/**
 * The Logic Lab
 * @author jpk Jan 17, 2008
 */
package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.mvc.view.ViewKey;

/**
 * UnloadViewRequest
 * @author jpk
 */
public final class UnloadViewRequest extends ViewOpRequest {

	/**
	 * Constructor
	 * @param source
	 * @param viewKey
	 */
	public UnloadViewRequest(Widget source, ViewKey viewKey) {
		super(source, viewKey);
	}

	@Override
	public boolean addHistory() {
		return false;
	}
}