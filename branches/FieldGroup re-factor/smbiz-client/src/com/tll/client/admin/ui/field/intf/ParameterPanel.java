package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.Widget;
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
	protected Widget doInit() {
		final TextField fname = createNameEntityField();
		fields.addField(fname);

		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer();

		// first row
		canvas.addField(fname);
		canvas.addField(code);
		canvas.addField(description);

		return canvas.getWidget();
	}
}