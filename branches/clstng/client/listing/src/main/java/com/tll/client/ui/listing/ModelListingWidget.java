/**
 * The Logic Lab
 * @author jpk
 * Jun 13, 2008
 */
package com.tll.client.ui.listing;

import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IListingOperator;
import com.tll.client.model.IModelChangeListener;
import com.tll.client.model.ModelChangeEvent;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;

/**
 * ModelListingWidget - Listing Widget dedicated to handling Model type data.
 * @author jpk
 * @param <O> the listing operator type
 */
public class ModelListingWidget<O extends IListingOperator<Model>> extends ListingWidget<Model, ModelListingTable, O>
		implements IModelChangeListener {

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
	 * @return ModelKey
	 */
	public ModelKey getRowKey(int row) {
		return table.getRowKey(row);
	}

	/**
	 * Get the row index given a {@link ModelKey}.
	 * @param rowKey The ModelKey for which to find the associated row index.
	 * @return The row index or <code>-1</code> if no row matching the given ref
	 *         key is present in the table.
	 */
	public int getRowIndex(ModelKey rowKey) {
		return table.getRowIndex(rowKey);
	}

	/**
	 * Handles successful model change events.
	 * <p>
	 * NOTE: This method is <em>not</em> automatically invoked.
	 * @param event
	 */
	public void onModelChangeEvent(ModelChangeEvent event) {
		switch(event.getChangeOp()) {
			case ADDED:
				addRow(event.getModel());
				break;
			case UPDATED: {
				final ModelKey modelRef = event.getModel().getRefKey();
				final int rowIndex = getRowIndex(modelRef);
				if(rowIndex != -1) {
					assert rowIndex > 0; // header row
					// TODO determine how to handle named query specific model data!!
					updateRow(rowIndex, event.getModel());
				}
				break;
			}
			case DELETED: {
				final ModelKey modelRef = event.getModelRef();
				final int rowIndex = getRowIndex(modelRef);
				if(rowIndex != -1) {
					assert rowIndex > 0; // header row
					markRowDeleted(rowIndex, true);
				}
				break;
			}
		}
	}
}
