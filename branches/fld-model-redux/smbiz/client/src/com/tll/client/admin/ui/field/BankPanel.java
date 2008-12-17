/**
 * The Logic Lab
 * @author jpk
 * Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.FieldModelBinding;
import com.tll.client.model.Model;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.TextField;
import com.tll.client.ui.field.VerticalFieldPanelComposer;

/**
 * BankPanel
 * @author jpk
 */
public final class BankPanel extends FieldPanel {

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
	public void populateFieldGroup(FieldGroup fields) {
		bankName = FieldFactory.ftext("paymentData_bankName", "Bank Name", 40);
		bankAccountNo = FieldFactory.ftext("paymentData_bankAccountNo", "Account Num", 30);
		bankRoutingNo = FieldFactory.ftext("paymentData_bankRoutingNo", "Routing Num", 20);

		fields.addField(bankName);
		fields.addField(bankAccountNo);
		fields.addField(bankRoutingNo);
	}

	@Override
	public void setFieldBindings(Model model, FieldModelBinding bindings) {
		bindings.addBinding(bankName, model, "paymentData_bankName");
		bindings.addBinding(bankAccountNo, model, "paymentData_bankAccountNo");
		bindings.addBinding(bankRoutingNo, model, "paymentData_bankRoutingNo");
	}

	@Override
	protected void drawInternal(Panel canvas) {
		final VerticalFieldPanelComposer cmpsr = new VerticalFieldPanelComposer();
		cmpsr.setCanvas(canvas);

		cmpsr.addField(bankName);
		cmpsr.addField(bankAccountNo);
		cmpsr.addField(bankRoutingNo);
	}
}
