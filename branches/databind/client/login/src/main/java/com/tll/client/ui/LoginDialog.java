/**
 * The Logic Lab
 */
package com.tll.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.tll.client.data.rpc.ForgotPasswordCommand;
import com.tll.client.data.rpc.IHasRpcHandlers;
import com.tll.client.data.rpc.IRpcHandler;
import com.tll.client.data.rpc.RpcEvent;

/**
 * LoginDialog
 * @author jpk
 */
public class LoginDialog extends Dialog implements SubmitHandler, SubmitCompleteHandler, ClickHandler, IHasUserSessionHandlers, IHasRpcHandlers {

	private final Label lblStatusMsg;
	private final FormPanel form;
	private final TextBox tbEmail;
	private final Label lblPswd;
	private final PasswordTextBox tbPswd;
	private final Button btnSubmit;
	private final SimpleHyperLink lnkTgl;

	/**
	 * Constructor
	 * <p>
	 * Default, Spring-Security based, form field names and form action.
	 */
	public LoginDialog() {
		this("j_username", "j_password", GWT.getModuleBaseURL() + "j_acegi_security_check");
	}

	/**
	 * Constructor
	 * <p>
	 * Uses default username/password field names.
	 * @param formAction where to send login form submission.
	 */
	public LoginDialog(String formAction) {
		this("j_username", "j_password", formAction);
	}

	/**
	 * Constructor
	 * @param fldUsername name of the username field
	 * @param fldPassword name of the password field
	 * @param formAction path to which the form is submitted
	 */
	public LoginDialog(String fldUsername, String fldPassword, String formAction) {
		super();

		setText("Login");

		lblStatusMsg = new Label("");

		tbEmail = new TextBox();
		tbEmail.setName(fldUsername);
		tbEmail.setMaxLength(20);

		lblPswd = new Label("Password");
		tbPswd = new PasswordTextBox();
		tbPswd.setName(fldPassword);

		form = new FormPanel();
		form.setAction(formAction);
		form.setMethod(FormPanel.METHOD_POST);

		final VerticalPanel vert = new VerticalPanel();
		vert.setSpacing(2);

		vert.add(lblStatusMsg);

		btnSubmit = new Button("Login", this);

		lnkTgl = new SimpleHyperLink("Forgot Password", this);
		lnkTgl.setTitle("Forgot Password");

		final Grid grid = new Grid(3, 2);
		grid.setWidth("100%");
		grid.setCellSpacing(2);
		grid.setWidget(0, 0, new Label("Email Address:"));
		grid.setWidget(0, 1, tbEmail);
		grid.setWidget(1, 0, lblPswd);
		grid.setWidget(1, 1, tbPswd);
		grid.setWidget(2, 0, btnSubmit);
		grid.setWidget(2, 1, lnkTgl);
		vert.add(grid);

		form.addSubmitHandler(this);
		form.addSubmitCompleteHandler(this);

		form.setWidget(vert);

		setWidget(form);

		addRpcHandler(new RpcUiHandler(this));
	}

	@Override
	public HandlerRegistration addUserSessionHandler(IUserSessionHandler handler) {
		return addHandler(handler, UserSessionEvent.TYPE);
	}

	@Override
	public void center() {
		super.center();
		DeferredCommand.addCommand(new FocusCommand(tbEmail, true));
	}

	private boolean isLoginMode() {
		return "Forgot Password".equals(lnkTgl.getTitle());
	}

	@Override
	public HandlerRegistration addRpcHandler(IRpcHandler handler) {
		return addHandler(handler, RpcEvent.TYPE);
	}

	public void onClick(ClickEvent event) {
		if(event.getSource() == btnSubmit) {
			if(isLoginMode()) {
				// DeferredCommand.addCommand(new FocusCommand(btnSubmit, false));
				setVisible(false);
				form.submit();
			}
			else {
				final String emailAddress = tbEmail.getText();
				if(emailAddress.length() == 0) {
					lblStatusMsg.setText("Your email address must be specified for password retrieval.");
					return;
				}

				final ForgotPasswordCommand fpc = new ForgotPasswordCommand(emailAddress);
				fpc.setSource(this);
				fpc.execute();
			}
		}
		else if(event.getSource() == lnkTgl) {
			if(!isLoginMode()) {
				// to login mode
				lblStatusMsg.setText(null);
				lblPswd.setVisible(true);
				tbPswd.setVisible(true);
				lnkTgl.setTitle("Forgot Password");
				lnkTgl.setText("Forgot Password");
				btnSubmit.setText("Login");
				setText("Login");
			}
			else {
				// to forgot password mode
				lblStatusMsg.setText("Please specify your email address and your password will be emailed to you.");
				lblPswd.setVisible(false);
				tbPswd.setVisible(false);
				lnkTgl.setTitle("Back to Login");
				lnkTgl.setText("Back to Login");
				btnSubmit.setText("Email Password");
				setText("Forgot Password");
			}
			center();
		}
	}

	public void onSubmit(SubmitEvent event) {
		final StringBuilder msg = new StringBuilder(128);
		if(tbEmail.getText().length() == 0) {
			msg.append("Please specify your email address.");
			event.cancel();
		}
		if(tbPswd.getText().length() == 0) {
			msg.append("Please specify your password.");
			event.cancel();
		}
		if(event.isCanceled()) {
			setVisible(false);
		}
		lblStatusMsg.setText(msg.toString());
	}

	public void onSubmitComplete(SubmitCompleteEvent event) {
		final String results = event.getResults();
		if(results == null || results.length() == 0) {
			// successful login

			// userSessionListeners.fireLogin();
			fireEvent(new UserSessionEvent(true));

			tbEmail.setText(null);
			tbPswd.setText(null);
			return;
		}
		// unsuccessful login
		setVisible(true);
		lblStatusMsg.setText(event.getResults());
	}
}
