package com.tll.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author jpk
 * @since Aug 22, 2012
 */
public class LoginPanel extends Composite implements ClickHandler, SubmitHandler {

	private static LoginPanel2UiBinder uiBinder = GWT.create(LoginPanel2UiBinder.class);

	interface LoginPanel2UiBinder extends UiBinder<Widget, LoginPanel> {
	}

	static enum Mode {
		LOGIN,
		FORGOT_PASSWORD,
	}

	Mode mode = Mode.LOGIN; // default

	@UiField
	DivElement title;
	@UiField
	DivElement statusMsg;

	@UiField
	LabelElement lblUsername;
	@UiField
	TextBox tbUsername;
	@UiField
	LabelElement lblPassword;
	@UiField
	TextBox tbPassword;
	@UiField
	Anchor lnkTgl; // toggles btwn view modes
	@UiField
	Button btnSubmit;
	@UiField
	FormPanel form;

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public LoginPanel() {
		tbUsername.getElement().setId(DOM.createUniqueId());
		tbPassword.getElement().setId(DOM.createUniqueId());
		initWidget(uiBinder.createAndBindUi(this));

	}

	void setViewMode(Mode mode) {
		switch(mode) {
			default:
			case LOGIN:
				statusMsg.setInnerText("");
				lblUsername.setInnerText(LoginStyles.constants().labelUsername());
				lblPassword.setInnerText(LoginStyles.constants().labelPassword());
				lblPassword.getStyle().setDisplay(Display.INLINE);
				tbPassword.setVisible(true);
				lnkTgl.setTitle(LoginStyles.constants().toResetPasswordText());
				lnkTgl.setText(LoginStyles.constants().toResetPasswordText());
				btnSubmit.setText(LoginStyles.constants().buttonLoginText());
				break;
			case FORGOT_PASSWORD:
				statusMsg.setInnerText(LoginStyles.constants().resetPasswordText());
				lblUsername.setInnerText(LoginStyles.constants().labelUsernameResetPassword());
				lblPassword.getStyle().setDisplay(Display.NONE);
				tbPassword.setVisible(false);
				lnkTgl.setTitle(LoginStyles.constants().toLoginText());
				lnkTgl.setText(LoginStyles.constants().toLoginText());
				btnSubmit.setText(LoginStyles.constants().buttonResetPasswordText());
				break;
		}
		this.mode = mode;
	}

	@Override
	public void onClick(ClickEvent event) {
		Object src = event.getSource();
		if(src == lnkTgl) {
			// toggle view model
			setViewMode(mode == Mode.LOGIN ? Mode.FORGOT_PASSWORD : Mode.LOGIN);
		} else if(src == btnSubmit) {
			if(mode == Mode.LOGIN) {
				// submit form
				form.submit();
			} else {
				// xhr to reset password
				String url = LoginStyles.constants().formResetPasswordTarget();
				RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
				StringBuilder sb = new StringBuilder();
				sb.append(tbUsername.getValue());
				sb.append('=');
				sb.append(tbUsername.getValue());
				String requestData = URL.encode(sb.toString());
				int timeoutMillis = Integer.parseInt(LoginStyles.constants().resetPasswordTimeoutMillis());
				builder.setTimeoutMillis(timeoutMillis);
				builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
				builder.setHeader("Content-Length", String.valueOf(requestData.length()));
				try {
					builder.sendRequest(requestData, new RequestCallback() {

						public void onError(Request request, Throwable exception) {
							// Couldn't connect to server (could be timeout, SOP violation, etc.)
							statusMsg.setInnerText(LoginStyles.constants().resetPasswordConnectionErrorMsg());
						}

						public void onResponseReceived(Request request, Response response) {
							if(200 == response.getStatusCode()) {
								// success
								Window.Location.replace(LoginStyles.constants().urlLoginSuccess());
							} else {
								// Handle the error. Can get the status text from
								statusMsg.setInnerText(response.getStatusText());
							}
						}
					});
				}
				catch(RequestException e) {
					// Couldn't connect to server
					statusMsg.setInnerText(LoginStyles.constants().resetPasswordConnectionErrorMsg());
				}
			}
		}
	}

	@Override
	public void onSubmit(SubmitEvent event) {
		final StringBuilder msg = new StringBuilder(128);
		if(tbUsername.getText().length() == 0) {
			msg.append("Please specify your ");
			msg.append(LoginStyles.constants().labelUsername());
			event.cancel();
		}
		if(tbPassword.getText().length() == 0) {
			msg.append("Please specify your ");
			msg.append(LoginStyles.constants().labelPassword());
			event.cancel();
		}
		if(event.isCanceled()) {
			setVisible(false);
		}
		statusMsg.setInnerText(msg.toString());
	}

}
