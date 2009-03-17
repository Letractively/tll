/**
 * The Logic Lab
 * @author jpk
 * @since Mar 13, 2009
 */
package com.tll.client.listing;

import com.google.gwt.user.client.ui.Widget;
import com.tll.dao.Sorting;

/**
 * AbstractListingOperator
 * @author jpk
 * @param <R> the row element type
 */
public abstract class AbstractListingOperator<R> implements IListingOperator<R> {

	/**
	 * The Widget that will be passed in dispatched {@link ListingEvent}s.
	 */
	protected Widget sourcingWidget;

	/**
	 * The current list index offset.
	 */
	protected int offset = 0;

	/**
	 * The current sorting directive.
	 */
	protected Sorting sorting;

	/**
	 * The current list size.
	 */
	protected int listSize = -1;

	/**
	 * Constructor
	 */
	public AbstractListingOperator() {
	}

	/**
	 * Sets the source of rpc events
	 * @param sourcingWidget the optional widget that will serve as the rpc event
	 *        source
	 */
	public final void setSourcingWidget(Widget sourcingWidget) {
		this.sourcingWidget = sourcingWidget;
	}

	private void fetch(int offset, Sorting sorting) {
		doFetch(offset, sorting);
		listingGenerated = true;
	}

	/**
	 * Responsible for fetching the data.
	 * @param offset
	 * @param sorting
	 */
	protected abstract void doFetch(int offset, Sorting sorting);

	protected abstract int getPageSize();

	protected boolean listingGenerated;

	public void display() {
		fetch(offset, sorting);
	}

	public void sort(Sorting sorting) {
		if(!listingGenerated || (this.sorting != null && !this.sorting.equals(sorting))) {
			fetch(offset, sorting);
		}
	}

	public void firstPage() {
		if(!listingGenerated || offset != 0) fetch(0, sorting);
	}

	public void gotoPage(int pageNum) {
		final int offset = PagingUtil.listIndexFromPageNum(pageNum, getPageSize());
		if(!listingGenerated || this.offset != offset) fetch(offset, sorting);
	}

	public void lastPage() {
		final int pageSize = getPageSize();
		final int numPages = PagingUtil.numPages(listSize, pageSize);
		final int offset = PagingUtil.listIndexFromPageNum(numPages - 1, pageSize);
		if(!listingGenerated || this.offset != offset) {
			fetch(offset, sorting);
		}
	}

	public void nextPage() {
		final int offset = this.offset + getPageSize();
		if(offset < listSize) fetch(offset, sorting);
	}

	public void previousPage() {
		final int offset = this.offset - getPageSize();
		if(offset >= 0) fetch(offset, sorting);
	}
}
