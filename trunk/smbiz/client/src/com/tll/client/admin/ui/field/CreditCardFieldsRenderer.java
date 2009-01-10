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

	public void render(Panel panel, FieldGroup fg, String parentPropPath) {
		final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
		cmpsr.setCanvas(panel);

		cmpsr.addField(fg.getField(parentPropPath, "paymentData_ccType"));

		cmpsr.addField(fg.getField(parentPropPath, "paymentData_ccNum"));
		cmpsr.addField(fg.getField(parentPropPath, "paymentData_ccCvv2"));
		cmpsr.addField(fg.getField(parentPropPath, "paymentData_ccExpMonth"));
		cmpsr.addField(fg.getField(parentPropPath, "paymentData_ccExpYear"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField(parentPropPath, "paymentData_ccName"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField(parentPropPath, "paymentData_ccAddress1"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField(parentPropPath, "paymentData_ccAddress2"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField(parentPropPath, "paymentData_ccCity"));
		cmpsr.addField(fg.getField(parentPropPath, "paymentData_ccState"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField(parentPropPath, "paymentData_ccZip"));
		cmpsr.addField(fg.getField(parentPropPath, "paymentData_ccCountry"));
	}

}
