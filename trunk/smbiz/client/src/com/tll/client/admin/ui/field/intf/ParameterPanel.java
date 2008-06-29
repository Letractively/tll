package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.FlowFieldPanelComposer;
import com.tll.client.ui.field.FieldGroupPanel;
import com.tll.client.ui.field.TextAreaField;
import com.tll.client.ui.field.TextField;

/**
 * ParameterPanel - Interface option parameter definition panel.
 * @author jpk
 */
final class ParameterPanel extends FieldGroupPanel {

	final String propertyName;
	private TextField name, code;
	private TextAreaField description;

	/**
	 * Constructor
	 * @param propertyName The property name of the indexed parameter property.
	 *        (e.g.: options[1].parameters[0])
	 */
	public ParameterPanel(String propertyName) {
		super(null);
		this.propertyName = propertyName;
	}

	@Override
	public void populateFieldGroup() {
		name = createNameEntityField();
		code = ftext("code", "Code", 20);
		description = ftextarea("description", "Desc", 3, 8);

		addField(name);
		addField(code);
		addField(description);
	}

	@Override
	protected Widget draw() {
		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer();
		canvas.addField(name);
		canvas.addField(code);
		canvas.addField(description);
		return canvas.getCanvasWidget();
	}
}