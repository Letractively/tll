/**
 * The Logic Lab
 * @author jpk
 * Jun 13, 2008
 */
package com.tll.client.ui.listing;

import java.util.ArrayList;
import java.util.List;

import com.tll.client.listing.IListingConfig;
import com.tll.common.model.Model;
import com.tll.common.model.RefKey;

/**
 * ModelListingTable
 * @author jpk
 */
public final class ModelListingTable extends ListingTable<Model> {

	/**
	 * {@link RefKey}s for each listing element row.
	 */
	protected final List<RefKey> rowRefs = new ArrayList<RefKey>();

	/**
	 * Constructor
	 * @param config
	 */
	public ModelListingTable(IListingConfig<Model> config) {
		super(config);
	}

	/**
	 * Get the row ref for a given row.
	 * @param row 0-based table row num (considers the header row).
	 * @return RefKey
	 */
	RefKey getRowRef(int row) {
		return rowRefs.get(row - 1);
	}

	/**
	 * Get the row index given a {@link RefKey}.
	 * @param rowRef The RefKey for which to find the associated row index.
	 * @return The row index or <code>-1</code> if no row matching the given ref
	 *         key is present in the table.
	 */
	int getRowIndex(RefKey rowRef) {
		return rowRefs.indexOf(rowRef) + 1; // account for header row
	}

	@Override
	protected void setRowData(int rowIndex, int rowNum, Model rowData, boolean overwriteOnNull) {
		super.setRowData(rowIndex, rowNum, rowData, overwriteOnNull);
		rowRefs.add(rowData.getRefKey());
	}

	@Override
	int addRow(Model rowData) {
		rowRefs.add(rowData.getRefKey());
		return super.addRow(rowData);
	}

	@Override
	void updateRow(int rowIndex, Model rowData) {
		rowRefs.set(rowIndex - 1, rowData.getRefKey());
		super.updateRow(rowIndex, rowData);
	}

	@Override
	void deleteRow(int rowIndex) {
		rowRefs.remove(rowIndex - 1);
		super.deleteRow(rowIndex);
	}
}
