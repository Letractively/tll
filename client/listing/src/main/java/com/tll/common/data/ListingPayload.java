/**
 * The Logic Lab
 * @author jpk
 * Aug 31, 2007
 */
package com.tll.common.data;

import com.tll.IMarshalable;
import com.tll.dao.Sorting;

/**
 * ListingPayload - Response to a {@link ListingRequest}.
 * @param <R> The row data type
 * @author jpk
 */
public class ListingPayload<R extends IMarshalable> extends Payload {

	public enum ListingStatus {
		/**
		 * listing is cached on the server.
		 */
		CACHED,
		/**
		 * listing is not cached on the server.
		 */
		NOT_CACHED;
	}

	/**
	 * The unique listing id.
	 */
	private String listingId;

	/**
	 * The server side status of the listing
	 */
	private ListingStatus listingStatus;

	private int listSize;

	private R[] pageElements;

	private int offset = -1;

	private Sorting sorting;

	/**
	 * Constructor
	 */
	public ListingPayload() {
		super();
	}

	/**
	 * Constructor
	 * @param status
	 * @param listingId
	 * @param listingStatus
	 */
	public ListingPayload(Status status, String listingId, ListingStatus listingStatus) {
		super(status);
		this.listingId = listingId;
		this.listingStatus = listingStatus;
	}

	/**
	 * Sets page data.
	 * @param listSize
	 * @param pageElements
	 * @param offset
	 * @param sorting
	 */
	public void setPageData(int listSize, R[] pageElements, int offset, Sorting sorting) {
		this.listSize = listSize;
		this.pageElements = pageElements;
		this.offset = offset;
		this.sorting = sorting;
	}

	public String getListingId() {
		return listingId;
	}

	public ListingStatus getListingStatus() {
		return listingStatus;
	}

	public int getListSize() {
		return listSize;
	}

	public R[] getPageElements() {
		return pageElements;
	}

	public int getOffset() {
		return offset;
	}

	public Sorting getSorting() {
		return sorting;
	}

}
