/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.admin.ui.field;

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

		cmpsr.addField(fg.getFieldByName("type"));

		cmpsr.addField(fg.getFieldByName("num"));
		cmpsr.addField(fg.getFieldByName("cvv2"));
		cmpsr.addField(fg.getFieldByName("expMonth"));
		cmpsr.addField(fg.getFieldByName("expYear"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("name"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("address1"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("address2"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("city"));
		cmpsr.addField(fg.getFieldByName("state"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("zip"));
		cmpsr.addField(fg.getFieldByName("country"));
	}

}
