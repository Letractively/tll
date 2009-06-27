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
import com.tll.common.model.ModelKey;

/**
 * ModelListingTable - A table whose rows are identifiable by a key.
 * @author jpk
 */
public final class ModelListingTable extends ListingTable<Model> {

	/**
	 * {@link ModelKey}s for each listing element row.
	 */
	protected final List<ModelKey> rowKeys = new ArrayList<ModelKey>();

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
	 * @return ModelKey
	 */
	ModelKey getRowKey(int row) {
		return rowKeys.get(row - 1);
	}

	/**
	 * Get the row index given a {@link ModelKey}.
	 * @param rowKey The row key for which to find the associated row index.
	 * @return The resolved row index or <code>-1</code> if no row matching the
	 *         given row key is present in the table.
	 */
	int getRowIndex(ModelKey rowKey) {
		final int i = rowKeys.indexOf(rowKey);
		return i == -1 ? -1 : i + 1; // account for header row
	}

	@Override
	protected void setRowData(int rowIndex, int rowNum, Model rowData, boolean overwriteOnNull) {
		super.setRowData(rowIndex, rowNum, rowData, overwriteOnNull);
		rowKeys.add(rowData.getKey());
	}

	@Override
	int addRow(Model rowData) {
		rowKeys.add(rowData.getKey());
		return super.addRow(rowData);
	}

	@Override
	void updateRow(int rowIndex, Model rowData) {
		rowKeys.set(rowIndex - 1, rowData.getKey());
		super.updateRow(rowIndex, rowData);
	}

	@Override
	void deleteRow(int rowIndex) {
		rowKeys.remove(rowIndex - 1);
		super.deleteRow(rowIndex);
	}
}
