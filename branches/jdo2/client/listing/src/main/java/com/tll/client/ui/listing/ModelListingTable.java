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
	 * The row data comprised of . {@link Model} instances for each listing
	 * element row.
	 */
	protected final List<Model> rowDataList = new ArrayList<Model>();

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
		return getRowData(row).getKey();
	}

	/**
	 * Get the underlying model data for a row.
	 * @param row 0-based table row num (considers the header row).
	 * @return the model row data
	 */
	Model getRowData(int row) {
		return rowDataList.get(row - 1);
	}

	/**
	 * Get the row index given a {@link ModelKey}.
	 * @param rowKey The row key for which to find the associated row index.
	 * @return The resolved row index or <code>-1</code> if no row matching the
	 *         given row key is present in the table.
	 */
	int getRowIndex(ModelKey rowKey) {
		for(int i = 0; i < rowDataList.size(); i++) {
			final ModelKey rdlKey = rowDataList.get(i).getKey();
			if(rdlKey.equals(rowKey)) return i + 1; // account for header row
		}
		// can't find
		return -1;
	}

	@Override
	protected void setRowData(int rowIndex, int rowNum, Model rowData, boolean overwriteOnNull) {
		assert rowIndex >= 1;	// min index is one after the header row!
		super.setRowData(rowIndex, rowNum, rowData, overwriteOnNull);
		final int dindex = rowIndex - 1;
		if(dindex == rowDataList.size()) {
			rowDataList.add(rowData);
		}
		else {
			assert dindex < rowDataList.size();
			rowDataList.set(dindex, rowData);
		}
	}

	@Override
	int addRow(Model rowData) {
		rowDataList.add(rowData);
		return super.addRow(rowData);
	}

	@Override
	void updateRow(int rowIndex, Model rowData) {
		rowDataList.set(rowIndex - 1, rowData);
		super.updateRow(rowIndex, rowData);
	}

	@Override
	void deleteRow(int rowIndex) {
		rowDataList.remove(rowIndex - 1);
		super.deleteRow(rowIndex);
	}

	/**
	 * Applies model data to a given row in the UI only and does not alter the
	 * underlying row data.
	 */
	void applyModeltoUi(int rowIndex, Model mdata) {
		super.setRowData(rowIndex, -1, mdata, true);
		getRowFormatter().addStyleName(rowIndex, Styles.UPDATED);
	}
}
