/**
 * The Logic Lab
 * @author jpk
 * Apr 20, 2008
 */
package com.tll.client.mvc.view;

import com.tll.client.model.ModelChangeEvent;
import com.tll.client.mvc.ModelChangeViewHandler;
import com.tll.client.ui.listing.ModelListingWidget;

/**
 * ListingView - View dedicated to a single listing.
 * @author jpk
 * @param <I> the view initializer type
 * @param <L> the listing widget type
 */
public abstract class ListingView<I extends IViewInitializer, L extends ModelListingWidget> extends AbstractModelAwareView<I> {

	protected static abstract class AbstractListingViewClass extends ViewClass {

		private static final ViewOptions VIEW_OPTIONS = new ViewOptions(true, false, true, false, false);

		@Override
		public ViewOptions getViewOptions() {
			return VIEW_OPTIONS;
		}

	}

	/**
	 * The listing widget.
	 */
	protected L listingWidget;

	/**
	 * Sets the listing widget on this listing view handling necessary tasks
	 * associated with it.
	 * @param listingWidget The listing widget to set for this listing view.
	 */
	protected final void setListingWidget(L listingWidget) {
		listingWidget.addModelChangeHandler(ModelChangeViewHandler.get());
		addWidget(listingWidget);
		this.listingWidget = listingWidget;
	}

	@Override
	protected final void doRefresh() {
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
