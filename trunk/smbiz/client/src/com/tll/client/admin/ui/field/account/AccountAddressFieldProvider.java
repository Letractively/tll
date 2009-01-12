/**
 * The Logic Lab
 * @author jpk
 * Jan 6, 2009
 */
package com.tll.client.admin.ui.field.account;

import com.tll.client.admin.ui.field.AddressFieldsProvider;
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldGroup;

final class AccountAddressFieldProvider extends AbstractFieldGroupProvider {

	@Override
	protected void populateFieldGroup(FieldGroup fg) {
		addModelCommon(fg, true, true);
		FieldGroup fgAddress = (new AddressFieldsProvider()).getFieldGroup();
		fgAddress.setName("address");
		fg.addField("address", fgAddress);
	}
}