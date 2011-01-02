package com.tll.server.listing;

import com.tll.listhandler.ListHandlerException;

/**
 * ListingException
 * @author jpk
 */
public class ListingException extends ListHandlerException {

	static final long serialVersionUID = -1116645045511338623L;

	private final String listingId;

	/**
	 * Constructor
	 * @param listingId
	 * @param message
	 */
	public ListingException(final String listingId, final String message) {
		this(listingId, message, null);
	}

	/**
	 * Constructor
	 * @param listingId
	 * @param message
	 * @param t
	 */
	public ListingException(final String listingId, final String message, final Throwable t) {
		super(message, t);
		this.listingId = listingId;
	}

	public String getListingId() {
		return listingId;
	}
}
