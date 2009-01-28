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

	public static final ModelCellRenderer MODEL_CELL_RENDERER = new ModelCellRenderer();

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

	/**
	 * Optional add row handler. This is usually used in conjunction w/ the
	 * oprional add button in the listing's nav bar. If this property is set and
	 * intended for use, make sure the {@link #isShowNavBar()} is set to
	 * <code>true</code>.
	 * @return The optional add row handler.
	 */
	IAddRowDelegate getAddRowHandler();

	/**
	 * Optional row options provider and handler. If set, a contextual poupup with
	 * vertically laid out options will appear when a listing row is clicked. This
	 * handler is delegated to for handling option selection.
	 * @return The optional row context handler.
	 */
	IRowOptionsDelegate getRowOptionsHandler();
}