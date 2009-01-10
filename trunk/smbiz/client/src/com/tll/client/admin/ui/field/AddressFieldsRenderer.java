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

	public void render(Panel panel, FieldGroup fg, String parentPropPath) {
		FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
		cmpsr.setCanvas(panel);

		cmpsr.addField(fg.getField(parentPropPath, "emailAddress"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField(parentPropPath, "firstName"));
		cmpsr.addField(fg.getField(parentPropPath, "mi"));
		cmpsr.addField(fg.getField(parentPropPath, "lastName"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField(parentPropPath, "attn"));
		cmpsr.addField(fg.getField(parentPropPath, "company"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField(parentPropPath, "address1"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField(parentPropPath, "address2"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField(parentPropPath, "city"));
		cmpsr.addField(fg.getField(parentPropPath, "province"));

		cmpsr.newRow();
		cmpsr.addField(fg.getField(parentPropPath, "postalCode"));
		cmpsr.addField(fg.getField(parentPropPath, "country"));
	}

}
