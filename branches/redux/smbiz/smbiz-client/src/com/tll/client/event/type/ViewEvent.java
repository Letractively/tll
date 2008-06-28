/**
 * The Logic Lab
 * @author jpk
 * Jan 26, 2008
 */
package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;

/**
 * ViewEvent - Base class for all view related events in the system
 * @author jpk
 */
public abstract class ViewEvent extends BaseEvent {

	/**
	 * Constructor
	 * @param source
	 */
	public ViewEvent(Widget source) {
		super(source);
	}

}
