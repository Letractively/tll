/**
 * The Logic Lab
 * @author jpk Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.ui.FlowFieldPanelComposer;
import com.tll.client.ui.field.FieldGroupPanel;
import com.tll.client.ui.field.SelectField;
import com.tll.client.ui.field.SuggestField;
import com.tll.client.ui.field.TextField;
import com.tll.client.util.ClientEnumUtil;
import com.tll.model.impl.CreditCardType;

/**
 * CreditCardPanel
 * @author jpk
 */
public final class CreditCardPanel extends FieldGroupPanel {

	private SelectField type;
	private TextField num;
	private TextField cvv2;
	private TextField expMn;
	private TextField expYr;
	private TextField name;
	private TextField addr1;
	private TextField addr2;
	private TextField city;
	private SuggestField state;
	private TextField zip;
	private SuggestField country;

	/**
	 * Constructor
	 */
	public CreditCardPanel() {
		super("Credit Card");
	}

	@Override
	public void populateFieldGroup() {
		type = fselect("paymentData_ccType", "Type", ClientEnumUtil.toMap(CreditCardType.class));
		num = ftext("paymentData_ccNum", "Num", 15);
		cvv2 = ftext("paymentData_ccCvv2", "CVV2", 4);
		expMn = ftext("paymentData_ccExpMonth", "Exp Month", 2);
		expYr = ftext("paymentData_ccExpYear", "Exp Year", 4);
		name = ftext("paymentData_ccName", "Name", 30);
		addr1 = ftext("paymentData_ccAddress1", "Address 1", 40);
		addr2 = ftext("paymentData_ccAddress2", "Address 2", 40);
		city = ftext("paymentData_ccCity", "City", 30);
		state =
				fsuggest("paymentData_ccState", "State/Province", AuxDataCache.instance().getRefDataMap("usps-state-abbrs"));
		zip = ftext("paymentData_ccZip", "Zip/Postal Code", 15);
		country = fsuggest("paymentData_ccCountry", "Country", AuxDataCache.instance().getRefDataMap("iso-country-codes"));

		addField(type);
		addField(num);
		addField(cvv2);
		addField(expMn);
		addField(expYr);
		addField(name);
		addField(addr1);
		addField(addr2);
		addField(city);
		addField(state);
		addField(zip);
		addField(country);
	}

	@Override
	protected Widget draw() {
		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer();

		canvas.addField(type);

		canvas.addField(num);
		canvas.addField(cvv2);
		canvas.addField(expMn);
		canvas.addField(expYr);

		canvas.newRow();
		canvas.addField(name);

		canvas.newRow();
		canvas.addField(addr1);

		canvas.newRow();
		canvas.addField(addr2);

		canvas.newRow();
		canvas.addField(city);
		canvas.addField(state);

		canvas.newRow();
		canvas.addField(zip);
		canvas.addField(country);

		return canvas.getCanvasWidget();
	}
}