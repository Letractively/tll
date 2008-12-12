/**
 * The Logic Lab
 * @author jpk Jan 5, 2008
 */
package com.tll.client.event;

import java.util.ArrayList;
import java.util.EventListener;

import com.tll.client.event.type.ListingEvent;
import com.tll.client.model.IData;

/**
 * ISourcesListingEvents - Propagates listing related events.
 * @see IListingListener
 * @author jpk
 */
public interface ISourcesListingEvents<R extends IData> extends EventListener {

	/**
	 * Adds a listener to receive edit related events.
	 * @param listener The listener to add.
	 */
	void addListingListener(IListingListener<R> listener);

	/**
	 * Removes a previously added listener.
	 * @param listener The listener to remove.
	 */
	void removeListingListener(IListingListener<R> listener);

	/**
	 * ListingListenerCollection
	 * @author jpk
	 */
	public static final class ListingListenerCollection<R extends IData> extends ArrayList<IListingListener<R>> {

		public void fireListingEvent(ListingEvent<R> event) {
			for(IListingListener<R> listener : this) {
				listener.onListingEvent(event);
			}
		}
	}
}
