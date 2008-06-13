/**
 * The Logic Lab
 * @author jpk Jan 5, 2008
 */
package com.tll.client.event;

import java.util.ArrayList;
import java.util.EventListener;

import com.tll.client.event.type.ListingEvent;

/**
 * ISourcesListingEvents - Propagates listing related events.
 * @see IListingListener
 * @author jpk
 */
public interface ISourcesListingEvents extends EventListener {

	/**
	 * Adds a listener to receive edit related events.
	 * @param listener The listener to add.
	 */
	void addListingListener(IListingListener listener);

	/**
	 * Removes a previously added listener.
	 * @param listener The listener to remove.
	 */
	void removeListingListener(IListingListener listener);

	/**
	 * ListingListenerCollection
	 * @author jpk
	 */
	public static final class ListingListenerCollection extends ArrayList<IListingListener> {

		public void fireListingEvent(ListingEvent event) {
			for(IListingListener listener : this) {
				listener.onListingEvent(event);
			}
		}
	}
}
