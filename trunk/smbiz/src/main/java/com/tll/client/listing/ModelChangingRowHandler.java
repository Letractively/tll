package com.tll.client.listing;

import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.EditViewInitializer;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.listing.ModelListingWidget;

/**
 * ModelChangingRowHandler - Handles standard edit/delete row op selections
 * @author jpk
 */
public abstract class ModelChangingRowHandler extends AbstractRowOptions {

	protected ModelListingWidget listingWidget;

	/**
	 * @return The class of the view to display for editing listing rows.
	 */
	protected abstract ViewClass getEditViewClass();

	@Override
	protected void doEditRow(int rowIndex) {
		ViewManager.get().dispatch(
				new ShowViewRequest(new EditViewInitializer(getEditViewClass(), listingWidget.getRowKey(rowIndex))));
	}

	@Override
	protected void doDeleteRow(int rowIndex) {
		CrudCommand.deleteModel(listingWidget, listingWidget.getRowKey(rowIndex)).execute();
	}
}