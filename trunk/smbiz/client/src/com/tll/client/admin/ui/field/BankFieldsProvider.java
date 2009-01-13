/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.admin.ui.field;

import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldGroup;

/**
 * BankFieldsProvider
 * @author jpk
 */
public class BankFieldsProvider extends AbstractFieldGroupProvider {

	@Override
	protected String getFieldGroupName() {
		return "Bank Info";
	}

	@Override
	public void populateFieldGroup(FieldGroup fg) {
		fg.addField(fstext("name", "paymentData_bankName", "Bank Name", "Bank Name", 40));
		fg.addField(fstext("accountNo", "paymentData_bankAccountNo", "Account Num", "Account Num", 30));
		fg.addField(fstext("routingNo", "paymentData_bankRoutingNo", "Routing Num", "Routing Num", 20));
	}

}
