/**
 * 
 */
package com.tll.client.listing;

import com.tll.client.ui.listing.ListingTable;
import com.tll.client.ui.listing.ListingWidget;
import com.tll.client.ui.listing.ModelListingWidget;
import com.tll.client.ui.listing.RemoteListingWidget;
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
	 * @param config The listing config
	 * @param dataProvider The client listing data provider
	 * @return A new {@link ListingWidget}.
	 */
	public static <R> ListingWidget<R, ListingTable<R>> createListingWidget(IListingConfig<R> config,
			IListHandler<R> dataProvider) {
		return assemble(config, new ListingWidget<R, ListingTable<R>>(config, new ListingTable<R>(config)),
				new DataListingOperator<R>(config.getPageSize(), dataProvider, (config.isSortable() ? config
						.getDefaultSorting() : null)));
	}

	/**
	 * Crates a listing Widget based on a remote data source.
	 * @param <S> the search type
	 * @param config The client listing configuration
	 * @param listingName The unique listing name.
	 * @param listHandlerType The remote list handler type
	 * @param searchCriteria The search criteria that generates the remote
	 *        listing.
	 * @param propKeys optional array of OGNL property names that filter the
	 *        results on the server.
	 * @param initialSorting The initial sorting directive
	 * @return A new {@link ModelListingWidget}.
	 */
	public static <S extends ISearch> RemoteListingWidget createRemoteListingWidget(IListingConfig<Model> config,
			String listingName, ListHandlerType listHandlerType, S searchCriteria, String[] propKeys, Sorting initialSorting) {
		final RemoteListingOperator<S> lo =
				createRemoteOperator(listingName, listHandlerType, searchCriteria, propKeys, config.getPageSize(),
						initialSorting);
		return assemble(config, new RemoteListingWidget(config), lo);
	}

	/**
	 * Creates a listing command to control acccess to a remote listing.
	 * @param <S> The search type
	 * @param listingName The unique remote listing name
	 * @param listHandlerType The remote list handler type
	 * @param searchCriteria The search criteria that generates the remote
	 *        listing.
	 * @param propKeys Optional OGNL formatted property names representing a
	 *        white-list of properties to retrieve from those that are queried. If
	 *        <code>null</code>, all queried properties are provided.
	 * @param pageSize The desired paging page size.
	 * @param initialSorting The initial sorting directive
	 * @return A new {@link RemoteListingOperator}
	 */
	public static <S extends ISearch> RemoteListingOperator<S> createRemoteOperator(String listingName,
			ListHandlerType listHandlerType, S searchCriteria, String[] propKeys, int pageSize, Sorting initialSorting) {

		final RemoteListingDefinition<S> rld =
				new RemoteListingDefinition<S>(listHandlerType, searchCriteria, propKeys, pageSize, initialSorting);
		return new RemoteListingOperator<S>(listingName, rld);
	}

	/**
	 * Does the actual listing Widget assembling.
	 * @param <R> the row element type
	 * @param <T> the listing table widget type
	 * @param <LW> the listing widget type
	 * @param config
	 * @param listingWidget
	 * @param operator
	 * @return the assembled listing widget
	 */
	private static <R, T extends ListingTable<R>, LW extends ListingWidget<R, T>> LW assemble(IListingConfig<R> config,
			LW listingWidget, IListingOperator<R> operator) {

		if(config.getAddRowHandler() != null) listingWidget.setAddRowDelegate(config.getAddRowHandler());

		if(config.getRowOptionsHandler() != null) listingWidget.setRowOptionsDelegate(config.getRowOptionsHandler());

		listingWidget.setOperator(operator);

		return listingWidget;
	}
}
