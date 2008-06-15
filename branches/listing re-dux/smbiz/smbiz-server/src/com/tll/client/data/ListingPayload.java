/**
 * The Logic Lab
 * @author jpk
 * Aug 31, 2007
 */
package com.tll.client.data;

import java.util.List;

import com.tll.client.model.IData;
import com.tll.listhandler.Sorting;

/**
 * ListingPayload - Response to a {@link ListingRequest}.
 * @author jpk
 */
public class ListingPayload<R extends IData> extends Payload {

	public enum ListingStatus {
		/**
		 * Identified listing is cached on the server
		 */
		CACHED,
		/**
		 * No listing is currently cached
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

	private List<R> pageElements;

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
	public void setPageData(int listSize, List<R> pageElements, int offset, Sorting sorting) {
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

	public List<R> getPageElements() {
		return pageElements;
	}

	public int getOffset() {
		return offset;
	}

	public Sorting getSorting() {
		return sorting;
	}

}
