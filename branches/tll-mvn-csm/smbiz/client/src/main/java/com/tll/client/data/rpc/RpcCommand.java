/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.common.data.Payload;
import com.tll.common.data.Status;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * RpcCommand - Impl of {@link IRpcCommand} serving as a way to chain rpc calls
 * and to do app specific handling of rpc calls in a centralized manner.
 * <p>
 * ALL app RPC calls should be made via an extended {@link RpcCommand} instance
 * to monitor the number of calls made.
 * @author jpk
 * @param <P> payload type
 */
public abstract class RpcCommand<P extends Payload> implements IRpcCommand<P>, ISourcesRpcEvents {

	/**
	 * Tracks the number of RPC calls in process.
	 */
	private static int rpcCounter = 0;

	/**
	 * The RPC listeners. This serves as a generic way to issue notification on
	 * RPC return.
	 */
	private RpcListenerCollection rpcListeners;

	/**
	 * The declared ref is necessary in order to chain rpc commands.
	 */
	private AsyncCallback<P> callback = this;

	/**
	 * Constructor
	 * @throws IllegalArgumentException When the sourcing Widget is
	 *         <code>null</code>
	 */
	public RpcCommand() throws IllegalArgumentException {
		super();
	}

	/**
	 * @return The sourcing Widget which must <em>not</em> return
	 *         <code>null</code>.
	 */
	protected abstract Widget getSourcingWidget();

	public void addRpcListener(IRpcListener listener) {
		if(rpcListeners == null) {
			rpcListeners = new RpcListenerCollection();
		}
		rpcListeners.add(listener);
	}

	public void removeRpcListener(IRpcListener listener) {
		if(rpcListeners != null) {
			rpcListeners.remove(listener);
		}
	}

	protected AsyncCallback<P> getAsyncCallback() {
		return callback;
	}

	public void setAsyncCallback(AsyncCallback<P> callback) {
		this.callback = callback;
	}

	/**
	 * Adjusts the internal RPC counter and is reponsible for notifying the UI of
	 * the RPC status.
	 * @param sending
	 */
	static void rpc(boolean sending) {
		if(sending) {
			rpcCounter++;
			App.darkenBusyPanel();
			App.busy();
		}
		else {
			--rpcCounter;
			App.unbusy();
			App.lightenBusyPanel();
		}
	}

	/**
	 * Does the actual RPC execution.
	 */
	protected abstract void doExecute();

	public final void execute() {
		rpc(true);
		try {
			doExecute();
		}
		catch(Throwable t) {
			rpc(false);
			if(t instanceof RuntimeException) {
				throw (RuntimeException) t;
			}
		}
	}

	public final void onSuccess(P result) {
		rpc(false);
		handleSuccess(result);
	}

	public final void onFailure(Throwable caught) {
		rpc(false);
		handleFailure(caught);
	}

	/**
	 * May be overridden by sub-classes.
	 * @param result
	 */
	protected void handleSuccess(P result) {

		// fire RPC event
		if(rpcListeners != null) {
			rpcListeners.fireRpcEvent(new RpcEvent(getSourcingWidget(), result, false));
		}

		// fire status event
		StatusEventDispatcher.instance().fireStatusEvent(new StatusEvent(getSourcingWidget(), result.getStatus()));
	}

	/**
	 * May be overridden by sub-classes.
	 * @param caught
	 */
	protected void handleFailure(Throwable caught) {
		GWT.log("Error in rpc payload retrieval", caught);

		// fire RPC event
		if(rpcListeners != null) {
			rpcListeners.fireRpcEvent(new RpcEvent(getSourcingWidget(), caught, true));
		}

		// fire status event
		String msg = caught.getMessage();
		if(msg == null) msg = "An unknown RPC error occurred";
		final Status status = new Status(msg, MsgLevel.ERROR);
		StatusEventDispatcher.instance().fireStatusEvent(new StatusEvent(getSourcingWidget(), status));
	}

}
