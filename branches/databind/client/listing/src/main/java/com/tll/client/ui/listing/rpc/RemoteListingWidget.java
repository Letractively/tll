/**
 * The Logic Lab
 * @author jpk
 * @since May 8, 2009
 */
package com.tll.client.ui.listing.rpc;

import com.google.gwt.event.shared.HandlerRegistration;
import com.tll.client.data.rpc.IHasRpcHandlers;
import com.tll.client.data.rpc.IRpcHandler;
import com.tll.client.data.rpc.RpcEvent;
import com.tll.client.ui.RpcUiHandler;
import com.tll.client.ui.listing.ListingNavBar;
import com.tll.client.ui.listing.ModelListingTable;
import com.tll.client.ui.listing.ModelListingWidget;
import com.tll.common.model.Model;


/**
 * RemoteListingWidget
 * @param <T> table type
 * @author jpk
 */
public class RemoteListingWidget<T extends ModelListingTable> extends ModelListingWidget<T> implements IHasRpcHandlers {

	/**
	 * Constructor
	 * @param listingId 
	 * @param listingElementName 
	 * @param table
	 * @param navBar
	 */
	public RemoteListingWidget(String listingId, String listingElementName, T table, ListingNavBar<Model> navBar) {
		super(listingId, listingElementName, table, navBar);
		addRpcHandler(new RpcUiHandler(this));
	}

	@Override
	public HandlerRegistration addRpcHandler(IRpcHandler handler) {
		return addHandler(handler, RpcEvent.TYPE);
	}
}
