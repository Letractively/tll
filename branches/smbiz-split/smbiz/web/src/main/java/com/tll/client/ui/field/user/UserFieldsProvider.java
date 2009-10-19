/**
 * The Logic Lab
 * @author jpk
 * Jan 6, 2009
 */
package com.tll.client.ui.field.user;

import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.AddressFieldsProvider;
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
		addModelCommon(fg, true, true, "user");
		final TextField email = femail("userEmailAddress", "emailAddress", "Username", "Email Address/Username", 30);
		email.setReadOnly(true);
		fg.addField(email);
		fg.addField(fcheckbox("userLocked", "locked", "Locked", "Locked"));
		fg.addField(fcheckbox("userEnabled", "enabled", "Enabled", "Enabled"));
		fg.addField(fdate("userExpires", "expires", "Expires", "Expires"));
		fg.addField("address", (new AddressFieldsProvider()).getFieldGroup());
	}

}