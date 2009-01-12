/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.admin.ui.field;

import com.tll.client.convert.IFormattedConverter;
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.util.GlobalFormat;
import com.tll.model.impl.CreditCardType;
import com.tll.service.app.RefDataType;

/**
 * CreditCardFieldsProvider
 * @author jpk
 */
public class CreditCardFieldsProvider extends AbstractFieldGroupProvider {

	private static IFormattedConverter<String, Integer> noFormatIntToStringConverter =
			new IFormattedConverter<String, Integer>() {

				public String convert(Integer o) throws IllegalArgumentException {
					return o == null ? "" : o.toString();
				}

				public GlobalFormat getFormat() {
					return null;
				}
			};

	@Override
	public void populateFieldGroup(FieldGroup fg) {
		fg.addField(fenumselect("type", "paymentData_ccType", "Type", "Type", CreditCardType.class));
		fg.addField(fcreditcard("num", "paymentData_ccNum", "Num", null, 15));
		fg.addField(fstext("cvv2", "paymentData_ccCvv2", "CVV2", "CVV2", 4));
		fg.addField(FieldFactory.ftext("expMonth", "paymentData_ccExpMonth", "Exp Month", "Expiration Month", 2,
				noFormatIntToStringConverter));
		fg.addField(FieldFactory.ftext("expYear", "paymentData_ccExpYear", "Exp Year", "Expiration Year", 4,
				noFormatIntToStringConverter));
		fg.addField(fstext("name", "paymentData_ccName", "Name", "Name", 30));
		fg.addField(fstext("address1", "paymentData_ccAddress1", "Address 1", "Address 1", 40));
		fg.addField(fstext("address2", "paymentData_ccAddress2", "Address 2", "Address 2", 40));
		fg.addField(fstext("city", "paymentData_ccCity", "City", "City", 30));
		fg.addField(frefdata("state", "paymentData_ccState", "State/Province", "State", RefDataType.US_STATES));
		fg.addField(fstext("zip", "paymentData_ccZip", "Postal Code", "Postal Code", 15));
		fg.addField(frefdata("country", "paymentData_ccCountry", "Country", "Country", RefDataType.ISO_COUNTRY_CODES));
	}

}
