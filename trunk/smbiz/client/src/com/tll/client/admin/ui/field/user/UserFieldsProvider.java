/**
 * The Logic Lab
 * @author jpk
 * Jan 6, 2009
 */
package com.tll.client.admin.ui.field.user;

import com.tll.client.admin.ui.field.AddressFieldsProvider;
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.util.GlobalFormat;

/**
 * UserFieldsProvider
 * @author jpk
 */
public class UserFieldsProvider extends AbstractFieldGroupProvider {

	@Override
	public void populateFieldGroup(FieldGroup fg) {
		addModelCommon(fg, true, true);
		fg.addField(FieldFactory.femail("userEmailAddress", "emailAddress", "Email Address", "Email Address", 30));
		fg.getField("emailAddress").setReadOnly(true);
		fg.addField(FieldFactory.fcheckbox("locked", "locked", "Locked", "Locked"));
		fg.addField(FieldFactory.fcheckbox("enabled", "enabled", "Enabled", "Enabled"));
		fg.addField(FieldFactory.fdate("expires", "expires", "Expires", "Expires", GlobalFormat.DATE));
		fg.addField("address", (new AddressFieldsProvider()).getFieldGroup());
	}

}