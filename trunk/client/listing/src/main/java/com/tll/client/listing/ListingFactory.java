/**
 * 
 */
package com.tll.client.listing;

import com.tll.client.ui.listing.ListingTable;
import com.tll.client.ui.listing.ListingWidget;
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
		final DataListingOperator<R> lo =
				new DataListingOperator<R>(config.getPageSize(), dataProvider, (config.isSortable() ? config
						.getDefaultSorting() : null));
		final ListingWidget<R, ListingTable<R>> lw =
				new ListingWidget<R, ListingTable<R>>(config, new ListingTable<R>(config));
		lw.setOperator(lo);
		return lw;
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
	 * @return A new {@link RemoteListingWidget}.
	 */
	public static <S extends ISearch> RemoteListingWidget createRemoteListingWidget(IListingConfig<Model> config,
			String listingName, ListHandlerType listHandlerType, S searchCriteria, String[] propKeys, Sorting initialSorting) {
		final RemoteListingWidget lw = new RemoteListingWidget(config);
		lw.setOperator(createRemoteOperator(listingName, listHandlerType, searchCriteria, propKeys, config.getPageSize(),
				initialSorting));
		return lw;
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
}
