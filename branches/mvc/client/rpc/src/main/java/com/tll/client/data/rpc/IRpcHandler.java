/**
 * The Logic Lab
 * @author jpk
 * Sep 1, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.event.shared.EventHandler;
import com.tll.common.data.Payload;

/**
 * IRpcHandler - Listens to RPC related events.
 * @author jpk
 * @param <P> the payload type
 */
public interface IRpcHandler<P extends Payload> extends EventHandler {

	/**
	 * Fired when an RPC event occurrs.
	 * <p>
	 * <strong>NOTE: </strong>The rpc event conveys whether or not the RPC was
	 * successfull or not.
	 * @param event The rpc event
	 */
	void onRpcEvent(RpcEvent<P> event);
}
