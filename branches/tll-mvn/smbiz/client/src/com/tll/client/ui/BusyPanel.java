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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.widgetideas.client.GlassPanel;

/**
 * BusyPanel - GlassPanel w/ a single child content Panel used to indicate the
 * application is "busy".
 */
public class BusyPanel extends GlassPanel {

	/**
	 * Constructor
	 * @param autoHide
	 * @param color
	 * @param opacity
	 * @param contentPanel
	 */
	public BusyPanel(boolean autoHide, String color, int opacity, SimplePanel contentPanel) {
		super(autoHide);
		setColor(color);
		setOpacity(opacity);
		setStyleName("gwt-BusyPanel");
		if(contentPanel != null) {
			setWidget(contentPanel);
		}
	}

	/**
	 * Sets the color.
	 * @param color
	 */
	public void setColor(String color) {
		DOM.setStyleAttribute(getElement(), "backgroundColor", color == null ? "#000" : color);
	}

	/**
	 * Set the opacity in terms of 0 - 100
	 * @param opacity
	 */
	public void setOpacity(int opacity) {
		Element elm = getElement();
		DOM.setStyleAttribute(elm, "filter", "alpha(opacity=" + opacity + ")");
		DOM.setStyleAttribute(elm, "opacity", Float.toString(opacity / 100f));
	}

}
