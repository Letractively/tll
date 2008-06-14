/**
 * 
 */
package com.tll.client.listing;

import com.tll.client.model.IData;
import com.tll.client.model.Model;
import com.tll.client.search.ISearch;
import com.tll.client.ui.listing.DataListingWidget;
import com.tll.client.ui.listing.ListingWidget;
import com.tll.client.ui.listing.ModelListingWidget;
import com.tll.client.ui.listing.RowContextPopup;
import com.tll.listhandler.ListHandlerType;

/**
 * ListingFactory - Assembles listing Widgets used for showing listing data.
 * @author jpk
 */
public abstract class ListingFactory {

	private static <R extends IData> void applyRowOptionsHandler(ListingWidget<R> listingWidget,
			IRowOptionsDelegate rowOptionsHandler) {
		if(rowOptionsHandler != null) {
			RowContextPopup rowContextPopup = new RowContextPopup(rowOptionsHandler);
			// listingWidget.addTableListener(rowContextPopup);
			// listingWidget.addMouseListener(rowContextPopup);
			listingWidget.setRowPopup(rowContextPopup);
		}
	}

	/**
	 * Assembles a listing Widget given an ISearch instance serving as server side
	 * criteria for listing data.
	 * @param <S> The search type
	 * @param config The listing config
	 * @param props Array of properties serving as a filter on the server side
	 *        where only these properties will be provided
	 * @param criteria The search criteria
	 * @param listHandlerType The server side list handler type
	 * @return A new listing Widget
	 */
	public static <S extends ISearch> ListingWidget<Model> create(IListingConfig<Model> config, String[] props,
			S criteria, ListHandlerType listHandlerType) {

		ModelListingWidget listingWidget = new ModelListingWidget(config, config.getAddRowHandler());

		applyRowOptionsHandler(listingWidget, config.getRowOptionsHandler());

		listingWidget.setOperator(new RpcListingOperator<S>(listingWidget, config.getListingName(), listHandlerType, config
				.getPageSize(), props, criteria, (config.isSortable() ? config.getDefaultSorting() : null)));

		return listingWidget;
	}

	/**
	 * Assembles a listing from data provided by an {@link IDataProvider}
	 * instance.
	 * @param config The listing config
	 * @param dataProvider The listing data collection
	 * @return A new listing Widget
	 */
	public static <R extends IData> ListingWidget<R> create(IListingConfig<R> config, IDataProvider<R> dataProvider) {

		DataListingWidget<R> listingWidget = new DataListingWidget<R>(config, config.getAddRowHandler());

		listingWidget.setOperator(new DataListingOperator<R>(listingWidget, config.getPageSize(), dataProvider, (config
				.isSortable() ? config.getDefaultSorting() : null)));

		return listingWidget;
	}
}
