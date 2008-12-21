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
import com.tll.client.ui.field.SuggestField;
import com.tll.client.ui.field.TextField;
import com.tll.service.app.RefDataType;

/**
 * AddressPanel
 * @author jpk
 */
public final class AddressPanel extends FieldPanel {

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
	public void populateFieldGroup(FieldGroup fields) {
		emailAddress = femail("emailAddress", "Email Address", 30);
		fields.addField(emailAddress);

		firstName = ftext("firstName", "First Name", 20);
		fields.addField(firstName);

		lastName = ftext("lastName", "Last Name", 20);
		fields.addField(lastName);

		mi = ftext("mi", "MI", 1);
		fields.addField(mi);

		company = ftext("company", "Company", 20);
		fields.addField(company);

		attn = ftext("attn", "Attn", 10);
		fields.addField(attn);

		address1 = ftext("address1", "Address 1", 40);
		fields.addField(address1);

		address2 = ftext("address2", "Address 2", 40);
		fields.addField(address2);

		city = ftext("city", "City", 30);
		fields.addField(city);

		province = fsuggest("province", "State/Province", AuxDataCache.instance().getRefDataMap(RefDataType.US_STATES));
		fields.addField(province);

		postalCode = ftext("postalCode", "Zip", 20);
		fields.addField(postalCode);

		country = fsuggest("country", "Country", AuxDataCache.instance().getRefDataMap(RefDataType.ISO_COUNTRY_CODES));
		fields.addField(country);
	}

	@Override
	protected void drawInternal(Panel canvas) {
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
