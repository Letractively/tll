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

	private final Type type;

	/**
	 * The RPC payload.
	 */
	private final Payload payload;

	/**
	 * The RPC error.
	 */
	private final Throwable error;

	/**
	 * Constructor - Use for RPC send calls.
	 * @param source
	 */
	public RpcEvent(Widget source) {
		super(source);
		this.type = Type.SENT;
		this.payload = null;
		this.error = null;
	}

	/**
	 * Constructor - Use for successful RPC retrievals.
	 * @param source
	 * @param payload The payload
	 */
	public RpcEvent(Widget source, Payload payload) {
		super(source);
		this.type = Type.RECEIVED;
		this.payload = payload;
		this.error = null;
	}

	/**
	 * Constructor - Use for un-successful RPC calls.
	 * @param source
	 * @param error The RPC error
	 */
	public RpcEvent(Widget source, Throwable error) {
		super(source);
		this.type = Type.ERROR;
		this.payload = null;
		this.error = error;
	}
	
	public Type getType() {
		return type;
	}

	public Payload getPayload() {
		return payload;
	}
	
	public Throwable getError() {
		return error;
	}
}
