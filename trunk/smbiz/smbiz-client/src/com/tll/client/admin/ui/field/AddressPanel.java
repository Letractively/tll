/**
 * The Logic Lab
 * @author jpk Dec 17, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.field.IField;
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
		emailAddress = ftext("emailAddress", "Email Address", IField.LBL_ABOVE, 30);
		lastName = ftext("firstName", "First Name", IField.LBL_ABOVE, 20);
		firstName = ftext("lastName", "Last Name", IField.LBL_ABOVE, 20);
		mi = ftext("mi", "MI", IField.LBL_ABOVE, 1);
		company = ftext("company", "Company", IField.LBL_ABOVE, 20);
		attn = ftext("attn", "Attn", IField.LBL_ABOVE, 10);
		address1 = ftext("address1", "Address 1", IField.LBL_ABOVE, 40);
		address2 = ftext("address2", "Address 2", IField.LBL_ABOVE, 40);
		city = ftext("city", "City", IField.LBL_ABOVE, 30);
		province =
				fsuggest("province", "State/Province", IField.LBL_ABOVE, AuxDataCache.instance().getRefDataMap(
						"usps-state-abbrs"));
		postalCode = ftext("postalCode", "Zip", IField.LBL_ABOVE, 20);
		country =
				fsuggest("country", "Country", IField.LBL_ABOVE, AuxDataCache.instance().getRefDataMap("iso-country-codes"));

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

		HorizontalPanel hp;

		hp = new HorizontalPanel();
		hp.add(emailAddress);
		add(hp);

		hp = new HorizontalPanel();
		hp.add(firstName);
		hp.add(mi);
		hp.add(lastName);
		add(hp);

		hp = new HorizontalPanel();
		hp.add(attn);
		hp.add(company);
		add(hp);

		hp = new HorizontalPanel();
		hp.add(address1);
		add(hp);

		hp = new HorizontalPanel();
		hp.add(address2);
		add(hp);

		hp = new HorizontalPanel();
		hp.add(city);
		hp.add(province);
		add(hp);

		hp = new HorizontalPanel();
		hp.add(postalCode);
		hp.add(country);
		add(hp);
	}

}
