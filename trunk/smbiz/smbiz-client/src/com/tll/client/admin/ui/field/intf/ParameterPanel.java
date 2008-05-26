package com.tll.client.admin.ui.field.intf;

import com.tll.client.ui.FlowFieldCanvas;

/**
 * ParameterPanel - Interface option parameter definition panel.
 * @author jpk
 */
final class ParameterPanel extends InterfaceRelatedPanel {

	/**
	 * Constructor
	 * @param propName
	 */
	public ParameterPanel(String propName) {
		super(propName, null);
	}

	@Override
	protected void configure() {
		super.configure();

		FlowFieldCanvas canvas = new FlowFieldCanvas(panel);

		// first row
		canvas.addField(name);
		canvas.addField(code);
		canvas.addField(description);
	}
}