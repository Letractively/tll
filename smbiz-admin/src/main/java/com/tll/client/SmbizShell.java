/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * UI shell for the smbiz admin app.
 * @author jpk
 */
public class SmbizShell extends Composite {

	private static SmbizShellUiBinder uiBinder = GWT.create(SmbizShellUiBinder.class);

	interface SmbizShellUiBinder extends UiBinder<Widget, SmbizShell> {
	}

	@UiField
	DockLayoutPanel dockLayout;
	
	@UiField
	HTML header;
	
	@UiField
	HTML footer;
	
	@UiField
	FlowPanel navPanel;
	
	@UiField
	SimplePanel center;

	/**
	 * Constructor
	 */
	public SmbizShell() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public HasOneWidget getPanel() {
		return center;
	}
}
