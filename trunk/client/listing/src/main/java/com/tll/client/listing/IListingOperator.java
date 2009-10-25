/**
 * The Logic Lab
 * @author jpk
 * Mar 30, 2008
 */
package com.tll.client.listing;

import com.google.gwt.user.client.ui.Widget;
import com.tll.dao.Sorting;

/**
 * IListingOperator - Performs listing ops on a particular listing. This
 * interface enables a pluggable (generic) way to provide listing data.
 * @author jpk
 * @param <R> the row type
 */
public interface IListingOperator<R> {
	
	/**
	 * Sets the widget that will source listing/rpc events.
	 * @param widget
	 */
	void setSourcingWidget(Widget widget);

	/**
	 * Acquires or re-acquires the listing data resetting the listing state then
	 * dispatches a listing event to the listing Widget.
	 */
	void refresh();

	/**
	 * Dispatches a listing event containing cached listing data and state. If no
	 * listing data exists, the listing data is acquired as {@link #refresh()}
	 * would.
	 */
	void display();

	/**
	 * Sorts the listing. A listing event is then dispatched to the listing
	 * Widget.
	 * @param sorting The sorting directive
	 */
	void sort(Sorting sorting);

	/**
	 * Goto the first page in the listing.
	 */
	void firstPage();

	/**
	 * Goto the last page in the listing.
	 */
	void lastPage();

	/**
	 * Goto the next page in the listing.
	 */
	void nextPage();

	/**
	 * Goto the previous page in the listing.
	 */
	void previousPage();

	/**
	 * Goto an arbitrary page in the listing.
	 * @param pageNum 0-based page number
	 */
	void gotoPage(int pageNum);

	/**
	 * Relinquishes any inter-request listing cache and state associated with the
	 * listing this operator operates on.
	 */
	void clear();
}