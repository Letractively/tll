/**
 * The Logic Lab
 * @author jpk
 * Sep 4, 2007
 */
package com.tll.listhandler;

/**
 * PageUtil - Common paging utility methods.
 * <p>
 * <b>NOTE:</b> All indexes are 0-based.
 * @author jpk
 */
public abstract class PageUtil {

	/**
	 * Is the index valid over the total list?
	 * @param index the list index
	 * @param listSize to list size
	 * @return boolean
	 */
	public static final boolean isValidListIndex(int index, int listSize) {
		return index >= 0 && index <= listSize;
		// NOTE the upper bound check is LTEQ
		// to account for the fact that the upper bound is exclusive
	}

	/**
	 * @param start starting page index
	 * @param end ending page index
	 * @param listSize the list size
	 * @param pageSize the page size
	 * @return boolean
	 */
	public static final boolean isValidPageIndexRange(int start, int end, int listSize, int pageSize) {
		return isValidListIndex(start, listSize) && isValidListIndex(end, listSize) && (end >= start)
				&& ((end - start) <= pageSize);
	}

	/**
	 * Calculate the number of pages.
	 * @param pageSize the page size.
	 * @param listSize the list size.
	 * @return number of pages
	 */
	public static final int calculateNumPages(int pageSize, int listSize) {
		return (listSize % pageSize == 0) ? (int) (listSize / pageSize) : Math.round(listSize / pageSize + 0.5f);
	}

	/**
	 * Calculates the 0-based page number from the given list index.
	 * @param listIndex the 0-based list index.
	 * @param listSize list the list size.
	 * @param pageSize max number or elements per page.
	 * @return the 0-based page number.
	 */
	public static final int getPageNumberFromListIndex(int listIndex, int listSize, int pageSize) {
		if(!isValidListIndex(listIndex, listSize)) {
			throw new IndexOutOfBoundsException("Index out of bounds: " + listIndex);
		}
		return Math.round(listIndex / pageSize + 0.5f) - 1;
	}

	/**
	 * Calculates the "local" page index from the give list index.
	 * @param listIndex the list index.
	 * @param listSize the list size.
	 * @param pageSize the page size.
	 * @return the "local" page index.
	 */
	public static final int getPageIndexFromListIndex(int listIndex, int listSize, int pageSize) {
		if(!isValidListIndex(listIndex, listSize)) {
			throw new IndexOutOfBoundsException("Index out of bounds: " + listIndex);
		}
		return listIndex % pageSize;
	}

}
