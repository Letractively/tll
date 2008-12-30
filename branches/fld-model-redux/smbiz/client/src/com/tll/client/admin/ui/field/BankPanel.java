/**
 * The Logic Lab
 * @author jpk
 * Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.TextField;
import com.tll.client.ui.field.VerticalFieldPanelComposer;

/**
 * BankPanel
 * @author jpk
 */
public final class BankPanel<M> extends FieldPanel<M> {

	private final FlowPanel canvas = new FlowPanel();
	private TextField bankName;
	private TextField bankAccountNo;
	private TextField bankRoutingNo;

	/**
	 * Constructor
	 */
	public BankPanel() {
		super("Bank Info");
		initWidget(canvas);
	}

	@Override
	public void populateFieldGroup(FieldGroup fields) {
		bankName = FieldFactory.ftext("paymentData_bankName", "Bank Name", "Bank Name", 40);
		bankAccountNo = FieldFactory.ftext("paymentData_bankAccountNo", "Account Num", "Account Num", 30);
		bankRoutingNo = FieldFactory.ftext("paymentData_bankRoutingNo", "Routing Num", "Routing Num", 20);

		fields.addField(bankName);
		fields.addField(bankAccountNo);
		fields.addField(bankRoutingNo);
	}

	@Override
	protected void draw() {
		final VerticalFieldPanelComposer cmpsr = new VerticalFieldPanelComposer();
		cmpsr.setCanvas(canvas);

		cmpsr.addField(bankName);
		cmpsr.addField(bankAccountNo);
		cmpsr.addField(bankRoutingNo);
	}
}
