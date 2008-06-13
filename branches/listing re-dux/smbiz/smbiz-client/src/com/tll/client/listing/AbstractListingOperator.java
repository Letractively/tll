/**
 * The Logic Lab
 * @author jpk
 * Apr 20, 2008
 */
package com.tll.client.listing;

import com.tll.client.model.IData;
import com.tll.client.ui.listing.ListingWidget;

/**
 * AbstractListingOperator
 * @author jpk
 */
public abstract class AbstractListingOperator<R extends IData> implements IListingOperator {

	/**
	 * The listing this operator operates on.
	 */
	protected final ListingWidget<R> listingWidget;

	/**
	 * Constructor
	 * @param listingWidget
	 */
	public AbstractListingOperator(ListingWidget<R> listingWidget) {
		super();
		this.listingWidget = listingWidget;
	}
}
