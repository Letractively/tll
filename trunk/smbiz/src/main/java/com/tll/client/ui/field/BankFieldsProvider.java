/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.ui.field;


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
		fg.addField(ftext("bankName", "paymentData_bankName", "Bank Name", "Bank Name", 40));
		fg.addField(ftext("bankAccountNo", "paymentData_bankAccountNo", "Account Num", "Account Num", 30));
		fg.addField(ftext("bankRoutingNo", "paymentData_bankRoutingNo", "Routing Num", "Routing Num", 20));
	}

}
