/**
 * The Logic Lab
 * @author jpk
 * @since Mar 16, 2009
 */
package com.tll.client.ui.listing;

import com.tll.client.data.rpc.IRpcHandler;
import com.tll.client.data.rpc.RpcEvent;
import com.tll.client.listing.IListingConfig;
import com.tll.common.data.ListingPayload;
import com.tll.common.model.Model;

/**
 * RemoteListingWidget - listing widget that glass panels the table while
 * listing rpc calls are in process.
 * @author jpk
 */
public class RemoteListingWidget extends ModelListingWidget implements
		IRpcHandler<ListingPayload<Model>> {

	/**
	 * Constructor
	 * @param config
	 */
	public RemoteListingWidget(IListingConfig<Model> config) {
		super(config);
		addHandler(this, RpcEvent.TYPE);
	}

	@Override
	public void onRpcEvent(RpcEvent<ListingPayload<Model>> event) {
		assert event.getSource() == this;
		switch(event.getType()) {
			case SENT:
				// TODO: trigger glass panel
				break;
			case RECEIVED:
				// TODO: remove glass panel
				break;
			case ERROR:
				break;
		}
	}
}
