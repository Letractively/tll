/**
 * 
 */
package com.tll.client.listing;

import com.google.gwt.user.client.ui.Widget;
import com.tll.IMarshalable;
import com.tll.client.data.rpc.ListingCommand;
import com.tll.client.ui.listing.DataListingWidget;
import com.tll.client.ui.listing.ListingWidget;
import com.tll.client.ui.listing.ModelListingWidget;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.common.model.Model;
import com.tll.common.search.ISearch;
import com.tll.dao.Sorting;
import com.tll.listhandler.IListHandler;
import com.tll.listhandler.ListHandlerType;

/**
 * ListingFactory - Assembles listing Widgets used for showing listing data.
 * @author jpk
 */
public abstract class ListingFactory {

	/**
	 * Assembles a listing from data provided by an {@link IListHandler}.
	 * @param <R>
	 * @param sourcingWidget The Widget that will be passed in dispatched
	 *        {@link ListingEvent}s.
	 * @param config The listing config
	 * @param dataProvider The client listing data provider
	 * @return A new {@link DataListingWidget}.
	 */
	public static <R> DataListingWidget<R> createListingWidget(Widget sourcingWidget, IListingConfig<R> config,
			IListHandler<R> dataProvider) {

		return (DataListingWidget<R>) assemble(config, new DataListingWidget<R>(config), new DataListingOperator<R>(
				sourcingWidget, config.getPageSize(), dataProvider, (config.isSortable() ? config.getDefaultSorting() : null)));
	}

	/**
	 * Crates a listing Widget based on a remote data source.
	 * @param <S> The search type
	 * @param sourcingWidget The Widget that will be passed in dispatched
	 *        {@link ListingEvent}s.
	 * @param config The client listing configuration
	 * @param listingName The unique listing name.
	 * @param listHandlerType The remote list handler type
	 * @param searchCriteria The search criteria that generates the remote
	 *        listing.
	 * @param propKeys Optional OGNL formatted property names representing a
	 *        white-list of properties to retrieve from those that are queried. If
	 *        <code>null</code>, all queried properties are provided.
	 * @param initialSorting The initial sorting directive
	 * @return A new {@link ModelListingWidget}.
	 */
	public static <S extends ISearch> ModelListingWidget createListingWidget(Widget sourcingWidget,
			IListingConfig<Model> config, String listingName, ListHandlerType listHandlerType, S searchCriteria,
			String[] propKeys, Sorting initialSorting) {
		final IListingOperator<Model> lo =
				createRemoteOperator(sourcingWidget, listingName, listHandlerType, searchCriteria, propKeys, config
						.getPageSize(), initialSorting);
		return (ModelListingWidget) assemble(config, new ModelListingWidget(config), lo);
	}

	/**
	 * Creates a listing command to control acccess to a remote listing.
	 * @param <S> The search type
	 * @param <R> the row element type
	 * @param sourcingWidget The Widget that will be passed in dispatched
	 *        {@link ListingEvent}s.
	 * @param listingName The unique remote listing name
	 * @param listHandlerType The remote list handler type
	 * @param searchCriteria The search criteria that generates the remote
	 *        listing.
	 * @param propKeys Optional OGNL formatted property names representing a
	 *        white-list of properties to retrieve from those that are queried. If
	 *        <code>null</code>, all queried properties are provided.
	 * @param pageSize The desired paging page size.
	 * @param initialSorting The initial sorting directive
	 * @return A new {@link ListingCommand}.
	 */
	public static <S extends ISearch, R extends IMarshalable> RemoteListingOperator<S, R> createRemoteOperator(
			Widget sourcingWidget, String listingName,
			ListHandlerType listHandlerType, S searchCriteria, String[] propKeys, int pageSize, Sorting initialSorting) {
		
		final RemoteListingDefinition<S> rld =
				new RemoteListingDefinition<S>(listHandlerType, searchCriteria, propKeys, pageSize, initialSorting);
		return new RemoteListingOperator<S, R>(sourcingWidget, listingName, rld);
	}

	/**
	 * Does the actual listing Widget assembling.
	 * @param <R>
	 * @param config
	 * @param listingWidget
	 * @param operator
	 * @return the assembled listing widget
	 */
	private static <R> ListingWidget<R> assemble(IListingConfig<R> config, ListingWidget<R> listingWidget,
			IListingOperator<R> operator) {

		if(config.getAddRowHandler() != null) listingWidget.setAddRowDelegate(config.getAddRowHandler());

		if(config.getRowOptionsHandler() != null) listingWidget.setRowOptionsDelegate(config.getRowOptionsHandler());

		listingWidget.setOperator(operator);

		return listingWidget;
	}
}
