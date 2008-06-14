/*
 * Created on - Oct 21, 2005 Coded by - 'The Logic Lab' - jpk Copywright - 2005 -
 * All rights reserved.
 */

package com.tll.server.rpc.listing;

import java.util.List;

import com.tll.listhandler.EmptyListException;
import com.tll.listhandler.IPage;
import com.tll.listhandler.Sorting;

/**
 * Manages {@link IPage} instances and retrieval of their row data.
 * @author jpk
 */
public interface IListingHandler<T> {

	/**
	 * @return The name of the listing this handler handles.
	 */
	String getListingName();

	/**
	 * @return The current 0-based list index.
	 */
	int getOffset();

	/**
	 * @return The page size.
	 */
	int getPageSize();

	/**
	 * @return The sorting directive.
	 */
	Sorting getSorting();

	/**
	 * @return The total size of the underlying list.
	 */
	int size();

	/**
	 * @return The current list elements.
	 */
	List<T> getElements();

	/**
	 * Fetches list elements.
	 * @param offset The list index at which retriving begins
	 * @param pageSize The number of elements to retrieve
	 * @param sorting The optional sorting directive
	 * @throws EmptyListException
	 * @throws IndexOutOfBoundsException
	 * @throws ListingException
	 */
	void query(int offset, int pageSize, Sorting sorting) throws EmptyListException, IndexOutOfBoundsException,
			ListingException;
}
