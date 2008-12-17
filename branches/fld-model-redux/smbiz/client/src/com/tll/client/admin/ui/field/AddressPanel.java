/**
 * The Logic Lab
 * @author jpk Dec 17, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.field.FieldModelBinding;
import com.tll.client.field.FieldGroup;
import com.tll.client.model.Model;
import com.tll.client.ui.field.FieldFactory;
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
		fields.addField(FieldFactory.ftext("emailAddress", "Email Address", 30));
		fields.addField(FieldFactory.ftext("firstName", "First Name", 20));
		fields.addField(FieldFactory.ftext("lastName", "Last Name", 20));
		fields.addField(FieldFactory.ftext("mi", "MI", 1));
		fields.addField(FieldFactory.ftext("company", "Company", 20));
		fields.addField(FieldFactory.ftext("attn", "Attn", 10));
		fields.addField(FieldFactory.ftext("address1", "Address 1", 40));
		fields.addField(FieldFactory.ftext("address2", "Address 2", 40));
		fields.addField(FieldFactory.ftext("city", "City", 30));
		fields.addField(FieldFactory.fsuggest("province", "State/Province", AuxDataCache.instance().getRefDataMap(
				RefDataType.US_STATES)));
		fields.addField(FieldFactory.ftext("postalCode", "Zip", 20));
		fields.addField(FieldFactory.fsuggest("country", "Country", AuxDataCache.instance().getRefDataMap(
				RefDataType.ISO_COUNTRY_CODES)));
	}

	@Override
	public void setFieldBindings(Model model, FieldModelBinding bindings) {
		final FieldGroup fields = getFieldGroup();
		bindings.addBinding(fields.getField("emailAddress"), model, "emailAddress");
		bindings.addBinding(fields.getField("firstName"), model, "firstName");
		bindings.addBinding(fields.getField("lastName"), model, "lastName");
		bindings.addBinding(fields.getField("mi"), model, "mi");
		bindings.addBinding(fields.getField("company"), model, "company");
		bindings.addBinding(fields.getField("attn"), model, "attn");
		bindings.addBinding(fields.getField("address1"), model, "address1");
		bindings.addBinding(fields.getField("address2"), model, "address2");
		bindings.addBinding(fields.getField("city"), model, "city");
		bindings.addBinding(fields.getField("province"), model, "province");
		bindings.addBinding(fields.getField("country"), model, "country");
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
