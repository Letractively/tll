/**
 * The Logic Lab
 * @author jpk Jan 3, 2008
 */
package com.tll.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.tll.client.ui.toolbar.Toolbar;
import com.tll.client.view.ViewOptions;

/**
 * ViewToolbar - A UI toolbar for user management of views.
 * @author jpk
 */
public class ViewToolbar extends Toolbar implements HasMouseDownHandlers, HasMouseMoveHandlers, HasMouseUpHandlers {

	/**
	 * Styles - (view.css)
	 * @author jpk
	 */
	protected static class Styles {

		public static final String VIEW_TOOLBAR = "viewToolbar";
		public static final String VIEW_TITLE = "viewTitle";
	} // Styles

	private static final ViewToolbarClientBundle resources = GWT.create(ViewToolbarClientBundle.class);

	public static final ViewToolbarClientBundle resources() {
		return resources;
	}

	static final String TITLE_MINIMIZE = "Minimize";
	static final String TITLE_MAXIMIZE = "Maximize";
	static final String TITLE_CLOSE = "Close";
	static final String TITLE_REFRESH = "Refresh";
	static final String TITLE_POP = "Pop";
	static final String TITLE_PIN = "Pin";

	final Label viewTitle;
	final ToggleButton btnMinimize, btnPop;
	final PushButton btnClose, btnRefresh;
	int numViewOpBtns;

	/**
	 * Constructor
	 * @param viewDisplayName
	 * @param viewOptions The view options that dictates the appearance/behavior
	 *        of view toolbars.
	 * @param clickHandler For click events occurring w/in this toolbar.
	 */
	public ViewToolbar(final String viewDisplayName, final ViewOptions viewOptions, final ClickHandler clickHandler) {
		super();
		assert viewDisplayName != null && viewOptions != null && clickHandler != null;
		viewTitle = new Label(viewDisplayName);
		btnMinimize =
				viewOptions.isMinimizable() ? new ToggleButton(AbstractImagePrototype.create(resources.arrow_sm_down())
						.createImage(), AbstractImagePrototype.create(resources.arrow_sm_right()).createImage(), clickHandler)
						: null;
		btnPop =
				viewOptions.isPopable() ? new ToggleButton(AbstractImagePrototype.create(resources.external()).createImage(),
						AbstractImagePrototype.create(resources.permalink()).createImage(), clickHandler) : null;

		btnClose =
				viewOptions.isClosable() ? new PushButton(AbstractImagePrototype.create(resources.close()).createImage(),
						clickHandler) : null;
		btnRefresh =
				viewOptions.isRefreshable() ? new PushButton(AbstractImagePrototype.create(resources.refresh()).createImage(),
						clickHandler) : null;

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

	/**
	 * Adds a view op button.
	 * @param b
	 * @param title
	 */
	public void addViewOpButton(final ButtonBase b, final String title) {
		insertButton(b, title, btnMinimize == null ? 1 : 2 + numViewOpBtns);
		numViewOpBtns++;
	}

	@Override
	public HandlerRegistration addMouseDownHandler(final MouseDownHandler handler) {
		return viewTitle.addMouseDownHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseUpHandler(final MouseUpHandler handler) {
		return viewTitle.addMouseUpHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(final MouseMoveHandler handler) {
		return viewTitle.addMouseMoveHandler(handler);
	}
}
