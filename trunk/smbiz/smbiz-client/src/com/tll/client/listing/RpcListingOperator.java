/**
 * The Logic Lab
 * @author jpk
 * Mar 30, 2008
 */
package com.tll.client.listing;

import com.tll.client.data.ListingOp;
import com.tll.client.data.rpc.ListingCommand;
import com.tll.client.event.IListingListener;
import com.tll.client.event.type.ListingEvent;
import com.tll.client.search.ISearch;
import com.tll.listhandler.ListHandlerType;
import com.tll.listhandler.SortColumn;
import com.tll.listhandler.Sorting;

/**
 * RpcListingOperator - RPC listing operator. Issues listng commands to the
 * server. This is for search based listings.
 * @author jpk
 */
public final class RpcListingOperator<S extends ISearch> extends AbstractListingOperator implements IListingListener {

	private final String listingName;
	private final ListHandlerType listHandlerType;
	private final int pageSize;
	private final String[] props;
	private final S searchCriteria;
	private final Sorting sorting;

	/**
	 * Constructor
	 * @param listingWidget
	 * @param listingName The unique server-side listing name.
	 * @param listHandlerType The server side list handling strategy
	 * @param pageSize The desired page size
	 * @param props The desired properties the server shall provide as page data
	 * @param searchCriteria The search criteria used to generate the listing data
	 *        on the server
	 * @param sorting The sorting directive. May be <code>null</code>
	 */
	public RpcListingOperator(String listingName, ListHandlerType listHandlerType, int pageSize, String[] props,
			S searchCriteria, Sorting sorting) {
		super();
		this.listingName = listingName;
		this.listHandlerType = listHandlerType;
		this.pageSize = pageSize;
		this.props = props;
		this.searchCriteria = searchCriteria;
		this.sorting = sorting;
	}

	public void refresh() {
		ListingCommand<S> cmd = new ListingCommand<S>(listingWidget, listingName);
		cmd.addListingListener(this);
		cmd.list(listHandlerType, pageSize, props, searchCriteria, sorting, true);
		cmd.execute();
	}

	public void display() {
		ListingCommand<S> cmd = new ListingCommand<S>(listingWidget, listingName);
		cmd.addListingListener(this);
		cmd.list(listHandlerType, pageSize, props, searchCriteria, sorting, false);
		cmd.execute();
	}

	public void navigate(ListingOp navAction, Integer page) {
		ListingCommand<S> cmd = new ListingCommand<S>(listingWidget, listingName);
		cmd.addListingListener(this);
		cmd.navigate(navAction, page);
		cmd.execute();
	}

	public void sort(SortColumn sortColumn) {
		ListingCommand<S> cmd = new ListingCommand<S>(listingWidget, listingName);
		cmd.addListingListener(this);
		cmd.sort(sortColumn);
		cmd.execute();
	}

	public void clear() {
		ListingCommand<S> cmd = new ListingCommand<S>(listingWidget, listingName);
		cmd.addListingListener(this);
		cmd.clear();
		cmd.execute();
	}

	public void onListingEvent(ListingEvent event) {
		if(event.isSuccess()) {
			if(event.getPage() != null) {
				listingWidget.setPage(event.getPage(), event.getSorting());
			}
		}
	}
}
