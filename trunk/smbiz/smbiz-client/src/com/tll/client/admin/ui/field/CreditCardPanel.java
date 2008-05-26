/**
 * The Logic Lab
 * @author jpk Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.ui.FlowFieldCanvas;
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

	protected SelectField type;
	protected TextField num;
	protected TextField cvv2;
	protected TextField expMn;
	protected TextField expYr;
	protected TextField name;
	protected TextField addr1;
	protected TextField addr2;
	protected TextField city;
	protected SuggestField state;
	protected TextField zip;
	protected SuggestField country;

	/**
	 * Constructor
	 * @param propName
	 */
	public CreditCardPanel(String propName) {
		super(propName, "Credit Card");
	}

	@Override
	protected void neededAuxData(AuxDataRequest auxDataRequest) {
		auxDataRequest.requestAppRefData("usps-state-abbrs");
		auxDataRequest.requestAppRefData("iso-country-codes");
	}

	@Override
	protected void configure() {
		type = fselect("paymentData.ccType", "Type", ClientEnumUtil.toMap(CreditCardType.class));
		num = ftext("paymentData.ccNum", "Num", 15);
		cvv2 = ftext("paymentData.ccCvv2", "CVV2", 4);
		expMn = ftext("paymentData.ccExpMonth", "Exp Month", 2);
		expYr = ftext("paymentData.ccExpYear", "Exp Year", 4);
		name = ftext("paymentData.ccName", "Name", 30);
		addr1 = ftext("paymentData.ccAddress1", "Address 1", 40);
		addr2 = ftext("paymentData.ccAddress2", "Address 2", 40);
		city = ftext("paymentData.ccCity", "City", 30);
		state =
				fsuggest("paymentData.ccState", "State/Province", AuxDataCache.instance().getRefDataMap("usps-state-abbrs"));
		zip = ftext("paymentData.ccZip", "Zip/Postal Code", 15);
		country = fsuggest("paymentData.ccCountry", "Country", AuxDataCache.instance().getRefDataMap("iso-country-codes"));

		fields.addField(type);
		fields.addField(num);
		fields.addField(cvv2);
		fields.addField(expMn);
		fields.addField(expYr);
		fields.addField(name);
		fields.addField(addr1);
		fields.addField(addr2);
		fields.addField(city);
		fields.addField(state);
		fields.addField(zip);
		fields.addField(country);

		FlowFieldCanvas canvas = new FlowFieldCanvas(panel);

		canvas.addField(type);

		canvas.addField(num);
		canvas.addField(cvv2);
		canvas.addField(expMn);
		canvas.addField(expYr);

		canvas.newRow();
		canvas.addField(name);
		canvas.addField(addr1);
		canvas.addField(addr2);

		canvas.newRow();
		canvas.addField(city);
		canvas.addField(state);

		canvas.newRow();
		canvas.addField(zip);
		canvas.addField(country);
	}
}
