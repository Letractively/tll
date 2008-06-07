/**
 * The Logic Lab
 * @author jpk Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.field.FieldGroupPanel;

/**
 * PaymentInfoPanel
 * @author jpk
 */
public final class PaymentInfoPanel extends FieldGroupPanel implements SourcesTabEvents {

	private final TabPanel tabPanel = new TabPanel();

	private final CreditCardPanel creditCardPanel;
	private final BankPanel bankPanel;

	/**
	 * Constructor
	 */
	public PaymentInfoPanel() {
		super("Payment Info");
		creditCardPanel = new CreditCardPanel();
		bankPanel = new BankPanel();
	}

	@Override
	public void populateFieldGroup() {
		addField(creditCardPanel.getFields());
		addField(bankPanel.getFields());
	}

	@Override
	protected Widget draw() {
		tabPanel.add(creditCardPanel, "Credit Card");
		tabPanel.add(bankPanel, "Bank");
		return tabPanel;
	}

	public void addTabListener(TabListener listener) {
		tabPanel.addTabListener(listener);
	}

	public void removeTabListener(TabListener listener) {
		tabPanel.removeTabListener(listener);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		tabPanel.selectTab(0); // reset state
	}
}
