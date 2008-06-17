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

	private static <R extends IData> void assemble(ListingWidget<R> listingWidget, IListingConfig<R> config) {

		if(config.getAddRowHandler() != null) listingWidget.setAddRowDelegate(config.getAddRowHandler());

		if(config.getRowOptionsHandler() != null) listingWidget.setRowOptionsDelegate(config.getRowOptionsHandler());
	}

	/**
	 * Assembles a listing based on a remote data source returning the remote data
	 * operator.
	 * @param <S> The search type
	 * @param config The listing config
	 * @param listingDef The remote (server-side) listing definition
	 * @return The remote listing operator containing the created listing Widget.
	 */
	public static <S extends ISearch> IListingOperator<Model> create(IListingConfig<Model> config,
			RemoteListingDefinition<S> listingDef) {

		final ModelListingWidget lw = new ModelListingWidget(config);
		assemble(lw, config);

		final ListingCommand<S> lc = new ListingCommand<S>(config.getListingName(), listingDef);
		lc.setListingWidget(lw);

		return lc;
	}

	/**
	 * Assembles a listing from data provided by an {@link IDataProvider}
	 * returning the client data operator.
	 * @param config The listing config
	 * @param dataProvider The client listing data provider
	 * @return The local (client) listing operator containing the created listing
	 *         Widget.
	 */
	public static <R extends IData> IListingOperator<R> create(IListingConfig<R> config, IDataProvider<R> dataProvider) {

		final DataListingWidget<R> dlw = new DataListingWidget<R>(config);
		assemble(dlw, config);

		final DataListingOperator<R> dlo =
				new DataListingOperator<R>(config.getPageSize(), dataProvider, (config.isSortable() ? config
						.getDefaultSorting() : null));
		dlo.setListingWidget(dlw);

		return dlo;
	}
}
