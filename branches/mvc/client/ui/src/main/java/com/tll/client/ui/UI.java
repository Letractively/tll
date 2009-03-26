/**
 * The Logic Lab
 * @author jpk
 * Feb 10, 2009
 */
package com.tll.client.ui;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * UI
 * @author jpk
 */
public abstract class UI {

	/**
	 * The single busy panel that is shared among the client widgets.
	 */
	private static final BusyPanel theBusyPanel =
			new BusyPanel(false, null, 20, new Image(BusyPanel.DEFAULT_THROBBER_URL));

	/**
	 * The global counter used to decide on whether to show/hide the busy panel.
	 */
	private static int busyCounter = 0;

	public static void busy() {
		if(busyCounter++ == 0) {
			RootPanel.get().add(theBusyPanel, 0, 0);
		}
	}

	public static void unbusy() {
		if(busyCounter > 0 && --busyCounter == 0) {
			theBusyPanel.removeFromParent();
		}
	}

	public static void resetBusy() {
		busyCounter = 0;
		theBusyPanel.removeFromParent();
	}

	public static void darkenBusyPanel() {
		theBusyPanel.setOpacity(60);
	}

	public static void lightenBusyPanel() {
		theBusyPanel.setOpacity(20);
	}
}
