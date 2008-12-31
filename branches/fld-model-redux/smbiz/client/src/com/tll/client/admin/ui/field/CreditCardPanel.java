/**
 * The Logic Lab
 * @author jpk Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import java.util.Arrays;

import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.bind.IBindable;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowFieldPanelComposer;
import com.tll.client.ui.field.SelectField;
import com.tll.client.ui.field.SuggestField;
import com.tll.client.ui.field.TextField;
import com.tll.model.impl.CreditCardType;
import com.tll.service.app.RefDataType;

/**
 * CreditCardPanel
 * @author jpk
 */
public final class CreditCardPanel<M extends IBindable> extends FieldPanel<M> {

	private final FlowPanel canvas = new FlowPanel();

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
		initWidget(canvas);
	}

	@Override
	public void populateFieldGroup(FieldGroup fields) {
		type = FieldFactory.fselect("paymentData_ccType", "Type", null, false, Arrays.asList(CreditCardType.values()));
		num = FieldFactory.fcreditcard("paymentData_ccNum", "Num", null, 15);
		cvv2 = FieldFactory.ftext("paymentData_ccCvv2", "CVV2", "CVV2", 4);
		expMn = FieldFactory.ftext("paymentData_ccExpMonth", "Exp Month", "Expiration Month", 2);
		expYr = FieldFactory.ftext("paymentData_ccExpYear", "Exp Year", "Expiration Year", 4);
		name = FieldFactory.ftext("paymentData_ccName", "Name", "Name", 30);
		addr1 = FieldFactory.ftext("paymentData_ccAddress1", "Address 1", "Address 1", 40);
		addr2 = FieldFactory.ftext("paymentData_ccAddress2", "Address 2", "Address 2", 40);
		city = FieldFactory.ftext("paymentData_ccCity", "City", "City", 30);
		state =
				FieldFactory.fsuggest("paymentData_ccState", "State/Province", "State", AuxDataCache.instance().getRefDataMap(
						RefDataType.US_STATES).values());
		zip = FieldFactory.ftext("paymentData_ccZip", "Postal Code", "Postal Code", 15);
		country =
				FieldFactory.fsuggest("paymentData_ccCountry", "Country", "Country", AuxDataCache.instance().getRefDataMap(
						RefDataType.ISO_COUNTRY_CODES).values());

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
	}

	@Override
	protected void draw() {
		final FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
		cmpsr.setCanvas(canvas);

		cmpsr.addField(type);

		cmpsr.addField(num);
		cmpsr.addField(cvv2);
		cmpsr.addField(expMn);
		cmpsr.addField(expYr);

		cmpsr.newRow();
		cmpsr.addField(name);

		cmpsr.newRow();
		cmpsr.addField(addr1);

		cmpsr.newRow();
		cmpsr.addField(addr2);

		cmpsr.newRow();
		cmpsr.addField(city);
		cmpsr.addField(state);

		cmpsr.newRow();
		cmpsr.addField(zip);
		cmpsr.addField(country);
	}
}
