/**
 * The Logic Lab
 * @author jpk
 * @since Apr 10, 2010
 */
package com.tll.client.ui;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.DialogBox;

/**
 * @author jpk
 */
public class LoginDialog extends DialogBox {

	final LoginPanel loginPanel;

	/**
	 * Constructor
	 */
	public LoginDialog() {
		this(new LoginPanel());
	}

	/**
	 * Constructor
	 * @param loginPanel
	 */
	public LoginDialog(LoginPanel loginPanel) {
		super();
		setGlassEnabled(true);
		loginPanel.addValueChangeHandler(new ValueChangeHandler<LoginPanel.Mode>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<LoginPanel.Mode> event) {
				if(event.getValue() == LoginPanel.Mode.LOGIN) {
					setTitle("Login");
				}
				else if(event.getValue() == LoginPanel.Mode.FORGOT_PASSWORD) {
					setTitle("Forgot Password");
				}
				center();
			}
		});
		this.loginPanel = loginPanel;
	}
	
	public LoginPanel getLoginPanel() {
		return loginPanel;
	}
}
