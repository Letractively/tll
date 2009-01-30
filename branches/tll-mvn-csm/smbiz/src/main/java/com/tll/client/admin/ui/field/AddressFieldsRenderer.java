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

		cmpsr.addField(fg.getFieldByName("adrsEmailAddress"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("adrsFirstName"));
		cmpsr.addField(fg.getFieldByName("adrsMi"));
		cmpsr.addField(fg.getFieldByName("adrsLastName"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("adrsAttn"));
		cmpsr.addField(fg.getFieldByName("adrsCompany"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("adrsAddress1"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("adrsAddress2"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("adrsCity"));
		cmpsr.addField(fg.getFieldByName("adrsProvince"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldByName("adrsPostalCode"));
		cmpsr.addField(fg.getFieldByName("adrsCountry"));
	}

}
