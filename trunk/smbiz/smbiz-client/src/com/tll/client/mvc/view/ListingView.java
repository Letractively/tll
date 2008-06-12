/**
 * The Logic Lab
 * @author jpk
 * Apr 20, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.EntityOptions;
import com.tll.client.event.type.EditViewRequest;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.listing.RowOpDelegate;
import com.tll.client.model.AbstractModelChangeHandler;
import com.tll.client.model.RefKey;
import com.tll.client.mvc.Dispatcher;
import com.tll.client.mvc.ViewManager;
import com.tll.client.ui.listing.AbstractListingWidget;

/**
 * ListingView - View dedicated to a single listing.
 * @author jpk
 */
public abstract class ListingView extends AbstractView {

	/**
	 * ModelChangingRowOpDelegate - Handles standard edit/delete row op selections
	 * @author jpk
	 */
	protected abstract class ModelChangingRowOpDelegate extends RowOpDelegate {

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
		 */
		@Override
		protected void doEditRow(int rowIndex) {
			// TODO need to resolve the row ref!!!!!!!
			RefKey rowRef = null;
			Dispatcher.instance().dispatch(new EditViewRequest(getSourcingWidget(), getEditViewClass(), rowRef));
		}

		/**
		 * This method is invoked when a row is targeted for deletion.
		 * @param rowIndex The row index of the targeted row
		 */
		@Override
		protected void doDeleteRow(int rowIndex) {
			AbstractModelChangeHandler handler = new AbstractModelChangeHandler() {

				@Override
				protected Widget getSourcingWidget() {
					return ListingView.this;
				}

				@Override
				protected AuxDataRequest getNeededAuxData() {
					return null;
				}

				@Override
				protected EntityOptions getEntityOptions() {
					return null;
				}

			};
			handler.addModelChangeListener(ViewManager.instance());
			// TODO need to resolve the row ref!!!!!!!
			RefKey rowRef = null;
			handler.handleModelDelete(rowRef);
		}

	}

	/**
	 * The listing widget.
	 */
	private AbstractListingWidget listingWidget;

	/**
	 * Sets the listing widget on this listing view handling necessary tasks
	 * associated with it.
	 * @param listingWidget The listing widget to set for this listing view.
	 */
	protected final void setListingWidget(AbstractListingWidget listingWidget) {
		assert listingWidget != null;
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
