/**
 * The Logic Lab
 * @author jpk Dec 17, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.field.FieldBindingGroup;
import com.tll.client.field.FieldGroup;
import com.tll.client.model.Model;
import com.tll.client.ui.field.AbstractField;
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
	protected void populateFieldBindingGroup(FieldBindingGroup bindings, String parentPropertyPath, FieldGroup fields,
			Model model) {
		bindings.add(createFieldBinding("emailAddress", model, parentPropertyPath));
		bindings.add(createFieldBinding("firstName", model, parentPropertyPath));
		bindings.add(createFieldBinding("lastName", model, parentPropertyPath));
		bindings.add(createFieldBinding("mi", model, parentPropertyPath));
		bindings.add(createFieldBinding("company", model, parentPropertyPath));
		bindings.add(createFieldBinding("attn", model, parentPropertyPath));
		bindings.add(createFieldBinding("address1", model, parentPropertyPath));
		bindings.add(createFieldBinding("address2", model, parentPropertyPath));
		bindings.add(createFieldBinding("city", model, parentPropertyPath));
		bindings.add(createFieldBinding("province", model, parentPropertyPath));
		bindings.add(createFieldBinding("country", model, parentPropertyPath));
	}

	@Override
	protected void draw(Panel canvas, FieldGroup fields) {
		FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
		cmpsr.setCanvas(canvas);

		cmpsr.addField((AbstractField) fields.getField("emailAddress"));

		cmpsr.newRow();
		cmpsr.addField((AbstractField) fields.getField("firstName"));
		cmpsr.addField((AbstractField) fields.getField("mi"));
		cmpsr.addField((AbstractField) fields.getField("lastName"));

		cmpsr.newRow();
		cmpsr.addField((AbstractField) fields.getField("attn"));
		cmpsr.addField((AbstractField) fields.getField("company"));

		cmpsr.newRow();
		cmpsr.addField((AbstractField) fields.getField("address1"));

		cmpsr.newRow();
		cmpsr.addField((AbstractField) fields.getField("address2"));

		cmpsr.newRow();
		cmpsr.addField((AbstractField) fields.getField("city"));
		cmpsr.addField((AbstractField) fields.getField("province"));

		cmpsr.newRow();
		cmpsr.addField((AbstractField) fields.getField("postalCode"));
		cmpsr.addField((AbstractField) fields.getField("country"));
	}
}
