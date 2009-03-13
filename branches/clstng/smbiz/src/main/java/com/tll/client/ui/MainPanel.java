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
import com.tll.client.App;
import com.tll.client.data.rpc.ISourcesUserSessionEvents;
import com.tll.client.data.rpc.IStatusListener;
import com.tll.client.data.rpc.IUserSessionListener;
import com.tll.client.data.rpc.StatusEvent;
import com.tll.client.data.rpc.StatusEventDispatcher;
import com.tll.client.model.ModelChangeManager;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.EditViewRequest;
import com.tll.client.mvc.view.StaticViewRequest;
import com.tll.client.mvc.view.MainView.MainViewClass;
import com.tll.client.mvc.view.user.UserEditView;
import com.tll.client.rpc.IAdminContextListener;
import com.tll.client.ui.msg.Msgs;
import com.tll.client.ui.view.RecentViewsPanel;
import com.tll.client.ui.view.ViewPathPanel;
import com.tll.client.ui.view.ViewRequestLink;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;
import com.tll.common.AdminContext;
import com.tll.common.data.Status;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.common.msg.Msg;

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

	} // Styles

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
		dockPanel.setStylePrimaryName(Styles.MAIN);

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

			// set the initial view based on the user's account type
			ViewManager.get().dispatch(
					new StaticViewRequest(this, MainViewClass.getMainViewClass(account.getEntityType())));
		}
		else if(changeType == ChangeType.ACCOUNT_CHANGE) {
			// update the current account panel
			rightNav.setCurrentAccount(ac.getAccount());

			// clear out the views
			ViewManager.get().clear();
		}
		else if(changeType == ChangeType.INVALIDATE) {
			// clear out state in right nav
			rightNav.clearCurrentUser();
			rightNav.clearCurrentAccount();

			// clear out the views
			ViewManager.get().clear();
		}
	}

	private final class Header extends FlowPanel {

		public Header() {
			super();
			setStylePrimaryName(Styles.HEADER);
			add(new HTML("<h1>smbiz Admin</h1>"));
		}
	}

	private final class RightNav extends VerticalPanel implements ClickHandler, SubmitHandler, SubmitCompleteHandler {

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

			setStylePrimaryName(Styles.RIGHT_NAV);

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
			frmLogout.setAction(GWT.getModuleBaseURL() + "adminLogout");
			frmLogout.add(g);
			frmLogout.addSubmitHandler(this);
			frmLogout.addSubmitCompleteHandler(this);

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

		private void setCurrentUser(Model user) {
			this.vlUsername.setText(user.asString("emailAddress"));
			this.vlUsername.setViewRequest(new EditViewRequest(this, UserEditView.klas, user));
			this.lblUserDateCreated.setText(Fmt.format(user.getDateCreated(), GlobalFormat.DATE));
			String dm;
			try {
				final Model account = user.relatedOne("account").getModel();
				dm = Fmt.format(account.getDateModified(), GlobalFormat.DATE);
			}
			catch(final PropertyPathException e) {
				dm = "";
			}
			this.lblUserAccount.setText(dm);
		}

		private void setCurrentAccount(Model account) {
			this.lblCrntAcnt.setText(account.getName());
			this.lblCrntAcntType.setText(account.getEntityType().getPresentationName());
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
			setStylePrimaryName(Styles.CENTER);
		}

		@Override
		protected void onLoad() {
			super.onLoad();
			StatusEventDispatcher.instance().addStatusListener(this);
			ViewManager.get().initialize(this);
			// set the main model change listener so views see all model change events
			ModelChangeManager.get().addModelChangeListener(ViewManager.get());
		}

		@Override
		protected void onUnload() {
			super.onUnload();
			ModelChangeManager.get().removeModelChangeListener(ViewManager.get());
			ViewManager.get().clear();
			StatusEventDispatcher.instance().removeStatusListener(this);
			//MsgManager.get().clear();
		}

		public void onStatusEvent(StatusEvent event) {
			final Status status = event.getStatus();
			if(status != null) {
				final List<Msg> gms = status.getGlobalDisplayMsgs();
				if(gms != null && gms.size() > 0) {
					// TODO create a dedicated inlined widget to handle global status messages.
					//MsgManager.get().post(gms, this, true).show(Position.CENTER, -1);
					Msgs.post(gms, this, Position.CENTER, -1, true);
				}
			}
		}

	}

	private final class Footer extends FlowPanel {

		public Footer() {
			super();
			setStylePrimaryName(Styles.FOOTER);
			add(new HTML("<p>&copy; 2009 The Logic Lab - smbiz v" + App.constants.appVersion() + "</p>"));
		}

	}

}