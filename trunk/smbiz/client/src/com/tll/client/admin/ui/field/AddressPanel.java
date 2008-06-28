/**
 * The Logic Lab
 * @author jpk Dec 17, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.ui.FlowFieldPanelComposer;
import com.tll.client.ui.field.FieldGroupPanel;
import com.tll.client.ui.field.SuggestField;
import com.tll.client.ui.field.TextField;

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
	protected Widget draw() {
		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer();

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

		return canvas.getCanvasWidget();
	}

	@Override
	public void populateFieldGroup() {
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
}
