/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
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
public abstract class RpcCommand<P extends Payload> implements IRpcCommand<P> {

	/**
	 * The optional widget that will serve as the rpc event source.
	 */
	protected final Widget sourcingWidget;

	/**
	 * The declared ref is necessary in order to chain rpc commands.
	 */
	private AsyncCallback<P> callback = this;

	/**
	 * Constructor
	 */
	public RpcCommand() {
		this(null);
	}

	/**
	 * Constructor
	 * @param sourcingWidget If non-<code>null</code>, {@link RpcEvent}s will fire
	 *        on this widget.
	 */
	protected RpcCommand(Widget sourcingWidget) {
		this.sourcingWidget = sourcingWidget;
	}

	protected final AsyncCallback<P> getAsyncCallback() {
		return callback;
	}

	public final void setAsyncCallback(AsyncCallback<P> callback) {
		this.callback = callback;
	}

	/**
	 * Does the actual RPC execution.
	 */
	protected abstract void doExecute();

	public final void execute() {
		//rpc(true);
		try {
			doExecute();
			// fire an RPC send event
			if(sourcingWidget != null) sourcingWidget.fireEvent(new RpcEvent<P>());
		}
		catch(final Throwable t) {
			//rpc(false);
			if(t instanceof RuntimeException) {
				throw (RuntimeException) t;
			}
		}
	}

	public final void onSuccess(P result) {
		//rpc(false);
		handleSuccess(result);
	}

	public final void onFailure(Throwable caught) {
		//rpc(false);
		handleFailure(caught);
	}

	/**
	 * May be overridden by sub-classes.
	 * @param result
	 */
	protected void handleSuccess(P result) {
		if(sourcingWidget != null) {
			// fire RPC event
			sourcingWidget.fireEvent(new RpcEvent<P>(result));
			// fire status event
			StatusEventDispatcher.instance().fireStatusEvent(new StatusEvent(sourcingWidget, result.getStatus()));
		}
	}

	/**
	 * May be overridden by sub-classes.
	 * @param caught
	 */
	protected void handleFailure(Throwable caught) {
		GWT.log("Error in rpc payload retrieval", caught);

		if(sourcingWidget != null) {
			// fire RPC event
			sourcingWidget.fireEvent(new RpcEvent<P>(caught));

			// fire status event
			String msg = caught.getMessage();
			if(msg == null) msg = "An unknown RPC error occurred";
			final Status status = new Status(msg, MsgLevel.ERROR);
			StatusEventDispatcher.instance().fireStatusEvent(new StatusEvent(sourcingWidget, status));
		}
	}

}
