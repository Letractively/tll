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
 * AddressFieldsRenderer
 * @author jpk
 */
public class AddressFieldsRenderer implements IFieldRenderer {

	public void render(Panel panel, String parentPropPath, FieldGroup fg) {
		FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
		cmpsr.setCanvas(panel);

		cmpsr.addField(fg.getField("emailAddress"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField("firstName"));
		cmpsr.addField(fg.getField("mi"));
		cmpsr.addField(fg.getField("lastName"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField("attn"));
		cmpsr.addField(fg.getField("company"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField("address1"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField("address2"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField("city"));
		cmpsr.addField(fg.getField("province"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField("postalCode"));
		cmpsr.addField(fg.getField("country"));
	}

}
