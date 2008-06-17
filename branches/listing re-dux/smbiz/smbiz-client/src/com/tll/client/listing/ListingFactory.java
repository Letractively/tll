/**
 * 
 */
package com.tll.client.listing;

import com.tll.client.data.RemoteListingDefinition;
import com.tll.client.data.rpc.ListingCommand;
import com.tll.client.model.IData;
import com.tll.client.model.Model;
import com.tll.client.search.ISearch;
import com.tll.client.ui.listing.DataListingWidget;
import com.tll.client.ui.listing.ListingWidget;
import com.tll.client.ui.listing.ModelListingWidget;

/**
 * ListingFactory - Assembles listing Widgets used for showing listing data.
 * @author jpk
 */
public abstract class ListingFactory {

	/**
	 * Does the actual listing Widget assembling.
	 * @param <R>
	 * @param config
	 * @param listingWidget
	 * @param operator
	 * @return
	 */
	private static <R extends IData> ListingWidget<R> assemble(IListingConfig<R> config, ListingWidget<R> listingWidget,
			IListingOperator<R> operator) {

		if(config.getAddRowHandler() != null) listingWidget.setAddRowDelegate(config.getAddRowHandler());

		if(config.getRowOptionsHandler() != null) listingWidget.setRowOptionsDelegate(config.getRowOptionsHandler());

		listingWidget.setOperator(operator);

		return listingWidget;
	}

	/**
	 * Assembles a listing based on a remote data source returning the remote data
	 * operator.
	 * @param <S> The search type
	 * @param config The listing config
	 * @param listingDef The remote (server-side) listing definition
	 * @return A new {@link ModelListingWidget}.
	 */
	public static <S extends ISearch> ModelListingWidget create(IListingConfig<Model> config,
			RemoteListingDefinition<S> listingDef) {

		return (ModelListingWidget) assemble(config, new ModelListingWidget(config), new ListingCommand<S>(config
				.getListingName(), listingDef));
	}

	/**
	 * Assembles a listing from data provided by an {@link IDataProvider}
	 * returning the client data operator.
	 * @param config The listing config
	 * @param dataProvider The client listing data provider
	 * @return A new {@link DataListingWidget}.
	 */
	public static <R extends IData> DataListingWidget<R> create(IListingConfig<R> config, IDataProvider<R> dataProvider) {

		return (DataListingWidget<R>) assemble(config, new DataListingWidget<R>(config), new DataListingOperator<R>(config
				.getPageSize(), dataProvider, (config.isSortable() ? config.getDefaultSorting() : null)));
	}
}
