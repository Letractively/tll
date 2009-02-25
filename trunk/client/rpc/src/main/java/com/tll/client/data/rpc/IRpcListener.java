/**
 * The Logic Lab
 * @author jpk
 * Sep 1, 2007
 */
package com.tll.client.data.rpc;

import java.util.EventListener;


/**
 * IRpcListener - Listens to RPC related events.
 * @author jpk
 */
public interface IRpcListener extends EventListener {

	/**
	 * Fired when an RPC event occurrs.
	 * <p>
	 * <strong>NOTE: </strong>The rpc event conveys whether or not the RPC was
	 * successfull or not.
	 * @param event The rpc event
	 */
	void onRpcEvent(RpcEvent event);
}
