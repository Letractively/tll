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
	final HTML header = new HTML("<div><h1>Smbiz Admin</h1></div>");
	
	@UiField
	final HTML footer = new HTML("<p>&copy; 2009 The Logic Lab - smbiz vTODO</p>");
	
	@UiField
	final FlowPanel navPanel = new FlowPanel();
	
	@UiField
	final FlowPanel center = new FlowPanel();

	/**
	 * Constructor
	 */
	public SmbizShell() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	
}
