package com.tll.client.admin;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.tll.client.App;
import com.tll.client.admin.data.rpc.AdminContextCommand;
import com.tll.client.admin.event.IAdminContextListener;
import com.tll.client.admin.mvc.view.AspMain;
import com.tll.client.admin.mvc.view.CustomerMain;
import com.tll.client.admin.mvc.view.IspMain;
import com.tll.client.admin.mvc.view.MerchantMain;
import com.tll.client.admin.mvc.view.account.CustomerListingView;
import com.tll.client.admin.mvc.view.account.IspListingView;
import com.tll.client.admin.mvc.view.account.MerchantListingView;
import com.tll.client.admin.mvc.view.intf.InterfacesView;
import com.tll.client.admin.mvc.view.user.UserEditView;
import com.tll.client.admin.ui.MainPanel;
import com.tll.client.event.IUserSessionListener;
import com.tll.client.model.ModelChangeManager;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.LoginDialog;

/**
 * Smbiz Admin module.
 */
public final class SmbizAdmin implements EntryPoint, IAdminContextListener, IUserSessionListener {

	private static AdminContext adminContext;

	public static AdminContext getAdminContext() {
		return adminContext;
	}

	private AdminContextCommand acc;

	private LoginDialog loginDialog;
	private MainPanel mainPanel;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		App.init();

		// try/catch is necessary here because GWT.setUncaughtExceptionHandler()
		// will work not until onModuleLoad() has returned
		try {
			// set the main model change listener so model change events trickle into
			// the views
			ModelChangeManager.instance().addModelChangeListener(ViewManager.instance());

			// declare the available views
			populateViewClasses();

			// pre-build the main panel
			buildMainPanel();
			mainPanel.setVisible(false);

			// get the admin context from the server
			assert acc != null;
			acc.setChangeType(IAdminContextListener.USER_CHANGE);
			acc.execute();
		}
		catch(RuntimeException e) {
			GWT.log("Error in 'onModuleLoad()' method", e);
			e.printStackTrace();
		}
	}

	/**
	 * Sets all defined {@link ViewClass}es for later factory method instantiation
	 * of the associated views.
	 */
	private void populateViewClasses() {
		ViewClass.addClass(AspMain.klas);
		ViewClass.addClass(IspMain.klas);
		ViewClass.addClass(MerchantMain.klas);
		ViewClass.addClass(CustomerMain.klas);
		ViewClass.addClass(IspListingView.klas);
		ViewClass.addClass(MerchantListingView.klas);
		ViewClass.addClass(CustomerListingView.klas);
		ViewClass.addClass(InterfacesView.klas);
		ViewClass.addClass(UserEditView.klas);
	}

	public void onAdminContextChange(AdminContext ac, ChangeType changeType) {
		// set the admin context instance
		adminContext = ac;

		final boolean shouldLogin = (changeType == IAdminContextListener.INVALIDATE);
		mainPanel.setVisible(!shouldLogin);
		if(shouldLogin) {
			buildLoginDialog();
			loginDialog.center();
		}
		else if(loginDialog != null) {
			loginDialog.hide();
		}
	}

	private void buildLoginDialog() {
		if(loginDialog == null) {
			loginDialog = new LoginDialog();
			loginDialog.addUserSessionListener(this);
		}
	}

	private void buildMainPanel() {
		if(mainPanel == null) {
			mainPanel = new MainPanel();
			if(acc == null) {
				acc = new AdminContextCommand(mainPanel);
				acc.addAdminContextListener(this);
				acc.addAdminContextListener(mainPanel);
			}
			mainPanel.addUserSessionListener(this);
			mainPanel.addUserSessionListener(acc);
			RootPanel.get().add(mainPanel);
		}
	}

	public void onLogin() {
		// hide the login dialog
		if(loginDialog != null) {
			loginDialog.hide();
		}

		// show the main panel
		buildMainPanel();
		mainPanel.setVisible(true);

		// get the admin context from the server
		acc.setChangeType(IAdminContextListener.USER_CHANGE);
		acc.execute();
	}

	public void onLogout() {
		// IMPT: reset the history to ensure the history token will always be
		// different
		// than subsequent view tokens to ensure the Center panel is propertly
		// populated
		History.newItem(App.INITIAL_HISTORY_TOKEN);
	}

}
