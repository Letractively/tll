/**
 * The Logic Lab
 * @author jpk Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.field.IField;
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
		type = fselect("paymentData.ccType", "Type", IField.LBL_ABOVE, ClientEnumUtil.toMap(CreditCardType.class));
		num = ftext("paymentData.ccNum", "Num", IField.LBL_ABOVE, 15);
		cvv2 = ftext("paymentData.ccCvv2", "CVV2", IField.LBL_ABOVE, 4);
		expMn = ftext("paymentData.ccExpMonth", "Exp Month", IField.LBL_ABOVE, 2);
		expYr = ftext("paymentData.ccExpYear", "Exp Year", IField.LBL_ABOVE, 4);
		name = ftext("paymentData.ccName", "Name", IField.LBL_ABOVE, 30);
		addr1 = ftext("paymentData.ccAddress1", "Address 1", IField.LBL_ABOVE, 40);
		addr2 = ftext("paymentData.ccAddress2", "Address 2", IField.LBL_ABOVE, 40);
		city = ftext("paymentData.ccCity", "City", IField.LBL_ABOVE, 30);
		state =
				fsuggest("paymentData.ccState", "State/Province", IField.LBL_ABOVE, AuxDataCache.instance().getRefDataMap(
						"usps-state-abbrs"));
		zip = ftext("paymentData.ccZip", "Zip/Postal Code", IField.LBL_ABOVE, 15);
		country =
				fsuggest("paymentData.ccCountry", "Country", IField.LBL_ABOVE, AuxDataCache.instance().getRefDataMap(
						"iso-country-codes"));

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

		HorizontalPanel hp;

		add(type);

		hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.add(num);
		hp.add(cvv2);
		hp.add(expMn);
		hp.add(expYr);
		add(hp);

		add(name);
		add(addr1);
		add(addr2);

		hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.add(city);
		hp.add(state);
		add(hp);

		hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.add(zip);
		hp.add(country);
		add(hp);
	}
}
