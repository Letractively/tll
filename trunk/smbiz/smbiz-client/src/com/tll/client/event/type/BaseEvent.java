/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.event.type;

import java.util.EventObject;

import com.google.gwt.user.client.ui.Widget;

/**
 * BaseEvent - Abstract class for all defined events in the app.
 * @author jpk
 */
public abstract class BaseEvent extends EventObject {

	/**
	 * Constructor
	 * @param source The object that sourced the event
	 */
	public BaseEvent(Widget source) {
		super(source);
	}

	public final Widget getWidget() {
		return (Widget) getSource();
	}

	@Override
	public String toString() {
		// return "Event: " + GWT.getTypeName(this) + " source: " + GWT.getTypeName(getSource());
		return "Event: " + getClass().getName() + " source: " + getClass().getName();
	}
}
