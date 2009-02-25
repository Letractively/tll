/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.GridFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;

/**
 * BankFieldsRenderer
 * @author jpk
 */
public class BankFieldsRenderer implements IFieldRenderer<FlowPanel> {

	public void render(FlowPanel panel, FieldGroup fg) {
		final GridFieldComposer cmpsr = new GridFieldComposer();
		cmpsr.setCanvas(panel);

		cmpsr.addField(fg.getFieldByName("bankName"));
		cmpsr.addField(fg.getFieldByName("bankAccountNo"));
		cmpsr.addField(fg.getFieldByName("bankRoutingNo"));
	}

}
