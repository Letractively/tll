/**
 * The Logic Lab
 * @author jpk
 * Jan 6, 2009
 */
package com.tll.client.ui.field.user;

import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.AddressFieldsProvider;
import com.tll.client.ui.field.FieldFactory;
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
		final TextField email = femail("userEmailAddress", "emailAddress", "Email Address", "Email Address", 30);
		email.setReadOnly(true);
		fg.addField(email);
		fg.getField("userEmailAddress").setReadOnly(true);
		fg.addField(FieldFactory.fcheckbox("userLocked", "locked", "Locked", "Locked"));
		fg.addField(FieldFactory.fcheckbox("userEnabled", "enabled", "Enabled", "Enabled"));
		fg.addField(FieldFactory.fdate("userExpires", "expires", "Expires", "Expires"));
		fg.addField("address", (new AddressFieldsProvider()).getFieldGroup());
	}

}