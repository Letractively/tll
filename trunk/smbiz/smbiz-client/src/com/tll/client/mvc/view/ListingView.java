/**
 * The Logic Lab
 * @author jpk
 * Apr 20, 2008
 */
package com.tll.client.mvc.view;

import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.ui.listing.AbstractListingWidget;

/**
 * ListingView - View dedicated to a single listing.
 * @author jpk
 */
public abstract class ListingView extends AbstractView {

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
		// ModelChangeEventDispatcher.instance().addModelChangeListener(listingWidget);
		addWidget(listingWidget);
	}

	public final void refresh() {
		if(listingWidget != null) listingWidget.refresh();
	}

	@Override
	protected final void doDestroy() {
		if(listingWidget != null) {
			listingWidget.clear();
			// ModelChangeEventDispatcher.instance().removeModelChangeListener(listingWidget);
		}
	}

	public final void onModelChangeEvent(ModelChangeEvent event) {
		listingWidget.onModelChangeEvent(event);
	}

}
