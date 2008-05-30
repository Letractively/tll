/**
 * The Logic Lab
 * @author jpk
 * Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import com.tll.client.data.AuxDataRequest;
import com.tll.client.ui.field.FieldModelBinding;
import com.tll.client.ui.field.TextField;

/**
 * BankPanel
 * @author jpk
 */
public final class BankPanel extends FieldModelBinding {

	protected TextField bankName;
	protected TextField bankAccountNo;
	protected TextField bankRoutingNo;

	/**
	 * Constructor
	 * @param propName
	 */
	public BankPanel(String propName) {
		super(propName, "Bank Info");
	}

	@Override
	public void neededAuxData(AuxDataRequest auxDataRequest) {
		// none
	}

	@Override
	protected void doInitFields() {
		bankName = ftext("paymentData.bankName", "Bank Name", 40);
		bankAccountNo = ftext("paymentData.bankAccountNo", "Account Num", 30);
		bankRoutingNo = ftext("paymentData.bankRoutingNo", "Routing Num", 20);

		fields.addField(bankName);
		fields.addField(bankAccountNo);
		fields.addField(bankRoutingNo);

		/*
		VerticalFieldPanelComposer canvas = new VerticalFieldPanelComposer(panel);

		canvas.addField(bankName);
		canvas.addField(bankAccountNo);
		canvas.addField(bankRoutingNo);
		*/
	}
}
