/**
 * The Logic Lab
 * @author jpk
 * Jun 12, 2008
 */
package com.tll.client.ui.listing;

import java.util.ArrayList;
import java.util.List;

import com.tll.client.listing.IListingConfig;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;

/**
 * ModelTable
 * @author jpk
 */
public class ModelTable extends ListingTable<Model> {

	/**
	 * Constructor
	 * @param config
	 */
	public ModelTable(IListingConfig config) {
		super(config);
	}

	/**
	 * {@link RefKey}s for each listing element row.
	 */
	protected final List<RefKey> rowRefs = new ArrayList<RefKey>();

	/**
	 * Get the row ref for a given row.
	 * @param row 0-based table row num (considers the header row).
	 * @return RefKey
	 */
	public final RefKey getRowRef(int row) {
		return rowRefs.get(row - 1);
	}

	/**
	 * Get the row index given a {@link RefKey}.
	 * @param rowRef The RefKey for which to find the associated row index.
	 * @return The row index or <code>-1</code> if no row matching the given ref
	 *         key is present in the table.
	 */
	final int getRowIndex(RefKey rowRef) {
		return rowRefs.indexOf(rowRef) + 1; // account for header row
	}

	@Override
	protected void setRowData(int rowIndex, int rowNum, Model rowData, boolean overwriteOnNull) {
		super.setRowData(rowIndex, rowNum, rowData, overwriteOnNull);
		rowRefs.add(rowData.getRefKey());
	}

	@Override
	void deleteRow(int rowIndex) {
		super.deleteRow(rowIndex);
		rowRefs.remove(rowIndex - 1);
	}

	@Override
	protected void removeBodyRows() {
		super.removeBodyRows();
		rowRefs.clear();
	}

}
