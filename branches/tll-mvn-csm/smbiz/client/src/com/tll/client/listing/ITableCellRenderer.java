/**
 * The Logic Lab
 * @author jpk Jan 19, 2008
 */
package com.tll.client.listing;


/**
 * ITableCellRenderer - Responsible for transforming row data to presentation
 * ready columned text.
 * @param <R> The row data type
 * @author jpk
 */
public interface ITableCellRenderer<R> {

	/**
	 * Provides the table cell value given the row data and particular column.
	 * @param rowData The row data.
	 * @param column The table column
	 * @return The table cell value
	 */
	String getCellValue(R rowData, Column column);
}
