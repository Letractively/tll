/**
 * 
 */
package com.tll.client.listing;

import com.tll.client.search.ISearch;
import com.tll.client.ui.listing.AbstractListingWidget;
import com.tll.client.ui.listing.RowContextListingWidget;
import com.tll.listhandler.ListHandlerType;

/**
 * ListingFactory - Assembles listing Widgets used for showing listing data.
 * @author jpk
 */
public abstract class ListingFactory {

	/**
	 * Factory method for a listing Widget.
	 * @param config The listing config
	 * @param rowOptionsDelegate May be <code>null</code>
	 * @param addRowDelegate May be <code>null</code>
	 * @return A new listing Widget
	 */
	private static AbstractListingWidget assembleListingWidget(IListingConfig config,
			IRowOptionsDelegate rowOptionsDelegate, IAddRowDelegate addRowDelegate) {
		return new RowContextListingWidget(config, rowOptionsDelegate, addRowDelegate);
	}

	/**
	 * Assembles an RPC based listing Widget.
	 * @param config The listing config
	 * @param criteria The search criteria
	 * @param listHandlerType The server side list handler type
	 * @param rowOptionsDelegate Optional. If <code>null</code>, no row options
	 *        will be available.
	 * @param addRowDelegate The optional add row delegate that will handle adding
	 *        rows.
	 * @return A new listing Widget
	 */
	public static AbstractListingWidget rpcListing(IListingConfig config, ISearch criteria,
			ListHandlerType listHandlerType, IRowOptionsDelegate rowOptionsDelegate, IAddRowDelegate addRowDelegate) {
		AbstractListingWidget listingWidget = assembleListingWidget(config, rowOptionsDelegate, addRowDelegate);
		listingWidget.setOperator(new RpcListingOperator(listingWidget, config.getListingName(), listHandlerType, config
				.getPageSize(), config.getPropKeys(), criteria, (config.isSortable() ? config.getDefaultSorting() : null)));
		return listingWidget;
	}

	/**
	 * Assembles a listing from data provided by an {@link IDataProvider}
	 * instance.
	 * @param config The listing config
	 * @param dataProvider The listing data collection
	 * @param rowOptionsDelegate Optional. If <code>null</code>, no row options
	 *        will be available.
	 * @param addRowDelegate The optional add row delegate that will handle adding
	 *        rows.
	 * @return A new listing Widget
	 */
	public static AbstractListingWidget dataListing(IListingConfig config, IDataProvider dataProvider,
			IRowOptionsDelegate rowOptionsDelegate, IAddRowDelegate addRowDelegate) {
		AbstractListingWidget listingWidget = assembleListingWidget(config, rowOptionsDelegate, addRowDelegate);
		listingWidget.setOperator(new DataListingOperator(listingWidget, config.getPageSize(), dataProvider, (config
				.isSortable() ? config.getDefaultSorting() : null)));
		return listingWidget;
	}
}
