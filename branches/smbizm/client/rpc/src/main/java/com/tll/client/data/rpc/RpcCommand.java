/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.data.rpc;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.common.data.Payload;
import com.tll.common.data.Status;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * RpcCommand - Intended base class for all client-side RPC based requests.
 * @author jpk
 * @param <P> payload type
 */
public abstract class RpcCommand<P extends Payload> implements AsyncCallback<P>, IRpcCommand {

	private RpcEventCollection rpcHandlers;

	@Override
	public void addRpcHandler(IRpcHandler listener) {
		if(rpcHandlers == null) {
			rpcHandlers = new RpcEventCollection();
		}
		rpcHandlers.add(listener);
	}

	@Override
	public void removeRpcHandler(IRpcHandler listener) {
		if(rpcHandlers != null) {
			rpcHandlers.remove(listener);
		}
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		if(rpcHandlers != null && event.getAssociatedType() == RpcEvent.TYPE) {
			rpcHandlers.fire((RpcEvent) event);
		}
	}

	/**
	 * The declared ref is necessary in order to chain rpc commands.
	 */
	private AsyncCallback<P> callback = this;

	/**
	 * Constructor
	 */
	public RpcCommand() {
		super();
	}

	protected final AsyncCallback<P> getAsyncCallback() {
		return callback;
	}

	final void setAsyncCallback(AsyncCallback<P> callback) {
		this.callback = callback;
	}

	/**
	 * Does the actual RPC execution.
	 */
	protected abstract void doExecute();

	public final void execute() {
		try {
			doExecute();
			// fire an RPC send event
			fireEvent(new RpcEvent(RpcEvent.Type.SENT));
		}
		catch(final Throwable t) {
			fireEvent(new RpcEvent(RpcEvent.Type.SEND_ERROR));
			throw new RuntimeException(t.getMessage(), t);
		}
	}

	public final void onSuccess(P result) {
		handleSuccess(result);
	}

	public final void onFailure(Throwable caught) {
		handleFailure(caught);
	}

	/**
	 * May be overridden by sub-classes.
	 * @param result
	 */
	protected void handleSuccess(P result) {
		// fire RPC event
		fireEvent(new RpcEvent(RpcEvent.Type.RECEIVED));
		// fire status event
		StatusEventDispatcher.get().fireEvent(new StatusEvent(result.getStatus()));
	}

	/**
	 * May be overridden by sub-classes.
	 * @param caught
	 */
	protected void handleFailure(Throwable caught) {
		GWT.log("Error in rpc payload retrieval", caught);
		// fire RPC event
		fireEvent(new RpcEvent(RpcEvent.Type.ERROR));

		// fire status event
		String msg = caught.getMessage();
		if(msg == null) msg = "An unknown RPC error occurred";
		final Status status = new Status(msg, MsgLevel.ERROR);
		StatusEventDispatcher.get().fireEvent(new StatusEvent(status));
	}

}
