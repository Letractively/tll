/**
 * The Logic Lab
 * @author jpk
 * Mar 30, 2008
 */
package com.tll.client.listing;

import com.tll.client.data.ListingOp;
import com.tll.client.event.ISourcesListingEvents;
import com.tll.listhandler.SortColumn;

/**
 * IListingOperator - Performs listing ops on a particular listing. This
 * interface enables a pluggable (generic) way to provide listing data.
 * @author jpk
 */
public interface IListingOperator extends ISourcesListingEvents {

	/**
	 * Acquires or re-acquires the listing data resetting the listing state then
	 * dispatches the a listing event to the listing Widget.
	 */
	void refresh();

	/**
	 * Dispatches a listing event containing cached listing data and state. If no
	 * listing data exists, the listing data is acquired as {@link #refresh()}
	 * would. A corresponding listing event is then dispatched to the listing
	 * Widget.
	 */
	void display();

	/**
	 * Sorts the listing. A listing event is then dispatched to the listing
	 * Widget.
	 * @param sortColumn
	 */
	void sort(SortColumn sortColumn);

	/**
	 * Navigates the listing. A listing event is then dispatched to the listing
	 * Widget and any other listeners.
	 * @param navAction
	 * @param page The 0-based page number
	 */
	void navigate(ListingOp navAction, Integer page);

	/**
	 * Relinquishes any inter-request listing cache and state associated with the
	 * listing this operator operates on. A listing event is then dispatched to
	 * the listing Widget.
	 */
	void clear();
}