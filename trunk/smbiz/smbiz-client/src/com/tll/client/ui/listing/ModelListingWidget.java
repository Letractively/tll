/**
 * The Logic Lab
 * @author jpk
 * Jun 12, 2008
 */
package com.tll.client.ui.listing;

import com.tll.client.event.IModelChangeListener;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IListingOperator;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.listhandler.IPage;
import com.tll.listhandler.Sorting;

/**
 * ModelListingWidget
 * @author jpk
 */
public class ModelListingWidget extends AbstractListingWidget<Model> implements IModelChangeListener {

	/**
	 * The actual listing table.
	 */
	protected final ModelTable table;

	/**
	 * Constructor
	 * @param config
	 */
	public ModelListingWidget(IListingConfig config) {
		super(config);
		table = new ModelTable(config);
	}

	public final void addRow(Model rowData) {
		table.addRow(rowData);
		if(navBar != null) navBar.increment();
	}

	public final void updateRow(int rowIndex, Model rowData) {
		table.updateRow(rowIndex, rowData);
	}

	// TODO do we need this?
	public final void deleteRow(int rowIndex) {
		table.deleteRow(rowIndex);
		if(navBar != null) navBar.decrement();
	}

	public final void markRowDeleted(int rowIndex) {
		table.markRowDeleted(rowIndex);
	}

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

	@Override
	public void setOperator(IListingOperator operator) {
		table.setListingOperator(operator);
		super.setOperator(operator);
	}

	@Override
	public void setPage(IPage<Model> page, Sorting sorting) {
		table.setPage(page, sorting);
		super.setPage(page, sorting);
	}
}
