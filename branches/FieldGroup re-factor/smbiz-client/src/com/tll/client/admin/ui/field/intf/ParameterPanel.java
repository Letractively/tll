package com.tll.client.admin.ui.field.intf;

import com.tll.client.ui.FlowFieldPanelComposer;
import com.tll.client.ui.field.TextField;

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

		final TextField fname = createNameEntityField();
		fields.addField(fname);

		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer(panel);

		// first row
		canvas.addField(fname);
		canvas.addField(code);
		canvas.addField(description);
	}
}