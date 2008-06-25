/**
 * 
 */
package com.tll.client.listing;

import com.tll.client.data.RemoteListingDefinition;
import com.tll.client.data.rpc.ListingCommand;
import com.tll.client.model.IData;
import com.tll.client.model.Model;
import com.tll.client.ui.listing.DataListingWidget;
import com.tll.client.ui.listing.ListingWidget;
import com.tll.client.ui.listing.ModelListingWidget;
import com.tll.criteria.Criteria;
import com.tll.listhandler.ListHandlerType;
import com.tll.listhandler.Sorting;
import com.tll.model.IEntity;

/**
 * ListingFactory - Assembles listing Widgets used for showing listing data.
 * @author jpk
 */
public abstract class ListingFactory {

	/**
	 * Assembles a listing from data provided by an {@link IDataProvider}.
	 * @param config The listing config
	 * @param dataProvider The client listing data provider
	 * @return A new {@link DataListingWidget}.
	 */
	public static <R extends IData> DataListingWidget<R> createListingWidget(IListingConfig<R> config,
			IDataProvider<R> dataProvider) {

		return (DataListingWidget<R>) assemble(config, new DataListingWidget<R>(config), new DataListingOperator<R>(config
				.getPageSize(), dataProvider, (config.isSortable() ? config.getDefaultSorting() : null)));
	}

	/**
	 * Creates a listing command to control acccess to a remote listing.
	 * @param <E> The entity type
	 * @param listingName The unique remote listing name
	 * @param listHandlerType The remote list handler type
	 * @param criteria The search criteria that generates the remote listing.
	 * @param propKeys Optional OGNL formatted property names representing a
	 *        white-list of properties to retrieve from those that are queried. If
	 *        <code>null</code>, all queried properties are provided.
	 * @param pageSize The desired paging page size.
	 * @param initialSorting The initial sorting directive
	 * @return A new {@link ListingCommand}.
	 */
	public static <E extends IEntity> ListingCommand<E> createListingCommand(String listingName,
			ListHandlerType listHandlerType, Criteria<E> criteria, String[] propKeys, int pageSize, Sorting initialSorting) {
		return new ListingCommand<E>(listingName, new RemoteListingDefinition<E>(listHandlerType, criteria, propKeys,
				pageSize, initialSorting));
	}

	/**
	 * Crates a listing Widget based on a remote data source.
	 * @param <E> The entity type
	 * @param config The client listing configuration
	 * @param listHandlerType The remote list handler type
	 * @param criteria The search criteria that generates the remote listing.
	 * @param propKeys Optional OGNL formatted property names representing a
	 *        white-list of properties to retrieve from those that are queried. If
	 *        <code>null</code>, all queried properties are provided.
	 * @param initialSorting The initial sorting directive
	 * @return A new {@link ModelListingWidget}.
	 */
	public static <E extends IEntity> ModelListingWidget createListingWidget(IListingConfig<Model> config,
			ListHandlerType listHandlerType, Criteria<E> criteria, String[] propKeys, Sorting initialSorting) {

		return (ModelListingWidget) assemble(config, new ModelListingWidget(config), new ListingCommand<E>(config
				.getListingName(), new RemoteListingDefinition<E>(listHandlerType, criteria, propKeys, config.getPageSize(),
				initialSorting)));
	}

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
}
