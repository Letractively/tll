/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.listing;

import com.google.gwt.event.shared.GwtEvent;
import com.tll.common.data.ListingOp;
import com.tll.common.data.ListingPayload.ListingStatus;
import com.tll.dao.Sorting;

/**
 * ListingEvent - Fired when listing data is retrieved or updated for a targeted
 * listing.
 * @param <R> The row data type
 * @author jpk
 */
public final class ListingEvent<R> extends GwtEvent<IListingHandler<R>> {

	public static final Type<IListingHandler<?>> TYPE = new Type<IListingHandler<?>>();

	private final String listingId;
	private final ListingOp listingOp;
	private final ListingStatus listingStatus;
	private final int listSize;
	private final R[] pageElements;
	private final int offset;
	private final Sorting sorting;

	/**
	 * The calculated 0-based page number.
	 */
	private int pageNum;
	/**
	 * The calculated number of pages.
	 */
	private int numPages;

	/**
	 * Constructor
	 * @param listingId
	 * @param listingOp
	 * @param listSize
	 * @param pageElements
	 * @param offset
	 * @param sorting
	 * @param pageSize
	 */
	public ListingEvent(String listingId, ListingOp listingOp, int listSize,
			R[] pageElements, int offset, Sorting sorting, int pageSize) {
		this.listingId = listingId;
		this.listingOp = listingOp;
		this.listingStatus = null;
		this.listSize = listSize;
		this.pageElements = pageElements;
		this.offset = offset;
		this.sorting = sorting;
		setCalculated(pageSize);
	}

	private void setCalculated(int pageSize) {
		pageNum = ((int) Math.round(offset / (double) pageSize + 0.5d)) - 1;
		numPages =
			(listSize % pageSize == 0) ? (int) (listSize / (double) pageSize) : (int) Math
					.round(listSize
						/ (double) pageSize + 0.5d);
	}

	@Override
	protected void dispatch(IListingHandler<R> handler) {
		handler.onListingEvent(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Type<IListingHandler<R>> getAssociatedType() {
		return (Type) TYPE;
	}

	public String getListingId() {
		return listingId;
	}

	public ListingOp getListingOp() {
		return listingOp;
	}

	public ListingStatus getListingStatus() {
		return listingStatus;
	}

	public R[] getPageElements() {
		return pageElements;
	}

	public Integer getListSize() {
		return listSize;
	}

	public Integer getOffset() {
		return offset;
	}

	public Sorting getSorting() {
		return sorting;
	}

	public int getPageNum() {
		return pageNum;
	}

	public int getNumPages() {
		return numPages;
	}

	public boolean isFirstPage() {
		return pageNum == 0;
	}

	public boolean isLastPage() {
		return pageNum == numPages - 1;
	}

	public boolean isNextPage() {
		return pageNum < numPages - 1;
	}

	public boolean isPreviousPage() {
		return pageNum > 0;
	}

	@Override
	public String toString() {
		return getListingId();
	}
}
