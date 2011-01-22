/**
 * The Logic Lab
 * @author jpk
 * @since May 8, 2009
 */
package com.tll.client.ui.listing;

import com.google.gwt.event.shared.HandlerRegistration;
import com.tll.IMarshalable;
import com.tll.client.data.rpc.IHasRpcHandlers;
import com.tll.client.data.rpc.IRpcHandler;
import com.tll.client.data.rpc.RpcEvent;

/**
 * RemoteListingWidget
 * @param <R> row data type
 * @param <T> table type
 * @author jpk
 */
public class RemoteListingWidget<R extends IMarshalable, T extends ListingTable<R>> extends ListingWidget<R, T> implements IHasRpcHandlers {

	/**
	 * Constructor
	 * @param listingId
	 * @param listingElementName
	 * @param table
	 * @param navBar
	 */
	public RemoteListingWidget(String listingId, String listingElementName, T table, ListingNavBar<R> navBar) {
		super(listingId, listingElementName, table, navBar);
	}

	@Override
	public HandlerRegistration addRpcHandler(IRpcHandler handler) {
		return addHandler(handler, RpcEvent.TYPE);
	}
}
