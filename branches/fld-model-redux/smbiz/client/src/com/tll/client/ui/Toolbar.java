/**
 * The Logic Lab
 * @author jpk Jan 7, 2008
 */
package com.tll.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.CSS;

/**
 * Toolbar - Simple extension of {@link FlowPanel} that sets common
 * styles/properties relating to a "toolbar" widget implementation.
 * <p>
 * Refer to <code>toolbar.css</code> toolbar CSS styles.
 * @author jpk
 */
public abstract class Toolbar extends Composite {

	protected static final String CSS_SEPARATOR = "separator";
	protected static final String CSS_TOOLBAR = "toolbar";

	// private final FlowPanel pnl = new FlowPanel();
	private final HorizontalPanel pnl = new HorizontalPanel();

	/**
	 * Constructor
	 */
	public Toolbar() {
		super();
		pnl.setStyleName(CSS_TOOLBAR);
		initWidget(pnl);
	}

	public void add(Widget w) {
		pnl.add(w);
	}

	private void buttonize(ButtonBase b, String title) {
		final Element td = b.getElement().getParentElement();
		td.setClassName(CSS.BUTTON);
		if(title != null) {
			b.setTitle(title);
		}
	}

	/**
	 * Adds a Widget to the toolbar applying button styling.
	 * @param b The button to add
	 * @param title Optional title text shown on hover. May be <code>null</code>.
	 */
	public final void addButton(ButtonBase b, String title) {
		pnl.add(b);
		buttonize(b, title);
	}

	/**
	 * Shows a child Widget by setting the display style attribute of the parent
	 * Widget.
	 * @param w The Widget to show
	 */
	public final void show(Widget w) {
		if(pnl.getWidgetIndex(w) < 0) return;
		Element td = w.getElement().getParentElement();
		td.getStyle().setProperty("display", "");
	}

	/**
	 * Hides a child Widget by setting the display style attribute of the parent
	 * Widget.
	 * @param w The Widget to hide
	 */
	public final void hide(Widget w) {
		if(pnl.getWidgetIndex(w) < 0) return;
		Element td = w.getElement().getParentElement();
		td.getStyle().setProperty("display", "none");
	}

	/**
	 * Sets the width of the parent Widget of the given Widget.
	 * @param w The Widget
	 * @param width The width
	 */
	public final void setWidgetContainerWidth(Widget w, String width) {
		pnl.setCellWidth(w, width);
	}
}
