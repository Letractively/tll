package com.tll.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.tll.client.mvc.view.AspMain;
import com.tll.client.mvc.view.CustomerMain;
import com.tll.client.mvc.view.IspMain;
import com.tll.client.mvc.view.MerchantMain;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.mvc.view.account.CustomerListingView;
import com.tll.client.mvc.view.account.IspListingView;
import com.tll.client.mvc.view.account.MerchantListingView;
import com.tll.client.mvc.view.intf.AccountInterfaceView;
import com.tll.client.mvc.view.intf.InterfacesView;
import com.tll.client.mvc.view.user.UserEditView;
import com.tll.client.rpc.AdminContextCommand;
import com.tll.client.rpc.IAdminContextListener;
import com.tll.client.ui.LoginDialog;
import com.tll.client.ui.MainPanel;
import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.model.AdminRole;

/**
 * Smbiz Admin module.
 */
public final class SmbizAdmin implements EntryPoint, IAdminContextListener {

	/**
	 * The sole admin context command instance.
	 */
	private static final AdminContextCommand acc = new AdminContextCommand();

	/**
	 * @return The sole admin context command instance for the client app.
	 */
	public static AdminContextCommand getAdminContextCmd() {
		return acc;
	}

	/**
	 * Resolves whether or not the given account ref is able to be set as the
	 * current admin context account based on checking the current user's admin
	 * role held in the admin context.
	 * @param accountRef the desired account to be set as current
	 * @param parentAccountRef the parent account of the given
	 *        <code>accountRef</code> which is conditionally necessary
	 * @return true/false
	 * @throws IllegalArgumentException When the account ref param is
	 *         <code>null</code> or when it is non-<code>null</code> but the
	 *         parent account ref param is <code>null</code> and is necessary to
	 *         make a decision.
	 */
	public static boolean canSetAsCurrent(ModelKey accountRef, ModelKey parentAccountRef) throws IllegalArgumentException {
		if(accountRef == null || !accountRef.isSet()) throw new IllegalArgumentException("Null or unset account ref");
		final AdminContext ac = getAdminContextCmd().getAdminContext();
		final AdminRole role = ac.getUserRole();
		final SmbizEntityType targetAccountType = (SmbizEntityType) accountRef.getEntityType();
		final ModelKey userAcntRef = ac.getUserAccount().getKey();
		assert userAcntRef != null;
		switch(targetAccountType) {
		case ASP:
		case ISP:
			return (role == AdminRole.ASP);
		case MERCHANT:
			switch(role) {
			case ASP:
				return true;
			case ISP:
				// verify the user is parent to the given merchant
				return userAcntRef.equals(parentAccountRef);
			}
		}
		// default
		return false;
	}

	private LoginDialog loginDialog;
	private MainPanel mainPanel;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		App.init();

		DeferredCommand.addCommand(new Command() {

			@SuppressWarnings("synthetic-access")
			public void execute() {
				// declare the available views
				populateViewClasses();

				// pre-build the main panel
				buildMainPanel();
				mainPanel.setVisible(false);

				// get the admin context from the server
				assert acc != null;
				acc.setSource(mainPanel);
				acc.init();
			}
		});
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
		ViewClass.addClass(AccountInterfaceView.klas);
	}

	public void onAdminContextChange(AdminContext ac, ChangeType changeType) {
		final boolean shouldLogin = (changeType == ChangeType.INVALIDATE);
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
			loginDialog.getLoginPanel().addUserSessionHandler(acc);
		}
	}

	private void buildMainPanel() {
		if(mainPanel == null) {
			mainPanel = new MainPanel();
			acc.addAdminContextListener(this);
			acc.addAdminContextListener(mainPanel);
			mainPanel.addUserSessionHandler(acc);
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
		acc.init();
	}

	public void onLogout() {
		// IMPT: reset the history to ensure the history token will always be
		// different
		// than subsequent view tokens to ensure the Center panel is propertly
		// populated
		History.newItem(App.INITIAL_HISTORY_TOKEN);
	}
}
