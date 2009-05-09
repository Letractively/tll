/**
 * The Logic Lab
 * @author jpk
 * Apr 20, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.tll.client.listing.AbstractRowOptions;
import com.tll.client.model.IHasModelChangeHandlers;
import com.tll.client.model.IModelChangeHandler;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.model.ModelChangeManager;
import com.tll.client.mvc.ViewManager;
import com.tll.client.ui.listing.ModelListingWidget;

/**
 * ListingView - View dedicated to a single listing.
 * @author jpk
 * @param <I> the view initializer type
 */
public abstract class ListingView<I extends IViewInitializer> extends AbstractModelAwareView<I> implements IHasModelChangeHandlers {

	protected static final ViewOptions VIEW_OPTIONS = new ViewOptions(true, false, true, false, false);

	protected static abstract class AbstractListingViewClass extends ViewClass {

		@Override
		public ViewOptions getViewOptions() {
			return VIEW_OPTIONS;
		}

	}

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
			ModelChangeManager.deleteModel(listingWidget, listingWidget.getRowKey(rowIndex), null).execute();
		}

	}

	/**
	 * The listing widget.
	 */
	protected ModelListingWidget listingWidget;

	/**
	 * Constructor
	 */
	public ListingView() {
		super();
		// listing views shall notify the other views of model change events that
		// happen in the listing
		addModelChangeHandler(ViewManager.get());
	}

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
	public HandlerRegistration addModelChangeHandler(IModelChangeHandler handler) {
		return addHandler(handler, ModelChangeEvent.TYPE);
	}

	@Override
	protected boolean shouldHandleModelChangeEvent(ModelChangeEvent event) {
		// we don't know if the listing contains a corres. row so we simply defer to
		// the listing widget
		return true;
	}

	public String getLongViewName() {
		return null;
	}

	@Override
	protected void handleModelChangeSuccess(ModelChangeEvent event) {
		if(listingWidget != null) listingWidget.onModelChangeEvent(event);
	}
}
