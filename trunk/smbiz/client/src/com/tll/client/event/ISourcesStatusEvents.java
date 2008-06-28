/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.event;

import java.util.ArrayList;

import com.tll.client.event.type.StatusEvent;

/**
 * ISourcesStatusEvents
 * @author jpk
 */
public interface ISourcesStatusEvents {

	/**
	 * Adds a listener.
	 * @param listener
	 */
	void addStatusListener(IStatusListener listener);

	/**
	 * Removes a listener.
	 * @param listener
	 */
	void removeStatusListener(IStatusListener listener);

	/**
	 * StatusListenerCollection
	 * @author jpk
	 */
	public static class StatusListenerCollection extends ArrayList<IStatusListener> {

		public void fireStatusEvent(StatusEvent event) {
			for(IStatusListener listener : this) {
				listener.onStatusEvent(event);
			}
		}

	}
}
