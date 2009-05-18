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

		cmpsr.addField(fg.getFieldWidgetByName("adrsEmailAddress"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidgetByName("adrsFirstName"));
		cmpsr.addField(fg.getFieldWidgetByName("adrsMi"));
		cmpsr.addField(fg.getFieldWidgetByName("adrsLastName"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidgetByName("adrsAttn"));
		cmpsr.addField(fg.getFieldWidgetByName("adrsCompany"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidgetByName("adrsAddress1"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidgetByName("adrsAddress2"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidgetByName("adrsCity"));
		cmpsr.addField(fg.getFieldWidgetByName("adrsProvince"));

		cmpsr.newRow();
		cmpsr.addField(fg.getFieldWidgetByName("adrsPostalCode"));
		cmpsr.addField(fg.getFieldWidgetByName("adrsCountry"));
	}

}
