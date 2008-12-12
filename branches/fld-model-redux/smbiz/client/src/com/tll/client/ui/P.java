package com.tll.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

/**
 * Br - HTML p tag in widget form.
 * @author jpk
 */
public final class P extends Widget {

	public P() {
		setElement(DOM.createElement("p"));
	}

	public P(String text) {
		this();
		getElement().setInnerText(text);
	}
}