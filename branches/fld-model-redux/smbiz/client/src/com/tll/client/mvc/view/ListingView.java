/**
 * The Logic Lab
 * @author jpk
 * Apr 20, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.event.type.EditViewRequest;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.listing.AbstractRowOptions;
import com.tll.client.model.ModelChangeManager;
import com.tll.client.mvc.ViewManager;
import com.tll.client.ui.listing.ModelListingWidget;

/**
 * ListingView - View dedicated to a single listing.
 * @author jpk
 */
public abstract class ListingView extends AbstractView {

	/**
	 * ModelChangingRowOpDelegate - Handles standard edit/delete row op selections
	 * @author jpk
	 */
	protected abstract class ModelChangingRowOpDelegate extends AbstractRowOptions {

		/**
		 * Provides the necessary sourcing widget enabling the sourcing of potential
		 * events that are driven by option selections.
		 * @return A non-<code>null</code> Widget ref
		 */
		protected abstract Widget getSourcingWidget();

		/**
		 * @return The class of the view to display for editing listing rows.
		 */
		protected abstract ViewClass getEditViewClass();

		/**
		 * This method is invoked whtn a row is targeted for editing.
		 * @param rowIndex The row index of the targeted row
		 * @param rowRef The ref of the row to edit
		 */
		@Override
		protected void doEditRow(int rowIndex) {
			ViewManager.instance().dispatch(
					new EditViewRequest(getSourcingWidget(), getEditViewClass(), listingWidget.getRowRef(rowIndex)));
		}

		/**
		 * This method is invoked when a row is targeted for deletion.
		 * @param rowIndex The row index of the targeted row
		 * @param rowRef The ref of the row to delete
		 */
		@Override
		protected void doDeleteRow(int rowIndex) {
			ModelChangeManager.instance().handleModelDelete(ListingView.this, listingWidget.getRowRef(rowIndex), null);
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

	public final void onModelChangeEvent(ModelChangeEvent event) {
		if(listingWidget != null) listingWidget.onModelChangeEvent(event);
	}

}
