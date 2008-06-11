/**
 * The Logic Lab
 * @author jpk Jan 19, 2008
 */
package com.tll.client.listing;

import com.tll.client.model.Model;

/**
 * ITableCellTransformer - Responsible for transforming row data to presentation
 * ready columned text.
 * @author jpk
 */
public interface ITableCellTransformer {

	/**
	 * Provides a way for the underlying table rows to be set/updated given a
	 * model.
	 * @param rowData The row data.
	 * @param columns The defined {@link Column}s
	 * @return Array of presentation ready table cell value {@link String}s.
	 */
	String[] getCellValues(Model rowData, Column[] columns);
}
