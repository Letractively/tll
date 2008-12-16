/**
 * The Logic Lab
 * @author jpk
 * Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.field.FieldBindingGroup;
import com.tll.client.field.FieldGroup;
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
	protected void populateFieldBindingGroup(FieldBindingGroup bindings, String parentPropertyPath, FieldGroup fields,
			Model model) {
		bindings.add(createFieldBinding("paymentData_bankName", model, parentPropertyPath));
		bindings.add(createFieldBinding("paymentData_bankAccountNo", model, parentPropertyPath));
		bindings.add(createFieldBinding("paymentData_bankRoutingNo", model, parentPropertyPath));
	}

	@Override
	protected void draw(Panel canvas, FieldGroup fields) {
		final VerticalFieldPanelComposer cmpsr = new VerticalFieldPanelComposer();
		cmpsr.setCanvas(canvas);

		cmpsr.addField(bankName);
		cmpsr.addField(bankAccountNo);
		cmpsr.addField(bankRoutingNo);
	}
}
