/**
 * The Logic Lab
 * @author jpk
 * Apr 20, 2008
 */
package com.tll.client.mvc.view;

import com.tll.client.listing.AbstractRowOptions;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.model.ModelChangeManager;
import com.tll.client.mvc.ViewManager;
import com.tll.client.ui.listing.ModelListingWidget;

/**
 * ListingView - View dedicated to a single listing.
 * @author jpk
 * @param <I> the view initializer type
 */
public abstract class ListingView<I extends IViewInitializer> extends AbstractModelAwareView<I> {

	/**
	 * ModelChangingRowOpDelegate - Handles standard edit/delete row op selections
	 * @author jpk
	 */
	protected abstract class ModelChangingRowOpDelegate extends AbstractRowOptions {

		/**
		 * @return The class of the view to display for editing listing rows.
		 */
		protected abstract ViewClass getEditViewClass();

		/**
		 * This method is invoked whtn a row is targeted for editing.
		 * @param rowIndex The row index of the targeted row
		 */
		@Override
		protected void doEditRow(int rowIndex) {
			ViewManager.get().dispatch(
					new ShowViewRequest(new EditViewInitializer(getEditViewClass(), listingWidget.getRowKey(rowIndex))));
		}

		/**
		 * This method is invoked when a row is targeted for deletion.
		 * @param rowIndex The row index of the targeted row
		 */
		@Override
		protected void doDeleteRow(int rowIndex) {
			ModelChangeManager.get().deleteModel(ListingView.this, listingWidget.getRowKey(rowIndex), null);
		}

	}

	/**
	 * The listing widget.
	 */
	protected ModelListingWidget listingWidget;

	/**
	 * Sets the listing widget on this listing view handling necessary tasks
	 * associated with it.
	 * @param listingWidget The listing widget to set for this listing view.
	 */
	protected final void setListingWidget(ModelListingWidget listingWidget) {
		this.listingWidget = listingWidget;
		addWidget(listingWidget);
	}

	public final void refresh() {
		if(listingWidget != null) listingWidget.refresh();
	}

	@Override
	protected final void doDestroy() {
		if(listingWidget != null) {
			listingWidget.clear();
		}
	}

	@Override
	protected boolean shouldHandleModelChangeEvent(ModelChangeEvent event) {
		// TODO fix this - this will fail when invoking edit commands from a listing
		// and a subsequent edit view is rendered!!!
		return event.getSource() == this;
	}

	public String getLongViewName() {
		return null;
	}

	@Override
	protected void handleModelChangeSuccess(ModelChangeEvent event) {
		if(listingWidget != null) listingWidget.onModelChangeEvent(event);
	}
}
