package com.tll.server.rpc.listing;

import com.tll.listhandler.ListHandlerException;

/**
 * ListingException
 * @author jpk
 */
public class ListingException extends ListHandlerException {

	static final long serialVersionUID = -1116645045511338623L;

	private final String listingName;

	/**
	 * Constructor
	 * @param listingName
	 * @param message
	 */
	public ListingException(final String listingName, final String message) {
		this(listingName, message, null);
	}

	/**
	 * Constructor
	 * @param listingName
	 * @param message
	 * @param t
	 */
	public ListingException(final String listingName, final String message, final Throwable t) {
		super(message, t);
		this.listingName = listingName;
	}

	public String getListingName() {
		return listingName;
	}
}
