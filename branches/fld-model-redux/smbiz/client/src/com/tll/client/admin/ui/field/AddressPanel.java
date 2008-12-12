/**
 * The Logic Lab
 * @author jpk Dec 17, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroupPanel;
import com.tll.client.ui.field.FlowFieldPanelComposer;
import com.tll.client.ui.field.SuggestField;
import com.tll.client.ui.field.TextField;
import com.tll.service.app.RefDataType;

/**
 * AddressPanel
 * @author jpk
 */
public final class AddressPanel extends FieldGroupPanel {

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
		super("Address");
	}

	@Override
	public void populateFieldGroup() {
		emailAddress = FieldFactory.ftext("emailAddress", "Email Address", 30);
		lastName = FieldFactory.ftext("firstName", "First Name", 20);
		firstName = FieldFactory.ftext("lastName", "Last Name", 20);
		mi = FieldFactory.ftext("mi", "MI", 1);
		company = FieldFactory.ftext("company", "Company", 20);
		attn = FieldFactory.ftext("attn", "Attn", 10);
		address1 = FieldFactory.ftext("address1", "Address 1", 40);
		address2 = FieldFactory.ftext("address2", "Address 2", 40);
		city = FieldFactory.ftext("city", "City", 30);
		province =
				FieldFactory.fsuggest("province", "State/Province", AuxDataCache.instance()
						.getRefDataMap(RefDataType.US_STATES));
		postalCode = FieldFactory.ftext("postalCode", "Zip", 20);
		country =
				FieldFactory.fsuggest("country", "Country", AuxDataCache.instance()
						.getRefDataMap(RefDataType.ISO_COUNTRY_CODES));

		addField(emailAddress);
		addField(lastName);
		addField(firstName);
		addField(mi);
		addField(company);
		addField(attn);
		addField(address1);
		addField(address2);
		addField(city);
		addField(province);
		addField(postalCode);
		addField(country);
	}

	@Override
	protected void draw(Panel canvas) {
		FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
		cmpsr.setCanvas(canvas);

		cmpsr.addField(emailAddress);

		cmpsr.newRow();
		cmpsr.addField(firstName);
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
