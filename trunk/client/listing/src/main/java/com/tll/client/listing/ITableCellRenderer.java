/**
 * The Logic Lab
 * @author jpk Jan 19, 2008
 */
package com.tll.client.listing;

/**
 * ITableCellRenderer - Responsible for transforming row data to presentation
 * ready columned text.
 * @author jpk
 * @param <R> The row data type
 * @param <C> the column type
 */
public interface ITableCellRenderer<R, C extends Column> {

	/**
	 * Provides the table cell value given the row data and particular column.
	 * @param rowData The row data.
	 * @param column The table column
	 * @return The table cell value
	 */
	String getCellValue(R rowData, C column);
}
