/**
 * The Logic Lab
 * @author jpk Jan 27, 2008
 */
package com.tll.client.event;

import java.util.ArrayList;

import com.tll.client.event.type.FieldBindingEvent;

/**
 * ISourcesFieldBindingEvents
 * @author jpk
 */
public interface ISourcesFieldBindingEvents {

	/**
	 * Adds a listener to receive field binding related events.
	 * @param listener the listener to add
	 */
	void addFieldBindingEventListener(IFieldBindingListener listener);

	/**
	 * Removes a previously added listener.
	 * @param listener the listener to remove
	 */
	void removeFieldBindingEventListener(IFieldBindingListener listener);

	/**
	 * FieldBindingEventListenerCollection
	 * @author jpk
	 */
	public static class FieldBindingEventListenerCollection extends ArrayList<IFieldBindingListener> {

		public void fireOnFieldBindingEvent(FieldBindingEvent event) {
			for(IFieldBindingListener listener : this) {
				listener.onFieldBindingEvent(event);
			}
		}

	}
}