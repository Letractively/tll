/**
 * 
 */
package com.tll.client.listing;

import com.tll.client.search.ISearch;
import com.tll.client.ui.listing.ListingWidget;
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
	private static ListingWidget assembleListingWidget(IListingConfig config, IRowOptionsDelegate rowOptionsDelegate,
			IAddRowDelegate addRowDelegate) {
		if(rowOptionsDelegate != null) {
			return new ListingWidget(config, addRowDelegate);
		}
		return new RowContextListingWidget(config, rowOptionsDelegate, addRowDelegate);
	}

	/**
	 * Assembles an RPC based listing Widget.
	 * @param <S> The search type
	 * @param config The listing config
	 * @param props Array of properties serving as a filter on the server side
	 *        where only these properties will be provided
	 * @param criteria The search criteria
	 * @param listHandlerType The server side list handler type
	 * @param rowOptionsDelegate Optional. If <code>null</code>, no row options
	 *        will be available.
	 * @param addRowDelegate The optional add row delegate that will handle adding
	 *        rows.
	 * @return A new listing Widget
	 */
	public static <S extends ISearch> ListingWidget rpcListing(IListingConfig config, String[] props, S criteria,
			ListHandlerType listHandlerType, IRowOptionsDelegate rowOptionsDelegate, IAddRowDelegate addRowDelegate) {
		ListingWidget listingWidget = assembleListingWidget(config, rowOptionsDelegate, addRowDelegate);
		listingWidget.setOperator(new RpcListingOperator<S>(listingWidget, config.getListingName(), listHandlerType, config
				.getPageSize(), props, criteria, (config.isSortable() ? config.getDefaultSorting() : null)));
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
	public static ListingWidget dataListing(IListingConfig config, IDataProvider dataProvider,
			IRowOptionsDelegate rowOptionsDelegate, IAddRowDelegate addRowDelegate) {
		ListingWidget listingWidget = assembleListingWidget(config, rowOptionsDelegate, addRowDelegate);
		listingWidget.setOperator(new DataListingOperator(listingWidget, config.getPageSize(), dataProvider, (config
				.isSortable() ? config.getDefaultSorting() : null)));
		return listingWidget;
	}
}
