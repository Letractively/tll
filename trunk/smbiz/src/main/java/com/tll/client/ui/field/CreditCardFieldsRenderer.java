/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;

/**
 * CreditCardFieldsRenderer
 * @author jpk
 */
public class CreditCardFieldsRenderer implements IFieldRenderer<FlowPanel> {

	public void render(FlowPanel panel, FieldGroup fg) {
		final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
		cmpsr.setCanvas(panel);

		cmpsr.addField(fg.getFieldByName("ccType"));

		cmpsr.addField(fg.getFieldByName("ccNum"));
		cmpsr.addField(fg.getFieldByName("ccCvv2"));
		cmpsr.addField(fg.getFieldByName("ccExpMonth"));
		cmpsr.addField(fg.getFieldByName("ccExpYear"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("ccName"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("ccAddress1"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("ccAddress2"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("ccCity"));
		cmpsr.addField(fg.getFieldByName("ccState"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("ccZip"));
		cmpsr.addField(fg.getFieldByName("ccCountry"));
	}

}
