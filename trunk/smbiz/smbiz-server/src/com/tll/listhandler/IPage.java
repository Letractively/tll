package com.tll.listhandler;

import java.util.List;

/**
 * Abstraction representing a sub-set of elements of a list referred to as a
 * page. A page knows where it is positioned within the list and the size of the
 * list.
 * <p>
 * <b>IMPT:</b> page numbering is 0-based.
 * @author jpk
 */
public interface IPage<T> {

	/**
	 * The page size is constant throughout the lifecycle of the page and
	 * represents the maximum number of elements for a single page.
	 * @return the page size property.
	 */
	int getPageSize();

	/**
	 * @return The number of total pages.
	 */
	int getNumPages();

	/**
	 * @return the list of elements of this page.
	 */
	List<T> getPageElements();

	/**
	 * @return the number or elements on this page.
	 */
	int getNumPageElements();

	/**
	 * @return the 0-based page number
	 */
	int getPageNumber();

	/**
	 * @return the total number of elements in the underlying collection.
	 */
	int getTotalSize();

	/**
	 * @return The 0-based index number relative to the underlying list of the
	 *         first element of this page.
	 */
	int getFirstIndex();

	/**
	 * @return The 0-based index number relative to the underlying collection of
	 *         the last element of this page.
	 */
	int getLastIndex();

	/**
	 * @return true if this page is the first.
	 */
	boolean isFirstPage();

	/**
	 * @return true if this page is the last.
	 */
	boolean isLastPage();
}