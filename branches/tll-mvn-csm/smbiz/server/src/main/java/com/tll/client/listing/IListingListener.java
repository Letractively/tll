/**
 * The Logic Lab
 * @author jpk
 * Aug 30, 2007
 */
package com.tll.client.listing;

import java.util.EventListener;


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
