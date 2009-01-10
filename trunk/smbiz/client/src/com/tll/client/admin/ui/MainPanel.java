/**
 * The Logic Lab
 */
package com.tll.client.admin.ui;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.admin.AdminContext;
import com.tll.client.admin.data.rpc.IAdminContextListener;
import com.tll.client.admin.mvc.view.MainView.MainViewClass;
import com.tll.client.admin.mvc.view.user.UserEditView;
import com.tll.client.data.Status;
import com.tll.client.data.rpc.ISourcesUserSessionEvents;
import com.tll.client.data.rpc.IStatusListener;
import com.tll.client.data.rpc.IUserSessionListener;
import com.tll.client.data.rpc.StatusEvent;
import com.tll.client.data.rpc.StatusEventDispatcher;
import com.tll.client.model.Model;
import com.tll.client.model.ModelChangeManager;
import com.tll.client.model.PropertyPathException;
import com.tll.client.msg.Msg;
import com.tll.client.msg.MsgManager;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.EditViewRequest;
import com.tll.client.mvc.view.StaticViewRequest;
import com.tll.client.ui.StatusDisplay;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.client.ui.view.RecentViewsPanel;
import com.tll.client.ui.view.ViewPathPanel;
import com.tll.client.ui.view.ViewRequestLink;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;

/**
 * MainPanel
 * @author jpk
 */
public final class MainPanel extends Composite implements IAdminContextListener, ISourcesUserSessionEvents {

	private final DockPanel dockPanel = new DockPanel();
	private final Header header = new Header();
	private final ViewPathPanel viewpath = new ViewPathPanel();
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
		dockPanel.setStylePrimaryName("sb-mainPanel");

		Element headerTd = header.getElement().getParentElement();
		Element viewpathTd = viewpath.getElement().getParentElement();
		Element centerTd = center.getElement().getParentElement();
		Element rightNavTd = rightNav.getElement().getParentElement();
		Element footerTd = footer.getElement().getParentElement();

		headerTd.setPropertyString("id", "headerTd");
		viewpathTd.setPropertyString("id", "viewpathTd");
		centerTd.setPropertyString("id", "centerTd");
		rightNavTd.setPropertyString("id", "rightNavTd");
		footerTd.setPropertyString("id", "footerTd");

		initWidget(dockPanel);
	}

	public void addUserSessionListener(IUserSessionListener listener) {
		userSessionListeners.add(listener);
	}

	public void removeUserSessionListener(IUserSessionListener listener) {
		userSessionListeners.remove(listener);
	}

	public void onAdminContextChange(AdminContext ac, ChangeType changeType) {
		if(changeType == ChangeType.USER_CHANGE) {
			Model user = ac.getUser();
			Model account = ac.getUserAccount();

			// update the current user panel
			rightNav.setCurrentUser(user);
			rightNav.setCurrentAccount(account);

			// set the initial view based on the user's account type
			ViewManager.instance().dispatch(
					new StaticViewRequest(this, MainViewClass.getMainViewClass(account.getEntityType())));
		}
		else if(changeType == ChangeType.ACCOUNT_CHANGE) {
			// update the current account panel
			rightNav.setCurrentAccount(ac.getAccount());

			// clear out the views
			ViewManager.instance().clear();
		}
		else if(changeType == ChangeType.INVALIDATE) {
			// clear out state in right nav
			rightNav.clearCurrentUser();
			rightNav.clearCurrentAccount();

			// clear out the views
			ViewManager.instance().clear();
		}
	}

	private final class Header extends FlowPanel {

		public Header() {
			super();
			setStylePrimaryName("sb-Header");
			add(new HTML("<h1>smbiz Admin</h1>"));
		}
	}

	private final class RightNav extends VerticalPanel implements ClickListener, FormHandler {

		FormPanel frmLogout;
		Button btnLogoff;
		ViewRequestLink vlUsername;
		Label lblUserDateCreated;
		Label lblUserAccount;

		DisclosurePanel dpCrntAccount;
		Label lblCrntAcnt;
		Label lblCrntAcntType;
		Label lblCrntAcntDateCreated;

		DisclosurePanel dpViewHistory;
		RecentViewsPanel viewHistoryPanel;

		DisclosurePanel dpStatusDisplay;
		StatusDisplay statusDisplay;

		public RightNav() {
			super();

			setStylePrimaryName("sb-RightNav");

			Label lbl;

			// current user...
			vlUsername = new ViewRequestLink();
			lblUserDateCreated = new Label();
			lblUserAccount = new Label();

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
			g.setWidget(2, 1, lblUserAccount);
			g.setWidget(3, 0, btnLogoff);

			frmLogout = new FormPanel();
			frmLogout.setMethod(FormPanel.METHOD_POST);
			frmLogout.setAction(App.getBaseUrl() + "adminLogout");
			frmLogout.add(g);
			frmLogout.addFormHandler(this);

			SimplePanel simplePanel = new SimplePanel();
			DOM.setElementAttribute(simplePanel.getElement(), "id", "currentUser");
			simplePanel.add(frmLogout);
			add(simplePanel);

			// current account...
			lblCrntAcnt = new Label();
			lblCrntAcntType = new Label();
			lblCrntAcntDateCreated = new Label();

			g = new Grid(3, 2);
			lbl = new Label("name");
			lbl.setStyleName("lbl");
			g.setWidget(0, 0, lbl);
			g.setWidget(0, 1, lblCrntAcnt);
			lbl = new Label("type");
			lbl.setStyleName("lbl");
			g.setWidget(1, 0, lbl);
			g.setWidget(1, 1, lblCrntAcntType);
			lbl = new Label("created");
			lbl.setStyleName("lbl");
			g.setWidget(2, 0, lbl);
			g.setWidget(2, 1, lblCrntAcntDateCreated);

			simplePanel = new SimplePanel();
			simplePanel.add(g);

			dpCrntAccount = new DisclosurePanel("Current Account", true);
			dpCrntAccount.add(simplePanel);
			add(dpCrntAccount);

			// view history...
			dpViewHistory = new DisclosurePanel("Recent Views", false);
			viewHistoryPanel = new RecentViewsPanel();
			simplePanel = new SimplePanel();
			simplePanel.add(viewHistoryPanel);
			dpViewHistory.add(simplePanel);
			add(dpViewHistory);

			// status history...
			statusDisplay = new StatusDisplay();
			simplePanel = new SimplePanel();
			simplePanel.add(statusDisplay);

			dpStatusDisplay = new DisclosurePanel("Status History", false);
			DOM.setElementAttribute(dpStatusDisplay.getElement(), "id", "dpStatusDisplay");
			dpStatusDisplay.add(simplePanel);
			add(dpStatusDisplay);
		}

		public void onClick(Widget sender) {
			if(sender == btnLogoff) {
				frmLogout.submit();
			}
		}

		public void onSubmit(FormSubmitEvent event) {
			if(!Window.confirm("Are you sure you want to Log out?")) {
				event.setCancelled(true);
			}
		}

		public void onSubmitComplete(FormSubmitCompleteEvent event) {
			String results = event.getResults();
			if(results == null || results.length() == 0) {
				// successful logoff
				userSessionListeners.fireLogout();
				return;
			}
			// logout error
			Window.alert("Logout error: " + results);
		}

		private void setCurrentUser(Model user) {
			this.vlUsername.setText(user.asString("emailAddress"));
			this.vlUsername.setViewRequest(new EditViewRequest(this, UserEditView.klas, user));
			this.lblUserDateCreated.setText(Fmt.format(user.getDateCreated(), GlobalFormat.DATE));
			String dm;
			try {
				Model account = user.relatedOne("account").getModel();
				dm = Fmt.format(account.getDateModified(), GlobalFormat.DATE);
			}
			catch(PropertyPathException e) {
				dm = "";
			}
			this.lblUserAccount.setText(dm);
		}

		private void setCurrentAccount(Model account) {
			this.lblCrntAcnt.setText(account.getName());
			this.lblCrntAcntType.setText(account.getEntityType().getName());
			this.lblCrntAcntDateCreated.setText(Fmt.format(account.getDateCreated(), GlobalFormat.DATE));
		}

		private void clearCurrentUser() {
			this.vlUsername.setText(null);
			this.vlUsername.setViewRequest(null);
			this.lblUserDateCreated.setText(null);
			this.lblUserAccount.setText(null);
		}

		private void clearCurrentAccount() {
			this.lblCrntAcnt.setText(null);
			this.lblCrntAcntType.setText(null);
			this.lblCrntAcntDateCreated.setText(null);
		}

	}

	/**
	 * Center - A {@link SimplePanel} designed to contain the current pinned view.
	 * @author jpk
	 */
	private final class Center extends FlowPanel implements IStatusListener {

		public Center() {
			super();
			setStylePrimaryName("sb-Center");
		}

		@Override
		protected void onLoad() {
			super.onLoad();
			StatusEventDispatcher.instance().addStatusListener(this);
			ViewManager.instance().initialize(this);
			// set the main model change listener so views see all model change events
			ModelChangeManager.instance().addModelChangeListener(ViewManager.instance());
		}

		@Override
		protected void onUnload() {
			super.onUnload();
			ModelChangeManager.instance().removeModelChangeListener(ViewManager.instance());
			ViewManager.instance().clear();
			StatusEventDispatcher.instance().removeStatusListener(this);
			MsgManager.instance().clear();
		}

		public void onStatusEvent(StatusEvent event) {
			Status status = event.getStatus();
			if(status != null) {
				List<Msg> gms = status.getGlobalDisplayMsgs();
				if(gms != null && gms.size() > 0) {
					MsgManager.instance().post(true, gms, Position.CENTER, this, -1, true).show();
				}
			}
		}

	}

	private final class Footer extends FlowPanel {

		public Footer() {
			super();
			setStylePrimaryName("sb-Footer");
			add(new HTML("<p>&copy; 2009 The Logic Lab - smbiz v" + App.constants.appVersion() + "</p>"));
		}

	}

}
