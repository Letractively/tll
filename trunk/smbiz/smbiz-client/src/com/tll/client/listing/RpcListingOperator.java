/**
 * The Logic Lab
 * @author jpk
 * Mar 30, 2008
 */
package com.tll.client.listing;

import com.tll.client.data.ListingOp;
import com.tll.client.data.PropKey;
import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.data.rpc.ListingCommand;
import com.tll.client.data.rpc.ListingCommand.IListingDefinition;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.search.ISearch;
import com.tll.client.ui.listing.AbstractListingWidget;
import com.tll.listhandler.SortDir;
import com.tll.listhandler.Sorting;

/**
 * RpcListingOperator - RPC listing operator. Issues listng commands to the
 * server. This is for search based listings.
 * @author jpk
 */
public final class RpcListingOperator extends AbstractListingOperator {

	/**
	 * The RPC listing command.
	 */
	private final ListingCommand cmd;

	/**
	 * The search criteria that dictates data retrieval from the server.
	 */
	private final ISearch criteria;

	/**
	 * ListingDefinition - Ad hoc {@link IListingDefinition} impl to avoid having
	 * to retain a memory ref to a possibly large {@link ListingConfig} instance.
	 * @author jpk
	 */
	private final class ListingDefinition implements IListingDefinition {

		private final Sorting defaultSorting;
		private final int listHandlerType;
		private final String listingName;
		private final int pageSize;
		private final PropKey[] propKeys;
		private final boolean sortable;

		/**
		 * Constructor
		 * @param listingConfig
		 * @param listingName
		 * @param listHandlerType
		 */
		public ListingDefinition(IListingConfig listingConfig, String listingName, int listHandlerType) {
			super();
			defaultSorting = listingConfig.getDefaultSorting();
			this.listHandlerType = listHandlerType;
			this.listingName = listingName;
			pageSize = listingConfig.getPageSize();
			propKeys = listingConfig.getPropKeys();
			sortable = listingConfig.isSortable();
		}

		public Sorting getDefaultSorting() {
			return defaultSorting;
		}

		public int getListHandlerType() {
			return listHandlerType;
		}

		public String getListingName() {
			return listingName;
		}

		public int getPageSize() {
			return pageSize;
		}

		public PropKey[] getPropKeys() {
			return propKeys;
		}

		public boolean isSortable() {
			return sortable;
		}
	}

	/**
	 * Constructor
	 * @param listingWidget
	 * @param listingConfig
	 * @param listHandlerType
	 * @param criteria
	 */
	public RpcListingOperator(AbstractListingWidget listingWidget, final IListingConfig listingConfig,
			final int listHandlerType, final ISearch criteria) {
		super(listingWidget);
		String listingName = Integer.toString(criteria.hashCode());
		cmd = new ListingCommand(listingWidget, new ListingDefinition(listingConfig, listingName, listHandlerType));
		this.criteria = criteria;
		cmd.addListingListener(listingWidget);
	}

	public void refresh() {
		cmd.list(criteria, true);
		cmd.execute();
	}

	public void display() {
		cmd.list(criteria, false);
		cmd.execute();
	}

	public void navigate(ClientListingOp navAction, Integer page) {
		switch(navAction) {
			case LAST_PAGE:
				cmd.navigate(ListingOp.LAST_PAGE, page);
				break;
			case NEXT_PAGE:
				cmd.navigate(ListingOp.NEXT_PAGE, page);
				break;
			case PREVIOUS_PAGE:
				cmd.navigate(ListingOp.PREVIOUS_PAGE, page);
				break;
			case GOTO_PAGE:
				cmd.navigate(ListingOp.GOTO_PAGE, page);
				break;
			default:
			case FIRST_PAGE:
				cmd.navigate(ListingOp.FIRST_PAGE, page);
				break;
		}
		cmd.execute();
	}

	public void sort(String colName, SortDir direction) {
		cmd.sort(colName, direction);
		cmd.execute();
	}

	public void clear() {
		cmd.clear();
		cmd.execute();
	}

	public void insertRow(int rowIndex, Model rowData) {
		throw new UnsupportedOperationException("RPC listing operators do not support row insertion.");
	}

	public void updateRow(int rowIndex, Model rowData) {
		throw new UnsupportedOperationException("RPC listing operators do not support row updating.");
	}

	/**
	 * We rely on the model change event dispatching system to subsequently update
	 * the listing.
	 */
	public void deleteRow(int rowIndex) {
		RefKey rowRef = listingWidget.getRowRef(rowIndex);
		assert rowRef != null && rowRef.isSet();

		CrudCommand cc = new CrudCommand(listingWidget);
		cc.purge(rowRef);
		cc.execute();
	}

}
