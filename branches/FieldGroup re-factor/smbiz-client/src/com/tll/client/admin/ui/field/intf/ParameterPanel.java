package com.tll.client.admin.ui.field.intf;

import com.tll.client.ui.FlowFieldPanelComposer;

/**
 * ParameterPanel - Interface option parameter definition panel.
 * @author jpk
 */
final class ParameterPanel extends InterfaceRelatedPanel {

	/**
	 * Constructor
	 */
	public ParameterPanel() {
		super(null);
	}

	@Override
	protected void doInit() {
		super.doInit();

		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer(panel);

		// first row
		canvas.addField(name);
		canvas.addField(code);
		canvas.addField(description);
	}
}