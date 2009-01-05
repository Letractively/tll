/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;

/**
 * CreditCardFieldsRenderer
 * @author jpk
 */
public class CreditCardFieldsRenderer implements IFieldRenderer {

	public void render(Panel panel, String parentPropPath, FieldGroup fg) {
		final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
		cmpsr.setCanvas(panel);

		cmpsr.addField(fg.getField("paymentData_ccType"));

		cmpsr.addField(fg.getField("paymentData_ccNum"));
		cmpsr.addField(fg.getField("paymentData_ccCvv2"));
		cmpsr.addField(fg.getField("paymentData_ccExpMonth"));
		cmpsr.addField(fg.getField("paymentData_ccExpYear"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField("paymentData_ccName"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField("paymentData_ccAddress1"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField("paymentData_ccAddress2"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField("paymentData_ccCity"));
		cmpsr.addField(fg.getField("paymentData_ccState"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField("paymentData_ccZip"));
		cmpsr.addField(fg.getField("paymentData_ccCountry"));
	}

}
