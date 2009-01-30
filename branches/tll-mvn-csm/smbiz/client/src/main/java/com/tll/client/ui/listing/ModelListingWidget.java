/**
 * The Logic Lab
 * @author jpk
 * Jun 13, 2008
 */
package com.tll.client.ui.listing;

import com.tll.client.listing.IListingConfig;
import com.tll.client.model.Model;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.model.RefKey;

/**
 * ModelListingWidget - Listing Widget dedicated to handling Model type data.
 * @author jpk
 */
public final class ModelListingWidget extends ListingWidget<Model> {

	/**
	 * Constructor
	 * @param config
	 */
	public ModelListingWidget(IListingConfig<Model> config) {
		super(config, new ModelListingTable(config));
	}

	/**
	 * Get the row ref for a given row.
	 * @param row 0-based table row num (considers the header row).
	 * @return RefKey
	 */
	public RefKey getRowRef(int row) {
		return ((ModelListingTable) table).getRowRef(row);
	}

	/**
	 * Get the row index given a {@link RefKey}.
	 * @param rowRef The RefKey for which to find the associated row index.
	 * @return The row index or <code>-1</code> if no row matching the given ref
	 *         key is present in the table.
	 */
	public int getRowIndex(RefKey rowRef) {
		return ((ModelListingTable) table).getRowIndex(rowRef);
	}

	/**
	 * Handles successful model change events.
	 * <p>
	 * NOTE: This method is <em>not</em> automatically invoked.
	 * @param event
	 */
	public void handleModelChange(ModelChangeEvent event) {
		switch(event.getChangeOp()) {
			case ADDED:
				addRow(event.getModel());
				break;
			case UPDATED: {
				RefKey modelRef = event.getModel().getRefKey();
				int rowIndex = getRowIndex(modelRef);
				if(rowIndex != -1) {
					assert rowIndex > 0; // header row
					// TODO determine how to handle named query specific model data!!
					updateRow(rowIndex, event.getModel());
				}
				break;
			}
			case DELETED: {
				RefKey modelRef = event.getModelRef();
				int rowIndex = getRowIndex(modelRef);
				if(rowIndex != -1) {
					assert rowIndex > 0; // header row
					markRowDeleted(rowIndex, true);
				}
				break;
			}
		}
	}
}
