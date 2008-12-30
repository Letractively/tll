/**
 * The Logic Lab
 * @author jpk Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;

/**
 * PaymentInfoPanel
 * @author jpk
 */
public final class PaymentInfoPanel<M> extends FieldPanel<M> implements SourcesTabEvents {

	private final TabPanel tabPanel = new TabPanel();

	private final CreditCardPanel<M> creditCardPanel;
	private final BankPanel<M> bankPanel;

	/**
	 * Constructor
	 */
	public PaymentInfoPanel() {
		super("Payment Info");
		creditCardPanel = new CreditCardPanel<M>();
		bankPanel = new BankPanel<M>();
		initWidget(tabPanel);
	}

	@Override
	public void populateFieldGroup(FieldGroup fields) {
		fields.addField(creditCardPanel.getFieldGroup());
		fields.addField(bankPanel.getFieldGroup());
	}

	@Override
	protected void draw() {
		tabPanel.add(creditCardPanel, "Credit Card");
		tabPanel.add(bankPanel, "Bank");
	}

	public void addTabListener(TabListener listener) {
		tabPanel.addTabListener(listener);
	}

	public void removeTabListener(TabListener listener) {
		tabPanel.removeTabListener(listener);
	}
}
