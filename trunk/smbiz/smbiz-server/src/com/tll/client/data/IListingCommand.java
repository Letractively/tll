package com.tll.client.data;

import com.tll.client.IMarshalable;
import com.tll.client.search.ISearch;

/**
 * The listing command interface. Used to transfer a client/server request for
 * table data.
 * @author jpk
 */
public interface IListingCommand extends IMarshalable {

	/* in lieu of ListHandlerType enum */
	public static final int LIST_HANDLER_TYPE_COLLECTION = 0;
	public static final int LIST_HANDLER_TYPE_IDLIST = 1;
	public static final int LIST_HANDLER_TYPE_PAGE = 2;

	/**
	 * @return the distinguishing name of the listing. Must be unique against all
	 *         other loaded listings as it is used for caching.
	 */
	String getListingName();

	/**
	 * Paging switch. If <code>false</code>, no paging is applied.
	 * @return true/false
	 */
	boolean isPageable();

	/**
	 * The max number of elements to show per page.
	 * @return int
	 */
	int getPageSize();

	/**
	 * @return The list handling type. Controls how paging is performed on the
	 *         server.
	 */
	int getListHandlerType();

	/**
	 * @return The search criteria for the listing.
	 */
	ISearch getSearchCriteria();

	/**
	 * @return the prop keys array.
	 */
	PropKey[] getPropKeys();

	/**
	 * Required by the server side table model operator.
	 * @return ListingOp
	 */
	ListingOp getListingOp();
}