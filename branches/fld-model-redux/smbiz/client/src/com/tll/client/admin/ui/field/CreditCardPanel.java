/**
 * The Logic Lab
 * @author jpk Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.field.FieldBindingGroup;
import com.tll.client.field.FieldGroup;
import com.tll.client.model.Model;
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
	protected void populateFieldBindingGroup(FieldBindingGroup bindings, String parentPropertyPath, FieldGroup fields,
			Model model) {
		bindings.add(createFieldBinding("paymentData_ccType", model, parentPropertyPath));
		bindings.add(createFieldBinding("paymentData_ccNum", model, parentPropertyPath));
		bindings.add(createFieldBinding("paymentData_ccCvv2", model, parentPropertyPath));
		bindings.add(createFieldBinding("paymentData_ccExpMonth", model, parentPropertyPath));
		bindings.add(createFieldBinding("paymentData_ccExpYear", model, parentPropertyPath));
		bindings.add(createFieldBinding("paymentData_ccName", model, parentPropertyPath));
		bindings.add(createFieldBinding("paymentData_ccAddress1", model, parentPropertyPath));
		bindings.add(createFieldBinding("paymentData_ccAddress2", model, parentPropertyPath));
		bindings.add(createFieldBinding("paymentData_ccCity", model, parentPropertyPath));
		bindings.add(createFieldBinding("paymentData_ccState", model, parentPropertyPath));
		bindings.add(createFieldBinding("paymentData_ccZip", model, parentPropertyPath));
		bindings.add(createFieldBinding("paymentData_ccCountry", model, parentPropertyPath));
	}

	@Override
	protected void draw(Panel canvas, FieldGroup fields) {
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
