/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.admin.ui.field;

import com.tll.client.convert.CharacterToStringConverter;
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
		fg.addField(femail("emailAddress", "emailAddress", "Email Address", "Email Address", 30));
		fg.addField(fstext("firstName", "firstName", "First Name", "First Name", 20));
		fg.addField(fstext("lastName", "lastName", "Last Name", "Last Name", 20));
		fg.addField(FieldFactory.ftext("mi", "mi", "MI", "Middle Initial", 1, CharacterToStringConverter.INSTANCE));
		fg.addField(fstext("company", "company", "Company", "Company", 20));
		fg.addField(fstext("attn", "attn", "Attn", "Attention", 10));
		fg.addField(fstext("address1", "address1", "Address 1", "Address 1", 40));
		fg.addField(fstext("address2", "address2", "Address 2", "Address 2", 40));
		fg.addField(fstext("city", "city", "City", "City", 30));
		fg.addField(frefdata("province", "province", "State/Province", "State/Province", RefDataType.US_STATES));
		fg.addField(fstext("postalCode", "postalCode", "Zip", "Zip", 20));
		fg.addField(frefdata("country", "country", "Country", "Country", RefDataType.ISO_COUNTRY_CODES));
	}

}
