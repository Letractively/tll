/**
 * The Logic Lab
 * @author jpk
 * Sep 14, 2007
 */
package com.tll.client.event;

import java.util.EventListener;

import com.tll.client.event.type.FieldBindingEvent;

/**
 * IFieldBindingListener - Event listener for {@link IFieldBindingListener}
 * events.
 * @author jpk
 */
public interface IFieldBindingListener extends EventListener {

	/**
	 * Fired when a field binding related event occurs.
	 * @param event The event
	 */
	void onFieldBindingEvent(FieldBindingEvent event);
}
