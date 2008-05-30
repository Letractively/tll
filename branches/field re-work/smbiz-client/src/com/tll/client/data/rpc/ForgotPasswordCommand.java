/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.data.Payload;

/**
 * ForgotPasswordCommand
 * @author jpk
 */
public class ForgotPasswordCommand extends RpcCommand<Payload> {

	private static final IForgotPasswordServiceAsync svc;
	private final String emailAddress;

	static {
		svc = (IForgotPasswordServiceAsync) GWT.create(IForgotPasswordService.class);
		((ServiceDefTarget) svc).setServiceEntryPoint(App.getBaseUrl() + "rpc/adminForgotPassword");
	}

	/**
	 * Constructor
	 * @param emailAddress
	 */
	public ForgotPasswordCommand(Widget sourcingWidget, String emailAddress) {
		super(sourcingWidget);
		this.emailAddress = emailAddress;
	}

	@Override
	public void doExecute() {
		// rpc call to forgot password service
		svc.requestPassword(emailAddress, getAsyncCallback());
	}

}
