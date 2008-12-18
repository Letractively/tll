/**
 * The Logic Lab
 * @author jpk Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.FieldModelBinding;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowFieldPanelComposer;
import com.tll.client.ui.field.SelectField;
import com.tll.client.ui.field.SuggestField;
import com.tll.client.ui.field.TextField;
import com.tll.client.util.ClientEnumUtil;
import com.tll.client.validate.CreditCardValidator;
import com.tll.model.impl.CreditCardType;
import com.tll.service.app.RefDataType;

/**
 * CreditCardPanel
 * @author jpk
 */
public final class CreditCardPanel extends FieldPanel {

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
	public void populateFieldGroup(FieldGroup fields) {
		type = FieldFactory.fselect("paymentData_ccType", "Type", ClientEnumUtil.toMap(CreditCardType.class));
		num = FieldFactory.ftext("paymentData_ccNum", "Num", 15);
		num.addValidator(CreditCardValidator.INSTANCE);
		cvv2 = FieldFactory.ftext("paymentData_ccCvv2", "CVV2", 4);
		expMn = FieldFactory.ftext("paymentData_ccExpMonth", "Exp Month", 2);
		expYr = FieldFactory.ftext("paymentData_ccExpYear", "Exp Year", 4);
		name = FieldFactory.ftext("paymentData_ccName", "Name", 30);
		addr1 = FieldFactory.ftext("paymentData_ccAddress1", "Address 1", 40);
		addr2 = FieldFactory.ftext("paymentData_ccAddress2", "Address 2", 40);
		city = FieldFactory.ftext("paymentData_ccCity", "City", 30);
		state =
				FieldFactory.fsuggest("paymentData_ccState", "State/Province", AuxDataCache.instance().getRefDataMap(
						RefDataType.US_STATES));
		zip = FieldFactory.ftext("paymentData_ccZip", "Zip/Postal Code", 15);
		country =
				FieldFactory.fsuggest("paymentData_ccCountry", "Country", AuxDataCache.instance().getRefDataMap(
						RefDataType.ISO_COUNTRY_CODES));

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
	public void addFieldBindings(FieldModelBinding bindingDef, String modelPropertyPath) {
		bindingDef.addBinding(type, modelPropertyPath);
		bindingDef.addBinding(num, modelPropertyPath);
		bindingDef.addBinding(cvv2, modelPropertyPath);
		bindingDef.addBinding(expMn, modelPropertyPath);
		bindingDef.addBinding(expYr, modelPropertyPath);
		bindingDef.addBinding(name, modelPropertyPath);
		bindingDef.addBinding(addr1, modelPropertyPath);
		bindingDef.addBinding(addr2, modelPropertyPath);
		bindingDef.addBinding(city, modelPropertyPath);
		bindingDef.addBinding(state, modelPropertyPath);
		bindingDef.addBinding(zip, modelPropertyPath);
		bindingDef.addBinding(country, modelPropertyPath);
	}

	@Override
	protected void drawInternal(Panel canvas) {
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
