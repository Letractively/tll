/**
 * The Logic Lab
 * @author jpk
 * @since Mar 13, 2009
 */
package com.tll.client.listing;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.tll.common.data.ListingOp;
import com.tll.dao.Sorting;

/**
 * AbstractListingOperator
 * @author jpk
 * @param <R> the row element type
 */
public abstract class AbstractListingOperator<R> implements IListingOperator<R> {

	/**
	 * Calculates the list index from the page number and page size.
	 * @param pageNum 0-based page number
	 * @param pageSize The page size
	 * @return The calculated list index.
	 */
	protected static int listIndexFromPageNum(int pageNum, int pageSize) {
		return pageNum == 0 ? 0 : pageNum * pageSize;
	}

	/**
	 * Calculates the number of pages in a list subject to paging.
	 * @param listSize The list size
	 * @param pageSize The max number of elements per page
	 * @return The calculated number of pages.
	 */
	protected static int numPages(int listSize, int pageSize) {
		return (listSize % pageSize == 0) ? (int) (listSize / (double) pageSize) : (int) Math.round(listSize
				/ (double) pageSize + 0.5d);
	}

	protected Widget target;

	/**
	 * The current list index offset.
	 */
	protected int offset = 0;

	/**
	 * The initial (default) sorting directive.
	 */
	protected final Sorting defaultSorting;

	/**
	 * The current sorting directive.
	 */
	protected Sorting sorting;

	/**
	 * The current list size.
	 */
	protected int listSize = -1;

	protected boolean listingGenerated;

	/**
	 * Constructor
	 * @param target the required widget that will receive listing events.
	 * @param defaultSorting
	 */
	public AbstractListingOperator(Widget target, Sorting defaultSorting) {
		setTarget(target);
		this.defaultSorting = defaultSorting;
	}

	@Override
	public final HandlerRegistration addListingHandler(IListingHandler<R> handler) {
		return target.addHandler(handler, ListingEvent.TYPE);
	}

	@Override
	public final void fireEvent(GwtEvent<?> event) {
		target.fireEvent(event);
	}

	@Override
	public final void setTarget(Widget target) {
		if(target == null) throw new IllegalArgumentException("No target widget specified.");
		this.target = target;
	}

	protected final void fireListingEvent(ListingOp listingOp, List<R> pageElements) {
		fireEvent(new ListingEvent<R>(listingOp, listSize, pageElements, offset, sorting, getPageSize()));
	}

	/**
	 * Responsible for fetching the data.
	 * @param ofst
	 * @param srtg
	 */
	protected abstract void doFetch(int ofst, Sorting srtg);

	protected abstract int getPageSize();

	private void fetch(int ofst, Sorting srtg) {
		assert target != null;
		doFetch(ofst, srtg);
		listingGenerated = true;
	}

	@Override
	public void sort(Sorting srtg) {
		if(!listingGenerated || (this.sorting != null && !this.sorting.equals(srtg))) {
			fetch(offset, srtg);
		}
	}

	@Override
	public void firstPage() {
		if(!listingGenerated || offset != 0) fetch(0, sorting);
	}

	@Override
	public void gotoPage(int pageNum) {
		final int ofst = listIndexFromPageNum(pageNum, getPageSize());
		if(!listingGenerated || this.offset != ofst) fetch(ofst, sorting);
	}

	@Override
	public void lastPage() {
		final int pageSize = getPageSize();
		final int numPages = numPages(listSize, pageSize);
		final int ofst = listIndexFromPageNum(numPages - 1, pageSize);
		if(!listingGenerated || this.offset != ofst) {
			fetch(ofst, sorting);
		}
	}

	@Override
	public void nextPage() {
		final int ofst = this.offset + getPageSize();
		if(ofst < listSize) fetch(ofst, sorting);
	}

	@Override
	public void previousPage() {
		final int ofst = this.offset - getPageSize();
		if(ofst >= 0) fetch(ofst, sorting);
	}
}
