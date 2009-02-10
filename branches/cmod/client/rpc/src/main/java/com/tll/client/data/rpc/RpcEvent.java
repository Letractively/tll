/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.data.rpc;

import java.util.EventObject;

import com.google.gwt.user.client.ui.Widget;
import com.tll.common.data.Payload;

/**
 * RpcEvent
 * @author jpk
 */
@SuppressWarnings("serial")
public final class RpcEvent extends EventObject {

	/**
	 * The RPC result
	 */
	private final Object result;

	/**
	 * Was there an RPC error?
	 */
	private final boolean rpcError;

	/**
	 * Constructor
	 * @param source
	 * @param result
	 * @param rpcError
	 */
	public RpcEvent(Widget source, Object result, boolean rpcError) {
		super(source);
		this.result = result;
		this.rpcError = rpcError;
	}

	public Object getResult() {
		return result;
	}

	public Payload getPayload() {
		return (result instanceof Payload) ? (Payload) result : null;
	}

	public boolean isRpcError() {
		return rpcError;
	}

}
