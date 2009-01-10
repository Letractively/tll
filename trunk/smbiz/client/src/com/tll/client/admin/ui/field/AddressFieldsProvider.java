/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.admin.ui.field;

import com.tll.client.cache.AuxDataCache;
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.service.app.RefDataType;

/**
 * AddressFieldsProvider
 * @author jpk
 */
public class AddressFieldsProvider extends AbstractFieldGroupProvider {

	@Override
	public void populateFieldGroup(FieldGroup fg) {
		fg.addField(FieldFactory.femail("emailAddress", "emailAddress", "Email Address", "Email Address", 30));
		fg.addField(FieldFactory.ftext("firstName", "firstName", "First Name", "First Name", 20));
		fg.addField(FieldFactory.ftext("lastName", "lastName", "Last Name", "Last Name", 20));
		fg.addField(FieldFactory.ftext("mi", "mi", "MI", "Middle Initial", 1));
		fg.addField(FieldFactory.ftext("company", "company", "Company", "Company", 20));
		fg.addField(FieldFactory.ftext("attn", "attn", "Attn", "Attention", 10));
		fg.addField(FieldFactory.ftext("address1", "address1", "Address 1", "Address 1", 40));
		fg.addField(FieldFactory.ftext("address2", "address2", "Address 2", "Address 2", 40));
		fg.addField(FieldFactory.ftext("city", "city", "City", "City", 30));
		fg.addField(FieldFactory.fsuggest("province", "province", "State/Province", "State/Province", AuxDataCache
				.instance().getRefDataMap(RefDataType.US_STATES).values()));
		fg.addField(FieldFactory.ftext("postalCode", "postalCode", "Zip", "Zip", 20));
		fg.addField(FieldFactory.fsuggest("country", "country", "Country", "Country", AuxDataCache.instance()
				.getRefDataMap(RefDataType.ISO_COUNTRY_CODES).values()));
	}

}
