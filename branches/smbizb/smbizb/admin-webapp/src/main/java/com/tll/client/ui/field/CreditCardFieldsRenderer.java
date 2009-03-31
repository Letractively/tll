/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * CreditCardFieldsRenderer
 * @author jpk
 */
public class CreditCardFieldsRenderer implements IFieldRenderer<FlowPanel> {

	public void render(FlowPanel panel, FieldGroup fg) {
		final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
		cmpsr.setCanvas(panel);

		cmpsr.addField(fg.getFieldWidgetByName("ccType"));

		cmpsr.addField(fg.getFieldWidgetByName("ccNum"));
		cmpsr.addField(fg.getFieldWidgetByName("ccCvv2"));
		cmpsr.addField(fg.getFieldWidgetByName("ccExpMonth"));
		cmpsr.addField(fg.getFieldWidgetByName("ccExpYear"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidgetByName("ccName"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidgetByName("ccAddress1"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidgetByName("ccAddress2"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidgetByName("ccCity"));
		cmpsr.addField(fg.getFieldWidgetByName("ccState"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidgetByName("ccZip"));
		cmpsr.addField(fg.getFieldWidgetByName("ccCountry"));
	}

}
