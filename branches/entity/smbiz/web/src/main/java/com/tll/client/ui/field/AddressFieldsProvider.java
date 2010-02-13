/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.ui.field;

import com.tll.client.cache.AuxDataCache;
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
		fg.addField(ftext("adrsFirstName", "firstName", "First Name", "First Name", 20));
		fg.addField(ftext("adrsLastName", "lastName", "Last Name", "Last Name", 20));
		fg.addField(ftext("adrsMi", "mi", "MI", "Middle Initial", 1));
		fg.addField(ftext("adrsCompany", "company", "Company", "Company", 20));
		fg.addField(ftext("adrsAttn", "attn", "Attn", "Attention", 10));
		fg.addField(ftext("adrsAddress1", "address1", "Address 1", "Address 1", 40));
		fg.addField(ftext("adrsAddress2", "address2", "Address 2", "Address 2", 40));
		fg.addField(ftext("adrsCity", "city", "City", "City", 30));
		fg.addField(frefdata("adrsProvince", "province", "State/Province", "State/Province", AuxDataCache.get().getRefDataMap(RefDataType.US_STATES)));
		fg.addField(ftext("adrsPostalCode", "postalCode", "Zip", "Zip", 20));
		fg.addField(frefdata("adrsCountry", "country", "Country", "Country", AuxDataCache.get().getRefDataMap(RefDataType.ISO_COUNTRY_CODES)));
	}

}
