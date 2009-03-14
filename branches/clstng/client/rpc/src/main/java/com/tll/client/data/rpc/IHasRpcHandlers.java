/**
 * The Logic Lab
 * @author jpk Jan 3, 2008
 */
package com.tll.client.data.rpc;

import com.google.gwt.event.shared.HandlerRegistration;
import com.tll.common.data.Payload;


/**
 * IHasRpcHandlers
 * @author jpk
 * @param <P> the payload type
 */
public interface IHasRpcHandlers<P extends Payload> {

	/**
	 * Adds an rpc handler.
	 * @param handler
	 * @return handler registration
	 */
	HandlerRegistration addRpcHandler(IRpcHandler<P> handler);
}
