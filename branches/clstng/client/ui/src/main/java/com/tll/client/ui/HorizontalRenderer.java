package com.tll.client.ui;

import java.util.Collection;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * HorizontalRenderer
 * @author jpk
 */
public final class HorizontalRenderer implements IWidgetRenderer {
	
	public static final HorizontalRenderer INSTANCE = new HorizontalRenderer();

	/**
	 * Constructor
	 */
	private HorizontalRenderer() {
		super();
	}

	@Override
	public Panel render(Collection<? extends Widget> buttons) {
		final HorizontalPanel panel = new HorizontalPanel();
		for(final Widget rb : buttons) {
			panel.add(rb);
		}
		return panel;
	}
}