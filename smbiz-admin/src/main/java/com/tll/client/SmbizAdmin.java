package com.tll.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * @author jon.kirton
 */
public class SmbizAdmin implements EntryPoint {

	@Override
	public void onModuleLoad() {
		SmbizApp app = new SmbizApp(new SmbizShell());
		app.run(RootLayoutPanel.get());
	}
}
