package com.tll.server.rpc.listing;

/**
 * PageNumOutOfBoundsException
 * @author jpk
 */
public class PageNumOutOfBoundsException extends ListingException {

	private static final long serialVersionUID = 4883104934900269600L;

	private final int requestedPageNum, actualNumPages;

	public PageNumOutOfBoundsException(String listingName, int requestedPageNum, int actualNumPages) {
		super(listingName, "Page number: " + requestedPageNum + " is out of bounds");
		this.requestedPageNum = requestedPageNum;
		this.actualNumPages = actualNumPages;
	}

	public int getRequestedPageNum() {
		return requestedPageNum;
	}

	public int getActualNumPages() {
		return actualNumPages;
	}
}
