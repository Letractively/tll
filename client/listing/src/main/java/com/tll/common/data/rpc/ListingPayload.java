/**
 * The Logic Lab
 * @author jpk
 * Aug 31, 2007
 */
package com.tll.common.data.rpc;

import com.tll.IMarshalable;
import com.tll.common.data.Page;
import com.tll.common.data.Payload;
import com.tll.common.msg.Status;

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

	private Page<R> page;

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
	 * @param page
	 */
	public void setPageData(Page<R> page) {
		this.page = page;
	}

	public String getListingId() {
		return listingId;
	}

	public ListingStatus getListingStatus() {
		return listingStatus;
	}

	public Page<R> getPage() {
		return page;
	}
}
