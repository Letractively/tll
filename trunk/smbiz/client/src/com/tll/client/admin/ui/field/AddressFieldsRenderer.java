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
 * AddressFieldsRenderer
 * @author jpk
 */
public class AddressFieldsRenderer implements IFieldRenderer<FlowPanel> {

	public void render(FlowPanel panel, FieldGroup fg) {
		FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
		cmpsr.setCanvas(panel);

		cmpsr.addField(fg.getFieldByName("emailAddress"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("firstName"));
		cmpsr.addField(fg.getFieldByName("mi"));
		cmpsr.addField(fg.getFieldByName("lastName"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("attn"));
		cmpsr.addField(fg.getFieldByName("company"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("address1"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("address2"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("city"));
		cmpsr.addField(fg.getFieldByName("province"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("postalCode"));
		cmpsr.addField(fg.getFieldByName("country"));
	}

}
