/**
 * The Logic Lab
 * @author jpk
 * Jan 26, 2008
 */
package com.tll.client.mvc.view;

import java.util.EventObject;

import com.google.gwt.user.client.ui.Widget;

/**
 * ViewEvent - Base class for all view related events in the system
 * @author jpk
 */
@SuppressWarnings("serial")
public abstract class ViewEvent extends EventObject {

	/**
	 * Constructor
	 * @param source
	 */
	public ViewEvent(Widget source) {
		super(source);
	}

}
