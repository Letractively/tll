/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.admin.ui.field;

import java.util.Arrays;

import com.tll.client.cache.AuxDataCache;
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.model.impl.CreditCardType;
import com.tll.service.app.RefDataType;

/**
 * CreditCardFieldsProvider
 * @author jpk
 */
public class CreditCardFieldsProvider extends AbstractFieldGroupProvider {

	@Override
	public void populateFieldGroup(FieldGroup fg) {
		fg
				.addField(FieldFactory.fselect("paymentData_ccType", "Type", null, false, Arrays
						.asList(CreditCardType.values())));
		fg.addField(FieldFactory.fcreditcard("paymentData_ccNum", "Num", null, 15));
		fg.addField(FieldFactory.ftext("paymentData_ccCvv2", "CVV2", "CVV2", 4));
		fg.addField(FieldFactory.ftext("paymentData_ccExpMonth", "Exp Month", "Expiration Month", 2));
		fg.addField(FieldFactory.ftext("paymentData_ccExpYear", "Exp Year", "Expiration Year", 4));
		fg.addField(FieldFactory.ftext("paymentData_ccName", "Name", "Name", 30));
		fg.addField(FieldFactory.ftext("paymentData_ccAddress1", "Address 1", "Address 1", 40));
		fg.addField(FieldFactory.ftext("paymentData_ccAddress2", "Address 2", "Address 2", 40));
		fg.addField(FieldFactory.ftext("paymentData_ccCity", "City", "City", 30));
		fg.addField(FieldFactory.fsuggest("paymentData_ccState", "State/Province", "State", AuxDataCache.instance()
				.getRefDataMap(RefDataType.US_STATES).values()));
		fg.addField(FieldFactory.ftext("paymentData_ccZip", "Postal Code", "Postal Code", 15));
		fg.addField(FieldFactory.fsuggest("paymentData_ccCountry", "Country", "Country", AuxDataCache.instance()
				.getRefDataMap(RefDataType.ISO_COUNTRY_CODES).values()));
	}

}
