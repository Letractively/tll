/**
 * The Logic Lab
 * @author jpk
 * Aug 28, 2007
 */
package com.tll.client.admin.data.rpc;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.admin.data.rpc.IAdminContextListener.ChangeType;
import com.tll.client.data.rpc.IUserSessionListener;
import com.tll.client.data.rpc.RpcCommand;
import com.tll.common.admin.AdminContext;
import com.tll.common.admin.data.rpc.AdminContextPayload;
import com.tll.common.admin.data.rpc.IAdminContextService;
import com.tll.common.admin.data.rpc.IAdminContextServiceAsync;

/**
 * AdminContextCommand - RPC command to retrieve the admin context from the
 * server.
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public final class AdminContextCommand extends RpcCommand<AdminContextPayload> implements IUserSessionListener {

	private static final IAdminContextServiceAsync svc;
	static {
		svc = (IAdminContextServiceAsync) GWT.create(IAdminContextService.class);
		((ServiceDefTarget) svc).setServiceEntryPoint(GWT.getModuleBaseURL() + "rpc/adminContext");
	}

	private final ListenerCollection adminContextListeners = new ListenerCollection();
	private final Widget sourcingWidget;
	private ChangeType changeType;

	/**
	 * Constructor
	 * @param sourcingWidget
	 */
	public AdminContextCommand(Widget sourcingWidget) {
		super();
		this.sourcingWidget = sourcingWidget;
	}

	@Override
	protected Widget getSourcingWidget() {
		return sourcingWidget;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}

	public void addAdminContextListener(IAdminContextListener listener) {
		adminContextListeners.add(listener);
	}

	public void removeAdminContextListener(IAdminContextListener listener) {
		adminContextListeners.remove(listener);
	}

	@SuppressWarnings("serial")
	private static class ListenerCollection extends ArrayList<IAdminContextListener> {

		public void fire(AdminContext adminContext, ChangeType changeType) {
			for(IAdminContextListener listener : this) {
				listener.onAdminContextChange(adminContext, changeType);
			}
		}
	}

	@Override
	protected void doExecute() {
		if(changeType == null) {
			throw new IllegalStateException("changeType must be specified.");
		}
		svc.getAdminContext(getAsyncCallback());
	}

	@Override
	protected void handleSuccess(AdminContextPayload result) {
		super.handleSuccess(result);
		AdminContext ac = result.getAdminContext();
		adminContextListeners.fire(ac, changeType);
		changeType = null; // reset;
	}

	@Override
	protected void handleFailure(Throwable caught) {
		adminContextListeners.fire(null, ChangeType.INVALIDATE);
	}

	public void onLogin() {
		// no-op
	}

	public void onLogout() {
		adminContextListeners.fire(null, ChangeType.INVALIDATE);
	}

}
