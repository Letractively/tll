/*
 * Created on - Oct 21, 2005 Coded by - 'The Logic Lab' - jpk Copywright - 2005 -
 * All rights reserved.
 */

package com.tll.server.rpc.listing;

import java.util.List;

import com.tll.listhandler.EmptyListException;
import com.tll.listhandler.Sorting;

/**
 * IListingHandler - Manages server-side listing life-cycles.
 * @param <T> The list element type.
 * @author jpk
 */
public interface IListingHandler<T> {

	/**
	 * @return The name of the listing this handler handles.
	 */
	String getListingName();

	/**
	 * @return The page size.
	 */
	int getPageSize();

	/**
	 * @return The total size of the underlying list.
	 */
	int size();

	/**
	 * @return The current 0-based list index.
	 */
	int getOffset();

	/**
	 * @return The sorting directive.
	 */
	Sorting getSorting();

	/**
	 * @return The current list elements.
	 */
	List<T> getElements();

	/**
	 * Fetches list elements.
	 * @param offset The list index at which retriving begins
	 * @param sorting The sorting directive
	 * @param force Force a re-query even if the currently held listing data
	 *        matches the given offset and sorting.
	 * @throws EmptyListException When no matching list elements are found
	 * @throws IndexOutOfBoundsException When the offset exceeds the bounds of the
	 *         underlying list
	 * @throws ListingException When an unexpected listing related error occurrs
	 */
	void query(int offset, Sorting sorting, boolean force) throws EmptyListException, IndexOutOfBoundsException,
			ListingException;
}
