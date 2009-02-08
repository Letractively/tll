/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.common.data.Payload;
import com.tll.common.data.rpc.IForgotPasswordService;
import com.tll.common.data.rpc.IForgotPasswordServiceAsync;

/**
 * ForgotPasswordCommand
 * @author jpk
 */
public final class ForgotPasswordCommand extends RpcCommand<Payload> {

	private static final IForgotPasswordServiceAsync svc;
	static {
		svc = (IForgotPasswordServiceAsync) GWT.create(IForgotPasswordService.class);
		((ServiceDefTarget) svc).setServiceEntryPoint(App.getBaseUrl() + "rpc/adminForgotPassword");
	}

	private final Widget sourcingWidget;
	private final String emailAddress;

	/**
	 * Constructor
	 * @param sourcingWidget
	 * @param emailAddress
	 */
	public ForgotPasswordCommand(Widget sourcingWidget, String emailAddress) {
		super();
		this.sourcingWidget = sourcingWidget;
		this.emailAddress = emailAddress;
	}

	@Override
	protected Widget getSourcingWidget() {
		return sourcingWidget;
	}

	@Override
	public void doExecute() {
		// rpc call to forgot password service
		svc.requestPassword(emailAddress, getAsyncCallback());
	}

}
