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

		cmpsr.addField(fg.getFieldWidget("ccType"));

		cmpsr.addField(fg.getFieldWidget("ccNum"));
		cmpsr.addField(fg.getFieldWidget("ccCvv2"));
		cmpsr.addField(fg.getFieldWidget("ccExpMonth"));
		cmpsr.addField(fg.getFieldWidget("ccExpYear"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidget("ccName"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidget("ccAddress1"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidget("ccAddress2"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidget("ccCity"));
		cmpsr.addField(fg.getFieldWidget("ccState"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidget("ccZip"));
		cmpsr.addField(fg.getFieldWidget("ccCountry"));
	}

}
