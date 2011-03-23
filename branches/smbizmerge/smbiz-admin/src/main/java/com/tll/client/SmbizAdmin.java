package com.tll.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * @author jon.kirton
 */
public class SmbizAdmin implements EntryPoint {

	@Override
	public void onModuleLoad() {
		IClientFactory cf = GWT.create(IClientFactory.class);
		cf.getSmbizApp().run(RootLayoutPanel.get());
	}
}
