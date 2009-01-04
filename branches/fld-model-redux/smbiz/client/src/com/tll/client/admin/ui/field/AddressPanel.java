/**
 * The Logic Lab
 * @author jpk Dec 17, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.bind.IBindable;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowFieldPanelComposer;
import com.tll.client.ui.field.SuggestField;
import com.tll.client.ui.field.TextField;
import com.tll.service.app.RefDataType;

/**
 * AddressPanel
 * @author jpk
 */
public final class AddressPanel<M extends IBindable> extends FieldPanel<M> {

	private final FlowPanel canvas = new FlowPanel();

	private TextField emailAddress;
	private TextField lastName;
	private TextField firstName;
	private TextField mi;
	private TextField company;
	private TextField attn;
	private TextField address1;
	private TextField address2;
	private TextField city;
	private SuggestField province;
	private TextField postalCode;
	private SuggestField country;

	/**
	 * Constructor
	 */
	public AddressPanel() {
		super();
		initWidget(canvas);
	}

	@Override
	public void populateFieldGroup(FieldGroup fields) {
		emailAddress = FieldFactory.femail("emailAddress", "Email Address", "Email Address", 30);
		fields.addField(emailAddress);

		firstName = FieldFactory.ftext("firstName", "First Name", "First Name", 20);
		fields.addField(firstName);

		lastName = FieldFactory.ftext("lastName", "Last Name", "Last Name", 20);
		fields.addField(lastName);

		mi = FieldFactory.ftext("mi", "MI", "Middle Initial", 1);
		fields.addField(mi);

		company = FieldFactory.ftext("company", "Company", "Company", 20);
		fields.addField(company);

		attn = FieldFactory.ftext("attn", "Attn", "Attention", 10);
		fields.addField(attn);

		address1 = FieldFactory.ftext("address1", "Address 1", "Address 1", 40);
		fields.addField(address1);

		address2 = FieldFactory.ftext("address2", "Address 2", "Address 2", 40);
		fields.addField(address2);

		city = FieldFactory.ftext("city", "City", "City", 30);
		fields.addField(city);

		province =
				FieldFactory.fsuggest("province", "State/Province", "State/Province", AuxDataCache.instance().getRefDataMap(
						RefDataType.US_STATES).values());
		fields.addField(province);

		postalCode = FieldFactory.ftext("postalCode", "Zip", "Zip", 20);
		fields.addField(postalCode);

		country =
				FieldFactory.fsuggest("country", "Country", "Country", AuxDataCache.instance().getRefDataMap(
						RefDataType.ISO_COUNTRY_CODES).values());
		fields.addField(country);
	}

	@Override
	protected void draw() {
		FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
		cmpsr.setCanvas(canvas);

		cmpsr.addField(emailAddress);

		cmpsr.newRow();
		cmpsr.addField(firstName);
		cmpsr.addField(mi);
		cmpsr.addField(lastName);

		cmpsr.newRow();
		cmpsr.addField(attn);
		cmpsr.addField(company);

		cmpsr.newRow();
		cmpsr.addField(address1);

		cmpsr.newRow();
		cmpsr.addField(address2);

		cmpsr.newRow();
		cmpsr.addField(city);
		cmpsr.addField(province);

		cmpsr.newRow();
		cmpsr.addField(postalCode);
		cmpsr.addField(country);
	}
}
