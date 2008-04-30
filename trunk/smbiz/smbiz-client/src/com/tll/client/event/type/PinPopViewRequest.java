/**
 * The Logic Lab
 * @author jpk Jan 26, 2008
 */
package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.mvc.view.ViewKey;

/**
 * PinPopViewRequest - Request to either pin or pop a AbstractView.
 * @author jpk
 */
public final class PinPopViewRequest extends ViewOpRequest {

	private final boolean pop;

	/**
	 * Constructor
	 * @param source
	 * @param viewKey May be <code>null</code>
	 * @param pop <code>true</code> when requesting to pop the current view,
	 *          <code>false<code> when requesting to pin a popped view.
	 */
	public PinPopViewRequest(Widget source, ViewKey viewKey, boolean pop) {
		super(source, viewKey);
		this.pop = pop;
	}

	public boolean isPop() {
		return pop;
	}

	@Override
	public boolean addHistory() {
		return !pop;
	}
}
