/**
 * The Logic Lab
 * @author jpk
 * Apr 21, 2008
 */
package com.tll.client.listing;

import com.tll.client.model.IData;
import com.tll.listhandler.Sorting;

/**
 * IListingConfig - The listing configuration definition encompassing
 * non-runtime listing attributes.
 * @param <R> The listing row data type
 * @author jpk
 */
public interface IListingConfig<R extends IData> {

	public static final int DEFAULT_PAGE_SIZE = 25;

	public static final ModelDataCellRenderer MODEL_DATA_CELL_RENDERER = new ModelDataCellRenderer();

	/**
	 * @return A unique name to assign to the listing. Critical for server-side
	 *         listing handling as listing data is cached.
	 */
	String getListingName();

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
	 * Shall the listing be pageable?
	 * @return true/false
	 */
	boolean isPageable();

	/**
	 * @return The page size or <code>-1</code> for no paging.
	 */
	int getPageSize();

	/**
	 * @return <code>true</code> if we want a sortable listing
	 */
	boolean isSortable();

	/**
	 * @return The default sorting which is relevant only when
	 *         {@link #isSortable()} returns <code>true</code>.
	 */
	Sorting getDefaultSorting();

	/**
	 * @return The table cell transformer responsible for rendering cell values
	 *         from the backing listing data.
	 */
	ITableCellRenderer<R> getCellRenderer();

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
	 * Show the add row button?
	 * @return true/false
	 */
	boolean isShowAddBtn();
}