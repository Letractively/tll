/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * AddressFieldsRenderer
 * @author jpk
 */
public class AddressFieldsRenderer implements IFieldRenderer<FlowPanel> {

	public void render(FlowPanel panel, FieldGroup fg) {
		final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
		cmpsr.setCanvas(panel);

		cmpsr.addField(fg.getFieldWidget("adrsEmailAddress"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidget("adrsFirstName"));
		cmpsr.addField(fg.getFieldWidget("adrsMi"));
		cmpsr.addField(fg.getFieldWidget("adrsLastName"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidget("adrsAttn"));
		cmpsr.addField(fg.getFieldWidget("adrsCompany"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidget("adrsAddress1"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidget("adrsAddress2"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidget("adrsCity"));
		cmpsr.addField(fg.getFieldWidget("adrsProvince"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidget("adrsPostalCode"));
		cmpsr.addField(fg.getFieldWidget("adrsCountry"));
	}

}
