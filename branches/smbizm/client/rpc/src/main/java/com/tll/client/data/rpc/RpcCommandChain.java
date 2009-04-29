package com.tll.client.data.rpc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.common.data.Payload;

/**
 * RpcCommandChain - Enables {@link IRpcCommand}s to be chained together for
 * execution.
 * @author jpk
 */
public final class RpcCommandChain implements AsyncCallback<Payload>, IRpcCommand {

	/**
	 * The list of commands to be executed.
	 */
	private final List<RpcCommand<Payload>> chain = new ArrayList<RpcCommand<Payload>>(3);

	/**
	 * The command iterator.
	 */
	private Iterator<RpcCommand<Payload>> cmdIterator;

	/**
	 * The current command being executed.
	 */
	private RpcCommand<Payload> currentCommand;

	/**
	 * Adds a command to be executed upon successful completion of this command.
	 * @param command The chain command to be executed.
	 */
	public void addCommand(RpcCommand<Payload> command) {
		chain.add(command);
	}

	@SuppressWarnings("unchecked")
	private void executeNextCommand() {
		assert cmdIterator != null;
		if(cmdIterator.hasNext()) {
			currentCommand = cmdIterator.next();
			((RpcCommand) currentCommand).setAsyncCallback(this);
			currentCommand.execute();
			((RpcCommand) currentCommand).setAsyncCallback(currentCommand);
		}
		else {
			cmdIterator = null;
		}
	}

	public void execute() {
		if(cmdIterator == null) {
			cmdIterator = chain.iterator();
		}
		executeNextCommand();
	}

	@SuppressWarnings("unchecked")
	public void onSuccess(Payload result) {
		((RpcCommand) currentCommand).onSuccess(result);
		currentCommand = null;
		executeNextCommand();
	}

	public void onFailure(Throwable caught) {
		// halt
		currentCommand.onFailure(caught);
		currentCommand = null;
		cmdIterator = null;
	}

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
}
