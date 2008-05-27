/**
 * The Logic Lab
 * @author jpk
 * Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import com.tll.client.data.AuxDataRequest;
import com.tll.client.ui.FlowFieldCanvas;
import com.tll.client.ui.field.FieldGroupPanel;
import com.tll.client.ui.field.TextField;

/**
 * BankPanel
 * @author jpk
 */
public final class BankPanel extends FieldGroupPanel {

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
	protected void neededAuxData(AuxDataRequest auxDataRequest) {
		// none
	}

	@Override
	protected void configure() {
		bankName = ftext("paymentData.bankName", "Bank Name", 40);
		bankAccountNo = ftext("paymentData.bankAccountNo", "Account Num", 30);
		bankRoutingNo = ftext("paymentData.bankRoutingNo", "Routing Num", 20);

		fields.addField(bankName);
		fields.addField(bankAccountNo);
		fields.addField(bankRoutingNo);

		FlowFieldCanvas canvas = new FlowFieldCanvas(panel);

		canvas.addField(bankName);
		canvas.addField(bankAccountNo);
		canvas.addField(bankRoutingNo);
	}
}
