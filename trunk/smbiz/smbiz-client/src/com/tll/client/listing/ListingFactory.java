/**
 * 
 */
package com.tll.client.listing;

import java.util.List;

import com.tll.client.model.Model;
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
	 * @param rowOptionsProvider May be <code>null</code>
	 * @return A new listing Widget
	 */
	private static AbstractListingWidget assembleListingWidget(IListingConfig config,
			IRowOptionsProvider rowOptionsProvider) {
		return new RowContextListingWidget(config, rowOptionsProvider);
	}

	/**
	 * Assembles an RPC based listing Widget.
	 * @param config The listing config
	 * @param criteria The search criteria
	 * @param listHandlerType The server side list handler type
	 * @param rowOptionsProvider Optional. If <code>null</code>, no row options
	 *        will be available.
	 * @return A new listing Widget
	 */
	public static AbstractListingWidget rpcListing(IListingConfig config, ISearch criteria,
			ListHandlerType listHandlerType, IRowOptionsProvider rowOptionsProvider) {
		AbstractListingWidget listingWidget = assembleListingWidget(config, rowOptionsProvider);
		listingWidget.setOperator(new RpcListingOperator(listingWidget, config, listHandlerType, criteria));
		return listingWidget;
	}

	/**
	 * Assembles a collection based listing Widget.
	 * @param config The listing config
	 * @param data The listing data collection
	 * @param pageSize if <code>-1</code>, the listing will <em>not</em> be
	 *        page-able
	 * @param rowOptionsProvider Optional. If <code>null</code>, no row options
	 *        will be available.
	 * @return A new listing Widget
	 */
	public static AbstractListingWidget collectionListing(IListingConfig config, List<Model> data, int pageSize,
			IRowOptionsProvider rowOptionsProvider) {
		AbstractListingWidget listingWidget = assembleListingWidget(config, rowOptionsProvider);
		listingWidget.setOperator(new DataCollectionListingOperator(listingWidget, pageSize, data));
		return listingWidget;
	}
}
