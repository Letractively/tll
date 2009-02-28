/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * BankFieldsRenderer
 * @author jpk
 */
public class BankFieldsRenderer implements IFieldRenderer<FlowPanel> {

	public void render(FlowPanel panel, FieldGroup fg) {
		final GridFieldComposer cmpsr = new GridFieldComposer();
		cmpsr.setCanvas(panel);

		cmpsr.addField(fg.getFieldWidgetByName("bankName"));
		cmpsr.addField(fg.getFieldWidgetByName("bankAccountNo"));
		cmpsr.addField(fg.getFieldWidgetByName("bankRoutingNo"));
	}

}
