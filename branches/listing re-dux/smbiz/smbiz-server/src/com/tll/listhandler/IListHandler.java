package com.tll.listhandler;

import java.util.List;

/**
 * IListHandler - Manages large lists of varying types. This interface defines a
 * common contract for managing lists by providing "paged" access. <br>
 * {@link IListHandler}s hold state and are <em>not</em> designed to be
 * thread-safe! Therefore, clients must ensure safe access to them.
 * @param <T> The list element type.
 * @author jpk
 */
public interface IListHandler<T> {

	/**
	 * @return The {@link ListHandlerType}. This is the implementation type.
	 */
	ListHandlerType getListHandlerType();

	/**
	 * The workhorse method of list handlers. The method must be called first
	 * before list elements may be accessed.
	 * @param offset 0-based index of the underlying list at which fetching
	 *        starts.
	 * @param pageSize The number of list elements to fetch
	 * @param sorting The optional sorting directive.
	 * @return The result containing the "page" elements and the total size of the
	 *         list.
	 * @throws IndexOutOfBoundsException The the offset exceeds the size of the
	 *         list or the number of the elements to fetch results in an out of
	 *         bounds condition.
	 * @throws EmptyListException When the list is empty.
	 * @throws ListHandlerException When the sorting directive gives rise to an
	 *         error.
	 */
	List<T> getElements(int offset, int pageSize, Sorting sorting) throws IndexOutOfBoundsException, EmptyListException,
			ListHandlerException;

	/**
	 * @return The total number of elements in the underlying list or
	 *         <code>0<code> if no fetching has yet occurred.
	 */
	int size();
}
