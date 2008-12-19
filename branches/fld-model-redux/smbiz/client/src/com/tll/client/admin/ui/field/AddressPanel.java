/**
 * The Logic Lab
 * @author jpk Dec 17, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowFieldPanelComposer;
import com.tll.service.app.RefDataType;

/**
 * AddressPanel
 * @author jpk
 */
public final class AddressPanel extends FieldPanel {

	/**
	 * Constructor
	 */
	public AddressPanel() {
		super("Address");
	}

	@Override
	public void populateFieldGroup(FieldGroup fields) {
		fields.addField(ftext("emailAddress", "Email Address", 30));
		fields.addField(ftext("firstName", "First Name", 20));
		fields.addField(ftext("lastName", "Last Name", 20));
		fields.addField(ftext("mi", "MI", 1));
		fields.addField(ftext("company", "Company", 20));
		fields.addField(ftext("attn", "Attn", 10));
		fields.addField(ftext("address1", "Address 1", 40));
		fields.addField(ftext("address2", "Address 2", 40));
		fields.addField(ftext("city", "City", 30));
		fields
				.addField(fsuggest("province", "State/Province", AuxDataCache.instance().getRefDataMap(RefDataType.US_STATES)));
		fields.addField(ftext("postalCode", "Zip", 20));
		fields
				.addField(fsuggest("country", "Country", AuxDataCache.instance().getRefDataMap(RefDataType.ISO_COUNTRY_CODES)));
	}

	@Override
	protected void drawInternal(Panel canvas) {
		final FieldGroup fields = getFieldGroup();
		FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
		cmpsr.setCanvas(canvas);

		cmpsr.addField(fields.getField("emailAddress"));

		cmpsr.newRow();
		cmpsr.addField(fields.getField("firstName"));
		cmpsr.addField(fields.getField("mi"));
		cmpsr.addField(fields.getField("lastName"));

		cmpsr.newRow();
		cmpsr.addField(fields.getField("attn"));
		cmpsr.addField(fields.getField("company"));

		cmpsr.newRow();
		cmpsr.addField(fields.getField("address1"));

		cmpsr.newRow();
		cmpsr.addField(fields.getField("address2"));

		cmpsr.newRow();
		cmpsr.addField(fields.getField("city"));
		cmpsr.addField(fields.getField("province"));

		cmpsr.newRow();
		cmpsr.addField(fields.getField("postalCode"));
		cmpsr.addField(fields.getField("country"));
	}
}
