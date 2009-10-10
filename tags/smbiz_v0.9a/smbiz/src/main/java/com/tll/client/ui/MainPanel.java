/**
 * The Logic Lab
 */
package com.tll.client.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.tll.client.AdminContext;
import com.tll.client.App;
import com.tll.client.OpsManager;
import com.tll.client.SmbizAdmin;
import com.tll.client.data.rpc.ISourcesUserSessionEvents;
import com.tll.client.data.rpc.IStatusHandler;
import com.tll.client.data.rpc.IUserSessionListener;
import com.tll.client.data.rpc.RpcEvent;
import com.tll.client.data.rpc.StatusEvent;
import com.tll.client.data.rpc.StatusEventDispatcher;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.EditViewInitializer;
import com.tll.client.mvc.view.IViewInitializer;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.MainView.MainViewClass;
import com.tll.client.mvc.view.account.AccountEditView;
import com.tll.client.mvc.view.user.UserEditView;
import com.tll.client.rpc.IAdminContextListener;
import com.tll.client.ui.msg.Msgs;
import com.tll.client.ui.option.IOptionHandler;
import com.tll.client.ui.option.OptionEvent;
import com.tll.client.ui.option.OptionsPanel;
import com.tll.client.ui.view.ViewLink;
import com.tll.client.ui.view.ViewPathPanel;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;
import com.tll.common.data.Status;
import com.tll.common.model.Model;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgAttr;

/**
 * MainPanel
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public final class MainPanel extends Composite implements IAdminContextListener, ISourcesUserSessionEvents {

	/**
	 * Styles - (admin.css)
	 * @author jpk
	 */
	private static final class Styles {

		/**
		 * Style for the main panel panel.
		 */
		public static final String MAIN = "sb-mainPanel";

		/**
		 * Style for the header panel.
		 */
		public static final String HEADER = "sb-Header";

		/**
		 * Style for the right nav panel.
		 */
		public static final String RIGHT_NAV = "sb-RightNav";

		/**
		 * Style for the center panel.
		 */
		public static final String CENTER = "sb-Center";

		/**
		 * Style for the footer panel.
		 */
		public static final String FOOTER = "sb-Footer";

		/**
		 * Style for the footer panel.
		 */
		public static final String DISCLOSURE_PANEL = "sb-dp";
	} // Styles

	private final DockPanel dockPanel = new DockPanel();
	private final Header header = new Header();
	private final ViewPathPanel viewpath = new ViewPathPanel(4);
	private final RightNav rightNav = new RightNav();
	private final Footer footer = new Footer();
	private final Center center = new Center();

	private final UserSessionListenerCollection userSessionListeners = new UserSessionListenerCollection();

	/**
	 * Constructor
	 */
	public MainPanel() {
		super();

		dockPanel.add(header, DockPanel.NORTH);
		dockPanel.add(viewpath, DockPanel.NORTH);
		dockPanel.add(footer, DockPanel.SOUTH);
		dockPanel.add(rightNav, DockPanel.EAST);
		dockPanel.add(center, DockPanel.CENTER);
		dockPanel.setStylePrimaryName(Styles.MAIN);
		dockPanel.getElement().setAttribute("align", "center");

		final Element headerTd = header.getElement().getParentElement();
		final Element viewpathTd = viewpath.getElement().getParentElement();
		final Element centerTd = center.getElement().getParentElement();
		final Element rightNavTd = rightNav.getElement().getParentElement();
		final Element footerTd = footer.getElement().getParentElement();

		headerTd.setPropertyString("id", "headerTd");
		viewpathTd.setPropertyString("id", "viewpathTd");
		centerTd.setPropertyString("id", "centerTd");
		rightNavTd.setPropertyString("id", "rightNavTd");
		footerTd.setPropertyString("id", "footerTd");

		initWidget(dockPanel);

		addHandler(new RpcUiHandler(this), RpcEvent.TYPE);
	}

	public void addUserSessionListener(IUserSessionListener listener) {
		userSessionListeners.add(listener);
	}

	public void removeUserSessionListener(IUserSessionListener listener) {
		userSessionListeners.remove(listener);
	}

	public void onAdminContextChange(AdminContext ac, ChangeType changeType) {
		if(changeType == ChangeType.USER_CHANGE) {
			final Model user = ac.getUser();
			final Model account = ac.getUserAccount();

			// update the current user panel
			rightNav.setCurrentUser(user);
			rightNav.setCurrentAccount(account);

			// clear out the views
			ViewManager.get().clear();

			// set the initial view based on the user's account type
			ViewManager.get().dispatch(
					new ShowViewRequest(MainViewClass.getMainViewClass((SmbizEntityType) account.getEntityType())));
		}
		else if(changeType == ChangeType.ACCOUNT_CHANGE) {
			// update the current account panel
			rightNav.setCurrentAccount(ac.getAccount());
		}
		else if(changeType == ChangeType.INVALIDATE) {
			// clear out state in right nav
			rightNav.clearCurrentUser();
			rightNav.clearCurrentAccount();

			// clear out the views
			ViewManager.get().clear();
			return;
		}

		// update the options
		rightNav.opsPanel.setOptions(OpsManager.getOptions(ac.getUserAccountType(), ac.getAccountType(), ac.getUserRole()));
	}

	static final class Header extends FlowPanel {

		public Header() {
			super();
			setStylePrimaryName(Styles.HEADER);
			add(new HTML("<h1>smbiz Admin</h1>"));
		}
	}

	private final class RightNav extends VerticalPanel implements ClickHandler, SubmitHandler, SubmitCompleteHandler, IOptionHandler {

		FormPanel frmLogout;
		Button btnLogoff;
		ViewLink vlUsername;
		Label lblUserDateCreated;
		ViewLink vlUserAccount;

		DisclosurePanel dpCrntAccount;
		ViewLink vlCrntAcnt;
		Label lblCrntAcntType;
		Label lblCrntAcntDateCreated;

		DisclosurePanel dpOps;
		OptionsPanel opsPanel;

		DisclosurePanel dpOpsDisplay;
		StatusDisplay cmdDisplay;

		public RightNav() {
			super();

			setStylePrimaryName(Styles.RIGHT_NAV);

			Label lbl;

			// current user
			vlUsername = new ViewLink();
			lblUserDateCreated = new Label();
			vlUserAccount = new ViewLink();

			btnLogoff = new Button("logout", this);

			Grid g = new Grid(4, 2);
			lbl = new Label("user");
			lbl.setStyleName("lbl");
			g.setWidget(0, 0, lbl);
			g.setWidget(0, 1, vlUsername);
			lbl = new Label("created");
			lbl.setStyleName("lbl");
			g.setWidget(1, 0, lbl);
			g.setWidget(1, 1, lblUserDateCreated);
			lbl = new Label("account");
			lbl.setStyleName("lbl");
			g.setWidget(2, 0, lbl);
			g.setWidget(2, 1, vlUserAccount);
			g.setWidget(3, 0, btnLogoff);

			frmLogout = new FormPanel();
			frmLogout.setMethod(FormPanel.METHOD_POST);
			frmLogout.setAction(GWT.getModuleBaseURL() + "adminLogout");
			frmLogout.add(g);
			frmLogout.addSubmitHandler(this);
			frmLogout.addSubmitCompleteHandler(this);

			SimplePanel simplePanel = new SimplePanel();
			DOM.setElementAttribute(simplePanel.getElement(), "id", "currentUser");
			simplePanel.add(frmLogout);
			add(simplePanel);

			// current account
			vlCrntAcnt = new ViewLink();
			lblCrntAcntType = new Label();
			lblCrntAcntDateCreated = new Label();

			g = new Grid(3, 2);
			lbl = new Label("name");
			lbl.setStyleName("lbl");
			g.setWidget(0, 0, lbl);
			g.setWidget(0, 1, vlCrntAcnt);
			lbl = new Label("type");
			lbl.setStyleName("lbl");
			g.setWidget(1, 0, lbl);
			g.setWidget(1, 1, lblCrntAcntType);
			lbl = new Label("created");
			lbl.setStyleName("lbl");
			g.setWidget(2, 0, lbl);
			g.setWidget(2, 1, lblCrntAcntDateCreated);

			simplePanel = new SimplePanel();
			DOM.setElementAttribute(simplePanel.getElement(), "id", "currentAccount");
			simplePanel.add(g);

			dpCrntAccount = new DisclosurePanel("Current Account", true);
			dpCrntAccount.setStylePrimaryName(Styles.DISCLOSURE_PANEL);
			dpCrntAccount.add(simplePanel);
			add(dpCrntAccount);

			// operations
			dpOps = new DisclosurePanel("Operations", true);
			dpOps.setStylePrimaryName(Styles.DISCLOSURE_PANEL);
			opsPanel = new OptionsPanel();
			opsPanel.addOptionHandler(this);
			dpOps.add(opsPanel);
			add(dpOps);

			// command history
			cmdDisplay = new StatusDisplay(MsgAttr.EXCEPTION.flag | MsgAttr.STATUS.flag);
			simplePanel = new SimplePanel();
			simplePanel.add(cmdDisplay);
			dpOpsDisplay = new DisclosurePanel("Command History", false);
			dpOpsDisplay.setStylePrimaryName(Styles.DISCLOSURE_PANEL);
			DOM.setElementAttribute(dpOpsDisplay.getElement(), "id", "dpOpsDisplay");
			dpOpsDisplay.add(simplePanel);
			add(dpOpsDisplay);
		}

		public void onClick(ClickEvent event) {
			if(event.getSource() == btnLogoff) {
				frmLogout.submit();
			}
		}

		public void onSubmit(SubmitEvent event) {
			if(!Window.confirm("Are you sure you want to Log out?")) {
				event.cancel();
			}
		}

		public void onSubmitComplete(SubmitCompleteEvent event) {
			final String results = event.getResults();
			if(results == null || results.length() == 0) {
				// successful logoff
				userSessionListeners.fireLogout();
				return;
			}
			// logout error
			Window.alert("Logout error: " + results);
		}

		@Override
		public void onOptionEvent(OptionEvent event) {
			final String optionText = event.getOptionText();
			if(event.getOptionEventType() == OptionEvent.EventType.SELECTED) {
				final AdminContext ac = SmbizAdmin.getAdminContextCmd().getAdminContext();
				final IViewInitializer vi =
					OpsManager.resolveViewInitializer(optionText, ac.getUser(), ac.getAccount());
				if(vi == null) {
					Window.alert("The view: '" + optionText + "' is currently not implemented.");
					return;
				}
				ViewManager.get().dispatch(new ShowViewRequest(vi));
			}
		}

		private void setCurrentUser(Model user) {
			vlUsername.setText(user.asString("emailAddress"));
			vlUsername.setViewInitializer(new EditViewInitializer(UserEditView.klas, user));
			lblUserDateCreated.setText(Fmt.format(user.getDateCreated(), GlobalFormat.DATE));
			final Model account = user.nestedModel("account");
			if(account != null) {
				vlUserAccount.setText(account.getName());
				vlUserAccount.setViewInitializer(new EditViewInitializer(AccountEditView.klas, account));
			}
			else {
				vlUserAccount.setText("-");
				vlUserAccount.setViewInitializer(null);
			}
		}

		private void setCurrentAccount(Model account) {
			vlCrntAcnt.setText(account.getName());
			vlCrntAcnt.setViewInitializer(new EditViewInitializer(AccountEditView.klas, account));
			lblCrntAcntType.setText(account.getEntityType().descriptor());
			lblCrntAcntDateCreated.setText(Fmt.format(account.getDateCreated(), GlobalFormat.DATE));
		}

		private void clearCurrentUser() {
			this.vlUsername.setText(null);
			this.vlUsername.setViewInitializer(null);
			this.lblUserDateCreated.setText(null);
			this.vlUserAccount.setText(null);
			this.vlUserAccount.setViewInitializer(null);
		}

		private void clearCurrentAccount() {
			this.vlCrntAcnt.setText(null);
			this.lblCrntAcntType.setText(null);
			this.lblCrntAcntDateCreated.setText(null);
		}

	}

	/**
	 * Center - A {@link SimplePanel} designed to contain the current pinned view.
	 * @author jpk
	 */
	static final class Center extends FlowPanel implements IStatusHandler {

		public Center() {
			super();
			setStylePrimaryName(Styles.CENTER);
		}

		@Override
		protected void onLoad() {
			super.onLoad();
			StatusEventDispatcher.get().addStatusHandler(this);
		}

		@Override
		protected void onUnload() {
			super.onUnload();
			StatusEventDispatcher.get().removeStatusHandler(this);
		}

		public void onStatusEvent(StatusEvent event) {
			final Status status = event.getStatus();
			if(status != null) {
				final List<Msg> gms = status.getMsgs(MsgAttr.EXCEPTION.flag);
				if(gms != null && gms.size() > 0) {
					Msgs.post(gms, this, Position.CENTER, -1, true);
				}
			}
		}
	} // Center

	static final class Footer extends FlowPanel {

		public Footer() {
			super();
			setStylePrimaryName(Styles.FOOTER);
			add(new HTML("<p>&copy; 2009 The Logic Lab - smbiz v" + App.constants().appVersion() + "</p>"));
		}

	}

	@Override
	protected void onAttach() {
		ViewManager.initialize(center, 4);
		super.onAttach();
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		ViewManager.shutdown();
	}

}
