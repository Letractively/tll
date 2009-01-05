/**
 * The Logic Lab
 */
package com.tll.client.ui;

import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.data.rpc.ForgotPasswordCommand;
import com.tll.client.data.rpc.ISourcesUserSessionEvents;
import com.tll.client.data.rpc.IUserSessionListener;

/**
 * LoginDialog
 * @author jpk
 */
public class LoginDialog extends Dialog implements FormHandler, ClickListener, ISourcesUserSessionEvents {

	private final Label lblStatusMsg;
	private final FormPanel form;
	private final TextBox tbEmail;
	private final Label lblPswd;
	private final PasswordTextBox tbPswd;
	private final Button btnSubmit;
	private final SimpleHyperLink lnkTgl;

	private final UserSessionListenerCollection userSessionListeners = new UserSessionListenerCollection();

	/**
	 * Constructor
	 */
	public LoginDialog() {
		super();

		setText("Login");

		lblStatusMsg = new Label("");

		tbEmail = new TextBox();
		tbEmail.setName("j_username");
		tbEmail.setMaxLength(20);

		lblPswd = new Label("Password");
		tbPswd = new PasswordTextBox();
		tbPswd.setName("j_password");

		form = new FormPanel();
		form.setAction(App.getBaseUrl() + "j_acegi_security_check");
		form.setMethod(FormPanel.METHOD_POST);

		VerticalPanel vert = new VerticalPanel();
		vert.setSpacing(2);

		vert.add(lblStatusMsg);

		btnSubmit = new Button("Login", this);

		lnkTgl = new SimpleHyperLink("Forgot Password", this);
		lnkTgl.setTitle("Forgot Password");

		Grid grid = new Grid(3, 2);
		grid.setWidth("100%");
		grid.setCellSpacing(2);
		grid.setWidget(0, 0, new Label("Email Address:"));
		grid.setWidget(0, 1, tbEmail);
		grid.setWidget(1, 0, lblPswd);
		grid.setWidget(1, 1, tbPswd);
		grid.setWidget(2, 0, btnSubmit);
		grid.setWidget(2, 1, lnkTgl);
		vert.add(grid);

		form.addFormHandler(this);

		form.setWidget(vert);

		setWidget(form);
	}

	public void addUserSessionListener(IUserSessionListener listener) {
		userSessionListeners.add(listener);
	}

	public void removeUserSessionListener(IUserSessionListener listener) {
		userSessionListeners.remove(listener);
	}

	@Override
	public void center() {
		super.center();
		DeferredCommand.addCommand(new FocusCommand(tbEmail, true));
	}

	private boolean isLoginMode() {
		return "Forgot Password".equals(lnkTgl.getTitle());
	}

	public void onClick(Widget sender) {
		if(sender == btnSubmit) {
			if(isLoginMode()) {
				// DeferredCommand.addCommand(new FocusCommand(btnSubmit, false));
				setVisible(false);
				form.submit();
			}
			else {
				String emailAddress = tbEmail.getText();
				if(emailAddress.length() == 0) {
					lblStatusMsg.setText("Your email address must be specified for password retrieval.");
					return;
				}

				ForgotPasswordCommand fpc = new ForgotPasswordCommand(this, emailAddress);
				fpc.execute();
			}
		}
		else if(sender == lnkTgl) {
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

	public void onSubmit(FormSubmitEvent event) {
		StringBuffer msg = new StringBuffer(128);
		if(tbEmail.getText().length() == 0) {
			msg.append("Please specify your email address.");
			event.setCancelled(true);
		}
		if(tbPswd.getText().length() == 0) {
			msg.append("Please specify your password.");
			event.setCancelled(true);
		}
		if(event.isCancelled()) {
			setVisible(false);
		}
		lblStatusMsg.setText(msg.toString());
	}

	public void onSubmitComplete(FormSubmitCompleteEvent event) {
		String results = event.getResults();
		if(results == null || results.length() == 0) {
			// successful login
			userSessionListeners.fireLogin();
			tbEmail.setText(null);
			tbPswd.setText(null);
			return;
		}
		// unsuccessful login
		setVisible(true);
		lblStatusMsg.setText(event.getResults());
	}
}
