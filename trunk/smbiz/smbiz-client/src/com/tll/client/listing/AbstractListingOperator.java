/**
 * The Logic Lab
 * @author jpk
 * Apr 20, 2008
 */
package com.tll.client.listing;

import com.tll.client.ui.listing.AbstractListingWidget;

/**
 * AbstractListingOperator
 * @author jpk
 */
public abstract class AbstractListingOperator implements IListingOperator {

	/**
	 * The listing this operator operates on.
	 */
	protected AbstractListingWidget listingWidget;

	/**
	 * Constructor
	 * @param listingWidget
	 */
	public AbstractListingOperator() {
		super();
	}

	/**
	 * @param listingWidget the listingWidget to set
	 */
	public final void setListingWidget(AbstractListingWidget listingWidget) {
		this.listingWidget = listingWidget;
	}

}
