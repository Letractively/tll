/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;

/**
 * ViewChangedEvent
 * @author jpk
 */
public final class ViewChangedEvent extends ViewEvent {

	/**
	 * Constructor
	 * @param source
	 */
	public ViewChangedEvent(Widget source) {
		super(source);
	}
}
