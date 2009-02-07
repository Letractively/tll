/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.admin.ui.field;

import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.common.convert.CharacterToStringConverter;
import com.tll.refdata.RefDataType;

/**
 * AddressFieldsProvider
 * @author jpk
 */
public class AddressFieldsProvider extends AbstractFieldGroupProvider {

	@Override
	protected String getFieldGroupName() {
		return "Address";
	}

	@Override
	public void populateFieldGroup(FieldGroup fg) {
		fg.addField(femail("adrsEmailAddress", "emailAddress", "Email Address", "Email Address", 30));
		fg.addField(fstext("adrsFirstName", "firstName", "First Name", "First Name", 20));
		fg.addField(fstext("adrsLastName", "lastName", "Last Name", "Last Name", 20));
		fg.addField(FieldFactory.ftext("adrsMi", "mi", "MI", "Middle Initial", 1, CharacterToStringConverter.INSTANCE));
		fg.addField(fstext("adrsCompany", "company", "Company", "Company", 20));
		fg.addField(fstext("adrsAttn", "attn", "Attn", "Attention", 10));
		fg.addField(fstext("adrsAddress1", "address1", "Address 1", "Address 1", 40));
		fg.addField(fstext("adrsAddress2", "address2", "Address 2", "Address 2", 40));
		fg.addField(fstext("adrsCity", "city", "City", "City", 30));
		fg.addField(frefdata("adrsProvince", "province", "State/Province", "State/Province", RefDataType.US_STATES));
		fg.addField(fstext("adrsPostalCode", "postalCode", "Zip", "Zip", 20));
		fg.addField(frefdata("adrsCountry", "country", "Country", "Country", RefDataType.ISO_COUNTRY_CODES));
	}

}
