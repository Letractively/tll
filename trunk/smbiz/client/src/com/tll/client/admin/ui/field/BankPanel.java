/**
 * The Logic Lab
 * @author jpk
 * Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.VerticalFieldPanelComposer;
import com.tll.client.ui.field.FieldGroupPanel;
import com.tll.client.ui.field.TextField;

/**
 * BankPanel
 * @author jpk
 */
public final class BankPanel extends FieldGroupPanel {

	private TextField bankName;
	private TextField bankAccountNo;
	private TextField bankRoutingNo;

	/**
	 * Constructor
	 */
	public BankPanel() {
		super("Bank Info");
	}

	@Override
	public void populateFieldGroup() {
		bankName = ftext("paymentData_bankName", "Bank Name", 40);
		bankAccountNo = ftext("paymentData_bankAccountNo", "Account Num", 30);
		bankRoutingNo = ftext("paymentData_bankRoutingNo", "Routing Num", 20);

		addField(bankName);
		addField(bankAccountNo);
		addField(bankRoutingNo);
	}

	@Override
	protected Widget draw() {
		VerticalFieldPanelComposer canvas = new VerticalFieldPanelComposer();

		canvas.addField(bankName);
		canvas.addField(bankAccountNo);
		canvas.addField(bankRoutingNo);

		return canvas.getCanvasWidget();
	}
}
