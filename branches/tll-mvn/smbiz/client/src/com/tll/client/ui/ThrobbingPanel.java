package com.tll.client.ui;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * ThrobbingPanel - Simple panel (table) w/ the throbber image in the center.
 * @author jpk
 */
public final class ThrobbingPanel extends SimplePanel {

	/**
	 * Constructor
	 */
	public ThrobbingPanel() {
		super();
		Grid g = new Grid(1, 1);
		g.getCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		g.setWidth("100%");
		g.setHeight("100%");
		g.setWidget(0, 0, new Image("images/throbber.gif"));
		setWidget(g);
	}

}