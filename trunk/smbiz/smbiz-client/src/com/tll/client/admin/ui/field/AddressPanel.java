/**
 * The Logic Lab
 * @author jpk Dec 17, 2007
 */
package com.tll.client.admin.ui.field;

import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.ui.FlowFieldCanvas;
import com.tll.client.ui.field.FieldGroupPanel;
import com.tll.client.ui.field.SuggestField;
import com.tll.client.ui.field.TextField;

/**
 * AddressPanel
 * @author jpk
 */
public class AddressPanel extends FieldGroupPanel {

	protected TextField emailAddress;
	protected TextField lastName;
	protected TextField firstName;
	protected TextField mi;
	protected TextField company;
	protected TextField attn;
	protected TextField address1;
	protected TextField address2;
	protected TextField city;
	protected SuggestField province;
	protected TextField postalCode;
	protected SuggestField country;

	/**
	 * Constructor
	 * @param propName
	 */
	public AddressPanel(String propName) {
		super(propName, "Address");
	}

	@Override
	protected void neededAuxData(AuxDataRequest auxDataRequest) {
		auxDataRequest.requestAppRefData("usps-state-abbrs");
		auxDataRequest.requestAppRefData("iso-country-codes");
	}

	@Override
	protected void configure() {
		emailAddress = ftext("emailAddress", "Email Address", 30);
		lastName = ftext("firstName", "First Name", 20);
		firstName = ftext("lastName", "Last Name", 20);
		mi = ftext("mi", "MI", 1);
		company = ftext("company", "Company", 20);
		attn = ftext("attn", "Attn", 10);
		address1 = ftext("address1", "Address 1", 40);
		address2 = ftext("address2", "Address 2", 40);
		city = ftext("city", "City", 30);
		province = fsuggest("province", "State/Province", AuxDataCache.instance().getRefDataMap("usps-state-abbrs"));
		postalCode = ftext("postalCode", "Zip", 20);
		country = fsuggest("country", "Country", AuxDataCache.instance().getRefDataMap("iso-country-codes"));

		fields.addField(emailAddress);
		fields.addField(lastName);
		fields.addField(firstName);
		fields.addField(mi);
		fields.addField(company);
		fields.addField(attn);
		fields.addField(address1);
		fields.addField(address2);
		fields.addField(city);
		fields.addField(province);
		fields.addField(postalCode);
		fields.addField(country);

		FlowFieldCanvas canvas = new FlowFieldCanvas(panel);

		canvas.addField(emailAddress);

		canvas.newRow();
		canvas.addField(firstName);
		canvas.addField(firstName);
		canvas.addField(mi);
		canvas.addField(lastName);

		canvas.newRow();
		canvas.addField(attn);
		canvas.addField(company);

		canvas.newRow();
		canvas.addField(address1);

		canvas.newRow();
		canvas.addField(address2);

		canvas.newRow();
		canvas.addField(city);
		canvas.addField(province);

		canvas.newRow();
		canvas.addField(postalCode);
		canvas.addField(country);
	}

}
