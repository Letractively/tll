/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client;

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
	public BaseEvent(Object source) {
		super(source);
	}

	/**
	 * @return The sourcing Widget which is <code>null</code> when the source is
	 *         not a Widget type.
	 */
	public final Widget getWidget() {
		final Object source = getSource();
		return source instanceof Widget ? (Widget) source : null;
	}

	@Override
	public String toString() {
		// return "Event: " + GWT.getTypeName(this) + " source: " +
		// GWT.getTypeName(getSource());
		return "Event: " + getClass().getName() + " source: " + getClass().getName();
	}
}
