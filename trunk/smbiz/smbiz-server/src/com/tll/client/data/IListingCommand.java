package com.tll.client.data;

import com.tll.client.IMarshalable;
import com.tll.client.search.ISearch;
import com.tll.listhandler.ListHandlerType;
import com.tll.listhandler.Sorting;
import com.tll.util.IDescriptorProvider;

/**
 * The listing command interface. Used to transfer a client/server request for
 * table data.
 * @author jpk
 */
public interface IListingCommand<S extends ISearch> extends IMarshalable, IDescriptorProvider {

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
	ListHandlerType getListHandlerType();

	/**
	 * @return The search criteria for the listing.
	 */
	S getSearchCriteria();

	/**
	 * @return the prop keys array.
	 */
	String[] getPropKeys();

	/**
	 * Required by the server side table model operator.
	 * @return ListingOp
	 */
	ListingOp getListingOp();

	/**
	 * @return The sorting directive.
	 */
	Sorting getSorting();

	Integer getPageNumber();

	Boolean getRetainStateOnClear();
}