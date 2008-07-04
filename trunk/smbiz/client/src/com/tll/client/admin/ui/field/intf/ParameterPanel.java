package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.field.FieldGroup;
import com.tll.client.model.PropertyPath;
import com.tll.client.ui.FlowFieldPanelComposer;
import com.tll.client.ui.field.AbstractField;
import com.tll.client.ui.field.FieldGroupPanel;

/**
 * ParameterPanel - Interface option parameter definition panel.
 * @author jpk
 */
final class ParameterPanel extends FieldGroupPanel {

	final String propertyName;

	/**
	 * Constructor
	 * @param parameterProperty The property name of the indexed parameter property.
	 *        (e.g.: options[1].parameters[0])
	 * @param fieldGroup The field group containing the parameter fields
	 */
	public ParameterPanel(String parameterProperty, FieldGroup fieldGroup) {
		super(null, fieldGroup);
		this.propertyName = parameterProperty;
	}

	@Override
	public void populateFieldGroup() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void draw(Panel canvas) {
		FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
		cmpsr.setCanvas(canvas);

		cmpsr.addField((AbstractField) getFields().getField(PropertyPath.getPropertyPath(propertyName, "name")));
		cmpsr.addField((AbstractField) getFields().getField(PropertyPath.getPropertyPath(propertyName, "code")));
		cmpsr.addField((AbstractField) getFields().getField(PropertyPath.getPropertyPath(propertyName, "description")));
	}
}