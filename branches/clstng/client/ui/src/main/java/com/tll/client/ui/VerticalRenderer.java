package com.tll.client.ui;

import java.util.Collection;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * VerticalRenderer
 * @author jpk
 */
public final class VerticalRenderer implements IWidgetRenderer {

	public static final VerticalRenderer INSTANCE = new VerticalRenderer();

	/**
	 * Constructor
	 */
	private VerticalRenderer() {
		super();
	}

	@Override
	public Panel render(Collection<? extends Widget> buttons) {
		final VerticalPanel panel = new VerticalPanel();
		for(final Widget rb : buttons) {
			panel.add(rb);
		}
		return panel;
	}
}