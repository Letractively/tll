/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.admin.ui.field;

import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.IFieldGroupProvider;

/**
 * BankFieldsProvider
 * @author jpk
 */
public class BankFieldsProvider implements IFieldGroupProvider {

	public FieldGroup getFieldGroup() {
		FieldGroup fg = new FieldGroup();

		fg.addField(FieldFactory.ftext("paymentData_bankName", "Bank Name", "Bank Name", 40));
		fg.addField(FieldFactory.ftext("paymentData_bankAccountNo", "Account Num", "Account Num", 30));
		fg.addField(FieldFactory.ftext("paymentData_bankRoutingNo", "Routing Num", "Routing Num", 20));

		return fg;
	}

}
