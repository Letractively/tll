/**
 * The Logic Lab
 * @author jpk
 * Aug 28, 2007
 */
package com.tll.client.rpc;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.tll.client.AdminContext;
import com.tll.client.data.rpc.RpcCommand;
import com.tll.client.rpc.IAdminContextListener.ChangeType;
import com.tll.client.ui.IUserSessionHandler;
import com.tll.client.ui.UserSessionEvent;
import com.tll.common.data.rpc.AdminContextPayload;
import com.tll.common.data.rpc.IAdminContextService;
import com.tll.common.data.rpc.IAdminContextServiceAsync;
import com.tll.common.model.ModelKey;

/**
 * AdminContextCommand - RPC command to retrieve the admin context from the
 * server.
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public final class AdminContextCommand extends RpcCommand<AdminContextPayload> implements IUserSessionHandler {

	private static final IAdminContextServiceAsync svc;
	static {
		svc = (IAdminContextServiceAsync) GWT.create(IAdminContextService.class);
	}

	/**
	 * The sole client-side admin context instance.
	 */
	private final AdminContext adminContext = new AdminContext();

	private final ListenerCollection listeners = new ListenerCollection();

	private ChangeType changeType;

	private ModelKey accountRef;

	/**
	 * Constructor
	 */
	public AdminContextCommand() {
		super();
	}

	/**
	 * @return The sole admin context instance.
	 */
	public AdminContext getAdminContext() {
		return adminContext;
	}

	/**
	 * Requests the initial admin context from the server.
	 */
	public void init() {
		changeType = ChangeType.USER_CHANGE;
		execute();
	}

	/**
	 * Requests a current account change on the server.
	 * @param acntRef
	 */
	public void changeCurrentAccount(ModelKey acntRef) {
		changeType = ChangeType.ACCOUNT_CHANGE;
		this.accountRef = acntRef;
		execute();
	}

	public void addAdminContextListener(IAdminContextListener listener) {
		listeners.add(listener);
	}

	public void removeAdminContextListener(IAdminContextListener listener) {
		listeners.remove(listener);
	}

	@SuppressWarnings("serial")
	private static class ListenerCollection extends ArrayList<IAdminContextListener> {

		public void fire(AdminContext adminContext, ChangeType changeType) {
			for(final IAdminContextListener listener : this) {
				listener.onAdminContextChange(adminContext, changeType);
			}
		}
	}

	@Override
	protected void doExecute() {
		if(changeType == ChangeType.USER_CHANGE) {
			svc.getAdminContext(getAsyncCallback());
		}
		else if(changeType == ChangeType.ACCOUNT_CHANGE) {
			svc.changeCurrentAccount(accountRef, getAsyncCallback());
		}
		else {
			throw new IllegalStateException("Unhandled change type");
		}
	}

	@Override
	protected void handleSuccess(AdminContextPayload result) {
		super.handleSuccess(result);
		switch(changeType) {
		case USER_CHANGE:
			adminContext.setDebug(result.isDebug());
			adminContext.setEnvironment(result.getEnvironment());
			adminContext.setUser(result.getUser());
			adminContext.setAccount(result.getAccount());
			if(adminContext.getUser() == null) {
				// presume not logged in yet
				changeType = ChangeType.INVALIDATE;
			}
			break;
		case ACCOUNT_CHANGE:
			adminContext.setAccount(result.getAccount());
			break;
		default:
			throw new IllegalStateException("Unhandled change type for rpc request: " + changeType);
		}
		DeferredCommand.addCommand(new Command() {

			@Override
			public void execute() {
				listeners.fire(adminContext, changeType);
				changeType = null; // reset
				accountRef = null;
			}
		});
	}

	@Override
	protected void handleFailure(Throwable caught) {
		super.handleFailure(caught);
		DeferredCommand.addCommand(new Command() {

			@Override
			public void execute() {
				listeners.fire(null, ChangeType.INVALIDATE);
				changeType = null; // reset
				accountRef = null;
			}
		});
	}

	public void onUserSessionEvent(UserSessionEvent event) {
		if(event.isStart()) {
			// login
			// no-op
		}
		else {
			// logout
			listeners.fire(null, ChangeType.INVALIDATE);
		}
	}
}
