/**
 * The Logic Lab
 * @author jpk
 * Jan 1, 2008
 */
package com.tll.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * HtmlListPanel - Corresponds to either a
 * <ul>
 * or
 * <ol>
 * html tag.
 * @author jpk
 */
public class HtmlListPanel extends ComplexPanel {

	/**
	 * Constructor
	 * @param ordered
	 */
	public HtmlListPanel(boolean ordered) {
		setElement(ordered ? DOM.createElement("ol") : DOM.createElement("ul"));
	}

	/**
	 * Li - HTML list item tag in widget form.
	 * @author jpk
	 */
	private static final class Li extends SimplePanel {

		public Li(Widget liContents) {
			super(DOM.createElement("li"));
			add(liContents);
		}

	}

	@Override
	public void add(Widget child) {
		super.add(new Li(child), getElement());
	}

}
