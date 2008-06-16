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
import com.tll.client.ui.listing.RowContextPopup;

/**
 * ListingFactory - Assembles listing Widgets used for showing listing data.
 * @author jpk
 */
public abstract class ListingFactory {

	/**
	 * Creates a {@link RowContextPopup} and sets this in the listing Widget if
	 * the given row options handler is not <code>null</code>.
	 * @param <R>
	 * @param listingWidget
	 * @param rowOptionsHandler
	 */
	private static <R extends IData> void applyRowOptionsHandler(ListingWidget<R> listingWidget,
			IRowOptionsDelegate rowOptionsHandler) {
		if(rowOptionsHandler != null) {
			listingWidget.setRowPopup(new RowContextPopup(rowOptionsHandler));
		}
	}

	/**
	 * Assembles a listing Widget from a RemoteListingDefinition.
	 * @param <S> The search type
	 * @param config The listing config
	 * @param listingDef The remote (server-side) listing definition
	 * @return A new listing Widget
	 */
	public static <S extends ISearch> ListingWidget<Model> create(IListingConfig<Model> config,
			RemoteListingDefinition<S> listingDef) {

		ModelListingWidget listingWidget = new ModelListingWidget(config, config.getAddRowHandler());

		ListingCommand<S> lc = new ListingCommand<S>(listingWidget, config.getListingName(), listingDef);
		lc.addListingListener(listingWidget);

		applyRowOptionsHandler(listingWidget, config.getRowOptionsHandler());

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

		DataListingOperator<R> dlo =
				new DataListingOperator<R>(listingWidget, config.getPageSize(), dataProvider, (config.isSortable() ? config
						.getDefaultSorting() : null));

		dlo.addListingListener(listingWidget);

		applyRowOptionsHandler(listingWidget, config.getRowOptionsHandler());

		return listingWidget;
	}
}
