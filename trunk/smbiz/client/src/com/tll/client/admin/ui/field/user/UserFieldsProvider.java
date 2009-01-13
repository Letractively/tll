/**
 * The Logic Lab
 * @author jpk
 * Jan 6, 2009
 */
package com.tll.client.admin.ui.field.user;

import com.tll.client.admin.ui.field.AddressFieldsProvider;
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.TextField;

/**
 * UserFieldsProvider
 * @author jpk
 */
public class UserFieldsProvider extends AbstractFieldGroupProvider {

	@Override
	protected String getFieldGroupName() {
		return "User";
	}

	@Override
	public void populateFieldGroup(FieldGroup fg) {
		addModelCommon(fg, true, true);
		TextField<String> email = femail("userEmailAddress", "emailAddress", "Email Address", "Email Address", 30);
		email.setReadOnly(true);
		fg.addField(email);
		fg.getField("emailAddress").setReadOnly(true);
		fg.addField(fbool("locked", "locked", "Locked", "Locked"));
		fg.addField(fbool("enabled", "enabled", "Enabled", "Enabled"));
		fg.addField(fddate("expires", "expires", "Expires", "Expires"));
		fg.addField("address", (new AddressFieldsProvider()).getFieldGroup());
	}

}