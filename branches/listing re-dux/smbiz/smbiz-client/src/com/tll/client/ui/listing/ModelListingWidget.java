/**
 * The Logic Lab
 * @author jpk
 * Jun 13, 2008
 */
package com.tll.client.ui.listing;

import com.google.gwt.user.client.ui.TableListener;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IListingOperator;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.listhandler.IPage;
import com.tll.listhandler.Sorting;

/**
 * ModelListingWidget - Listing Widget dedicated to handling Model type data.
 * @author jpk
 */
public final class ModelListingWidget extends ListingWidget<Model> {

	private final ModelListingTable table;

	/**
	 * Constructor
	 * @param config
	 * @param addRowDelegate
	 */
	public ModelListingWidget(IListingConfig<Model> config, IAddRowDelegate addRowDelegate) {
		super(config, addRowDelegate);
		// create and initialize the table panel
		table = new ModelListingTable(config);
		portal.add(table);
		focusPanel.addKeyboardListener(table);
	}

	/**
	 * Sets the listing operator for this listing.
	 * @param operator the operator to set
	 */
	@Override
	public void setOperator(IListingOperator operator) {
		super.setOperator(operator);
		table.setListingOperator(operator);
	}

	public void addTableListener(TableListener listener) {
		table.addTableListener(listener);
	}

	public void removeTableListener(TableListener listener) {
		table.removeTableListener(listener);
	}

	@Override
	public final void addRow(Model rowData) {
		super.addRow(rowData);
		table.addRow(rowData);
	}

	@Override
	public void updateRow(int rowIndex, Model rowData) {
		super.updateRow(rowIndex, rowData);
		table.updateRow(rowIndex, rowData);
	}

	@Override
	public void deleteRow(int rowIndex) {
		super.deleteRow(rowIndex);
		table.deleteRow(rowIndex);
	}

	@Override
	public void markRowDeleted(int rowIndex) {
		super.markRowDeleted(rowIndex);
		table.markRowDeleted(rowIndex);
	}

	@Override
	public void setPage(IPage<Model> page, Sorting sorting) {
		super.setPage(page, sorting);
		table.setPage(page, sorting);
	}

	/**
	 * Get the row ref for a given row.
	 * @param row 0-based table row num (considers the header row).
	 * @return RefKey
	 */
	public RefKey getRowRef(int row) {
		return table.getRowRef(row);
	}

	/**
	 * Get the row index given a {@link RefKey}.
	 * @param rowRef The RefKey for which to find the associated row index.
	 * @return The row index or <code>-1</code> if no row matching the given ref
	 *         key is present in the table.
	 */
	public int getRowIndex(RefKey rowRef) {
		return table.getRowIndex(rowRef);
	}

	@Override
	public final void onModelChangeEvent(ModelChangeEvent event) {
		switch(event.getChangeOp()) {
			case ADDED:
				// TODO make this check more robust
				if(this.getElement().isOrHasChild(event.getWidget().getElement())) {
					// i.e. the add button in the nav bar was the source of the model
					// change..
					addRow(event.getModel());
				}
				break;
			case UPDATED: {
				RefKey modelRef = event.getModel().getRefKey();
				int rowIndex = table.getRowIndex(modelRef);
				if(rowIndex != -1) {
					assert rowIndex > 0; // header row
					// TODO determine how to handle named query specific model data!!
					updateRow(rowIndex, event.getModel());
				}
				break;
			}
			case DELETED: {
				RefKey modelRef = event.getModelRef();
				int rowIndex = table.getRowIndex(modelRef);
				if(rowIndex != -1) {
					assert rowIndex > 0; // header row
					markRowDeleted(rowIndex);
				}
				break;
			}

		}
	}
}
