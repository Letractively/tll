package com.tll.listhandler;

import java.util.List;

/**
 * The list handler interface.
 * @author jpk
 */
public interface IListHandler<T> extends Iterable<T> {

	/**
	 * @return The {@link ListHandlerType}
	 */
	ListHandlerType getListHandlerType();

	/**
	 * @return the number of list elements
	 */
	int size();

	/**
	 * @return true when one or more list elements exist, false otherwise
	 */
	boolean hasElements();

	/**
	 * Returns the element at the given index.
	 * @param index
	 * @throws EmptyListException when no list elements exist
	 * @throws ListHandlerException upon processing error or when the given index
	 *         is out of bounds.
	 */
	T getElement(int index) throws EmptyListException, ListHandlerException;

	/**
	 * @param start starting 0-based index inclusive
	 * @param end ending 0-based index EXCLUSIVE
	 * @return list of 0 or more elements
	 * @throws EmptyListException when no list elements exist
	 * @throws ListHandlerException upon processing error or when start and/or end
	 *         are out of bounds
	 */
	List<T> getElements(int start, int end) throws EmptyListException, ListHandlerException;

	/**
	 * @return the {@link Sorting} used by this list handler.
	 */
	Sorting getSorting();

	/**
	 * Sorts the list elements. <br>
	 * IMPT some implementations of this method may <b>alter</b> the results as a
	 * result of re-querying the data store, so clients need to take this into
	 * account.
	 * @param sorting sort directives
	 * @throws ListHandlerException when sort directives are mal-formed or when
	 *         there are none
	 */
	void sort(Sorting sorting) throws ListHandlerException;
}
