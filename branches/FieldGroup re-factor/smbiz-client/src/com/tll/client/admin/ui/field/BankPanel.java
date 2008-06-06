/**
 * The Logic Lab
 * @author jpk
 * Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.ui.VerticalFieldPanelComposer;
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
	 */
	public BankPanel() {
		super("Bank Info");
	}

	@Override
	public void neededAuxData(AuxDataRequest auxDataRequest) {
		// none
	}

	@Override
	protected Widget doInit() {
		bankName = ftext("paymentData_bankName", "Bank Name", 40);
		bankAccountNo = ftext("paymentData_bankAccountNo", "Account Num", 30);
		bankRoutingNo = ftext("paymentData_bankRoutingNo", "Routing Num", 20);

		fields.addField(bankName);
		fields.addField(bankAccountNo);
		fields.addField(bankRoutingNo);

		VerticalFieldPanelComposer canvas = new VerticalFieldPanelComposer();

		canvas.addField(bankName);
		canvas.addField(bankAccountNo);
		canvas.addField(bankRoutingNo);

		return canvas.getWidget();
	}
}
