/**
 * The Logic Lab
 * @author jpk
 * Aug 31, 2007
 */
package com.tll.client.data;

import com.tll.client.IMarshalable;
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
	 * The listing name uniquely identifying the listing on the server.
	 */
	private String listingName;

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
	 * @param listingName
	 * @param listingStatus
	 */
	public ListingPayload(String listingName, ListingStatus listingStatus) {
		super();
		this.listingName = listingName;
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

	public String getListingName() {
		return listingName;
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
