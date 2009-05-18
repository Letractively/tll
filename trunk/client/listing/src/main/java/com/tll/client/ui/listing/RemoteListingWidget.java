/**
 * The Logic Lab
 * @author jpk
 * @since May 8, 2009
 */
package com.tll.client.ui.listing;

import com.google.gwt.event.shared.HandlerRegistration;
import com.tll.client.data.rpc.IHasRpcHandlers;
import com.tll.client.data.rpc.IRpcHandler;
import com.tll.client.data.rpc.RpcEvent;
import com.tll.client.listing.IListingConfig;
import com.tll.client.ui.RpcUiHandler;
import com.tll.common.model.Model;


/**
 * RemoteListingWidget
 * @author jpk
 */
public class RemoteListingWidget extends ModelListingWidget implements IHasRpcHandlers {

	/**
	 * Constructor
	 * @param config
	 */
	public RemoteListingWidget(IListingConfig<Model> config) {
		super(config);
		addRpcHandler(new RpcUiHandler(this));
	}

	@Override
	public HandlerRegistration addRpcHandler(IRpcHandler handler) {
		return addHandler(handler, RpcEvent.TYPE);
	}
}
