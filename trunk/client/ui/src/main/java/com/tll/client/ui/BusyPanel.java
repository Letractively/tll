/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tll.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * BusyPanel - Overlay panel used to prohibit ui interaction to serve as a
 * visual way to indicate busy-ness.
 */
public class BusyPanel extends GlassPanel {

	/**
	 * ThrobbingPanel - Simple panel (table) w/ the throbber image in the center.
	 * @author jpk
	 */
	static final class ThrobbingPanel extends SimplePanel {

		/**
		 * Constructor
		 * @param throbImage The throbbing image
		 */
		public ThrobbingPanel(Image throbImage) {
			super();
			final Grid g = new Grid(1, 1);
			g.getCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
			g.setWidth("100%");
			g.setHeight("100%");
			g.setWidget(0, 0, throbImage);
			setWidget(g);
		}

	}

	/**
	 * The default throbber image ref.
	 */
	static final String THROBBER_URL = GWT.getModuleBaseURL() + "/images/throbber.gif";

	/**
	 * Creates an {@link AbsolutePanel} that overlays the given widget and is
	 * added to the {@link RootPanel}.
	 * @param w The target widget
	 * @return Newly created {@link AbsolutePanel} added to the {@link RootPanel}.
	 */
	public static AbsolutePanel getAbsoluteOverlay(Widget w) {
		final AbsolutePanel ap = new AbsolutePanel();
		ap.setPixelSize(w.getOffsetWidth(), w.getOffsetHeight());
		RootPanel.get().add(ap, w.getAbsoluteLeft(), w.getAbsoluteTop());
		return ap;
	}

	/**
	 * Constructor - Overlays the entire root panel
	 * @param autoHide
	 */
	public BusyPanel(boolean autoHide) {
		super(autoHide);
		setThrobImage(new Image(THROBBER_URL));
	}

	/**
	 * Sets the background color of the panel.
	 * @param color
	 */
	public void setColor(String color) {
		DOM.setStyleAttribute(getElement(), "backgroundColor", color == null ? "#000" : color);
	}

	/**
	 * Set the opacity from 0 (invisible) to 100 (opaque).
	 * @param opacity
	 */
	public void setOpacity(int opacity) {
		final Element elm = getElement();
		DOM.setStyleAttribute(elm, "filter", "alpha(opacity=" + opacity + ")");
		DOM.setStyleAttribute(elm, "opacity", Float.toString(opacity / 100f));
	}

	/**
	 * Sets the throb image.
	 * @param image
	 */
	public void setThrobImage(Image image) {
		if(image != null) {
			setWidget(new ThrobbingPanel(image));
		}
	}
}
