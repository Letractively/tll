package com.tll.client.data.rpc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.client.data.Payload;

/**
 * RpcCommandChain - Enables {@link IRpcCommand}s to be chained together for
 * execution.
 * @author jpk
 */
public final class RpcCommandChain implements IRpcCommand<Payload> {

	/**
	 * The list of commands to be executed.
	 */
	private final List<IRpcCommand<? extends Payload>> chain = new ArrayList<IRpcCommand<? extends Payload>>(3);

	/**
	 * The command iterator.
	 */
	private Iterator<IRpcCommand<? extends Payload>> cmdIterator;

	/**
	 * The current command being executed.
	 */
	private IRpcCommand<? extends Payload> currentCommand;

	/**
	 * Adds a command to be executed upon successful completion of this command.
	 * @param command The chain command to be executed.
	 */
	public void addCommand(IRpcCommand<? extends Payload> command) {
		chain.add(command);
	}

	public void setAsyncCallback(AsyncCallback<Payload> callback) {
		throw new UnsupportedOperationException("Chained RPC commands do not support setting the async callback");
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

}
