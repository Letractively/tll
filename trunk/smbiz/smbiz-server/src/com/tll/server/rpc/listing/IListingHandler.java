/*
 * Created on - Oct 21, 2005 Coded by - 'The Logic Lab' - jpk Copywright - 2005 -
 * All rights reserved.
 */

package com.tll.server.rpc.listing;

import com.tll.client.model.IData;
import com.tll.listhandler.EmptyListException;
import com.tll.listhandler.IPage;
import com.tll.listhandler.ListHandlerException;
import com.tll.listhandler.Sorting;

/**
 * Manages {@link IPage} instances and retrieval of their row data.
 * @author jpk
 */
public interface IListingHandler {

	/**
	 * @return The name of the listing this handler handles.
	 */
	String getListingName();

	/**
	 * @return The current 0-based page number.
	 */
	int getPageNumber();

	/**
	 * @return The calculated number of pages.
	 */
	int getNumPages();

	/**
	 * Updates the contained listing with rows assoc. with the given page number.
	 * @param pageNum 0-based page number
	 * @param adjustPageNum Attempt to adjust the page number if the one given is
	 *        out of bounds?
	 * @return The actual page number that was set
	 * @throws EmptyListException
	 * @throws PageNumOutOfBoundsException
	 * @throws ListingException
	 */
	int setCurrentPage(int pageNum, boolean adjustPageNum) throws EmptyListException, PageNumOutOfBoundsException,
			ListingException;

	/**
	 * Sorts the listing.
	 * @param sorting
	 * @throws ListHandlerException
	 */
	void sort(Sorting sorting) throws ListHandlerException;

	/**
	 * Generates a marshallable page suitable for transport to the UI layer.
	 * @return IPage instance
	 */
	IPage<? extends IData> getPage();

	/**
	 * @return the current state of this handler.
	 */
	ListingState getState();
}
