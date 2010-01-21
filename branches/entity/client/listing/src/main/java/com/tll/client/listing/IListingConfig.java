/**
 * The Logic Lab
 * @author jpk
 * Apr 21, 2008
 */
package com.tll.client.listing;

import com.tll.dao.Sorting;

/**
 * IListingConfig - The listing configuration definition encompassing
 * non-runtime listing attributes.
 * @param <R> The listing row data type
 * @author jpk
 */
public interface IListingConfig<R> {

	public static final int DEFAULT_PAGE_SIZE = 25;

	/**
	 * @return A unique token that uniquely identifies this listing.
	 */
	String getListingId();

	/**
	 * @return A presentation worthy name for the row type.
	 */
	String getListingElementName();

	/**
	 * Optional table caption name.
	 * @return The table caption name
	 */
	String getCaption();

	/**
	 * @return The columns
	 */
	Column[] getColumns();

	/**
	 * @return Array of (dot notation) property paths that correspond to a single
	 *         row. If <code>null</code>, row model data is NOT filtered on the
	 *         server. Impls should endeavor to constrain row model data instances
	 *         by specifying only the needed properties to fill a row.
	 */
	String[] getModelProperties();

	/**
	 * @return The desired page size or <code>-1</code> for no paging.
	 */
	int getPageSize();

	/**
	 * @return <code>true</code> if we want a sortable listing
	 */
	boolean isSortable();

	/**
	 * Ignore case when sorting?
	 * @return true/false
	 */
	boolean isIgnoreCaseWhenSorting();

	/**
	 * @return The default sorting which is relevant only when
	 *         {@link #isSortable()} returns <code>true</code>.
	 */
	Sorting getDefaultSorting();

	/**
	 * Show the listing nav bar?
	 * @return true/false
	 */
	boolean isShowNavBar();

	/**
	 * Show the refresh button?
	 * @return true/false
	 */
	boolean isShowRefreshBtn();

	/**
	 * @return The table cell transformer responsible for rendering cell values
	 *         from the backing listing data.
	 */
	ITableCellRenderer<R> getCellRenderer();
}