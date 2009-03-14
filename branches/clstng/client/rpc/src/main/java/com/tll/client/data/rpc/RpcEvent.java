/**
 * The Logic Lab
 * @author jpk Feb 23, 2008
 */
package com.tll.client.data.rpc;

import com.google.gwt.event.shared.GwtEvent;
import com.tll.common.data.Payload;

/**
 * RpcEvent
 * @author jpk
 * @param <P> the payload type
 */
public final class RpcEvent<P extends Payload> extends GwtEvent<IRpcHandler<P>> {

	/**
	 * Type
	 * @author jpk
	 */
	public static enum Type {
		/**
		 * An RPC command was just sent.
		 */
		SENT,
		/**
		 * An RPC command was successfull and was just received.
		 */
		RECEIVED,
		/**
		 * An RPC error occcurred.
		 */
		ERROR;
	}
	
	private final com.google.gwt.event.shared.GwtEvent.Type<IRpcHandler<P>> etype =
			new com.google.gwt.event.shared.GwtEvent.Type<IRpcHandler<P>>();

	private final Type type;

	/**
	 * The RPC payload.
	 */
	private final P payload;

	/**
	 * The RPC error.
	 */
	private final Throwable error;

	/**
	 * Constructor - Use for RPC send calls.
	 */
	public RpcEvent() {
		this.type = Type.SENT;
		this.payload = null;
		this.error = null;
	}

	/**
	 * Constructor - Use for successful RPC retrievals.
	 * @param payload The payload
	 */
	public RpcEvent(P payload) {
		this.type = Type.RECEIVED;
		this.payload = payload;
		this.error = null;
	}

	/**
	 * Constructor - Use for un-successful RPC calls.
	 * @param error The RPC error
	 */
	public RpcEvent(Throwable error) {
		this.type = Type.ERROR;
		this.payload = null;
		this.error = error;
	}

	@Override
	protected void dispatch(IRpcHandler<P> handler) {
		handler.onRpcEvent(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<IRpcHandler<P>> getAssociatedType() {
		return etype;
	}

	public Type getType() {
		return type;
	}

	public P getPayload() {
		return payload;
	}

	public Throwable getError() {
		return error;
	}
}
