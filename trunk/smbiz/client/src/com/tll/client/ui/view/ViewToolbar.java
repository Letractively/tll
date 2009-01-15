/**
 * The Logic Lab
 * @author jpk Jan 3, 2008
 */
package com.tll.client.ui.view;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.ToggleButton;
import com.tll.client.App;
import com.tll.client.mvc.view.ViewOptions;
import com.tll.client.ui.Toolbar;

/**
 * ViewToolbar - A UI toolbar for user management of views.
 * @author jpk
 */
public class ViewToolbar extends Toolbar implements SourcesMouseEvents {

	/**
	 * Styles - (view.css)
	 * @author jpk
	 */
	protected static class Styles {

		public static final String VIEW_TOOLBAR = "viewToolbar";
		public static final String VIEW_TITLE = "viewTitle";
	} // Styles

	static final String TITLE_MINIMIZE = "Minimize";
	static final String TITLE_MAXIMIZE = "Maximize";
	static final String TITLE_CLOSE = "Close";
	static final String TITLE_REFRESH = "Refresh";
	static final String TITLE_POP = "Pop";
	static final String TITLE_PIN = "Pin";

	final Label viewTitle;
	final ToggleButton btnMinimize;
	final ToggleButton btnPop;
	final PushButton btnClose;
	final PushButton btnRefresh;

	/**
	 * Constructor
	 * @param viewDisplayName
	 * @param viewOptions The view options that dictates the appearance/behavior
	 *        of view toolbars.
	 * @param clickListener The listener for click events occurring w/in this
	 *        toolbar.
	 */
	public ViewToolbar(String viewDisplayName, ViewOptions viewOptions, ClickListener clickListener) {
		super();
		assert viewDisplayName != null && viewOptions != null && clickListener != null;
		viewTitle = new Label(viewDisplayName);
		btnMinimize =
				viewOptions.isMinimizable() ? new ToggleButton(App.imgs().arrow_sm_down().createImage(), App.imgs()
						.arrow_sm_right().createImage(), clickListener) : null;
		btnPop =
				viewOptions.isPopable() ? new ToggleButton(App.imgs().external().createImage(), App.imgs().permalink()
						.createImage(), clickListener) : null;
		btnClose = viewOptions.isClosable() ? new PushButton(App.imgs().close().createImage(), clickListener) : null;
		btnRefresh = viewOptions.isRefreshable() ? new PushButton(App.imgs().refresh().createImage(), clickListener) : null;

		addStyleName(Styles.VIEW_TOOLBAR);
		viewTitle.setStyleName(Styles.VIEW_TITLE);

		if(btnMinimize != null) addButton(btnMinimize, TITLE_MINIMIZE);
		add(viewTitle);

		// NOTE: we do this here as this is intrinsic behavior
		setWidgetContainerWidth(viewTitle, "100%");

		if(btnPop != null) addButton(btnPop, TITLE_POP);
		if(btnRefresh != null) addButton(btnRefresh, TITLE_REFRESH);
		if(btnClose != null) addButton(btnClose, TITLE_CLOSE);

	}

	public void addMouseListener(MouseListener listener) {
		viewTitle.addMouseListener(listener);
	}

	public void removeMouseListener(MouseListener listener) {
		viewTitle.removeMouseListener(listener);
	}
}
