/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * ViewChangedEvent
 * @author jpk
 */
@SuppressWarnings("serial")
public final class ViewChangedEvent extends ViewEvent {

	/**
	 * Constructor
	 * @param source
	 */
	public ViewChangedEvent(Widget source) {
		super(source);
	}
}
