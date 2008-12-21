/**
 * The Logic Lab
 * @author jpk
 * Aug 30, 2007
 */
package com.tll.client.event;

import java.util.EventListener;

import com.tll.client.event.type.ListingEvent;

/**
 * IListingListener - Listens to listing related events.
 * @param <R> The row data type
 * @author jpk
 */
public interface IListingListener<R> extends EventListener {

	/**
	 * Fired when a listing related RPC call returns to client.
	 * @param event The event
	 */
	void onListingEvent(ListingEvent<R> event);
}
