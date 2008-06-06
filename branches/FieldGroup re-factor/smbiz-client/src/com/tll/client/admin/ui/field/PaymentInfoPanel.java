/**
 * The Logic Lab
 * @author jpk Nov 4, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.ui.field.FieldGroupPanel;

/**
 * PaymentInfoPanel
 * @author jpk
 */
public final class PaymentInfoPanel extends FieldGroupPanel implements TabListener {

	private final CreditCardPanel creditCardPanel;
	private final BankPanel bankPanel;
	private final TabPanel tabPanel = new TabPanel();

	/**
	 * Constructor
	 */
	public PaymentInfoPanel() {
		super("Payment Info");
		creditCardPanel = new CreditCardPanel();
		bankPanel = new BankPanel();
	}

	@Override
	public void neededAuxData(AuxDataRequest auxDataRequest) {
		creditCardPanel.neededAuxData(auxDataRequest);
		bankPanel.neededAuxData(auxDataRequest);
	}

	@Override
	protected Widget doInit() {
		creditCardPanel.init();
		bankPanel.init();

		fields.addField(creditCardPanel.getFields());
		fields.addField(bankPanel.getFields());

		tabPanel.add(creditCardPanel, "Credit Card");
		tabPanel.add(bankPanel, "Bank");

		return tabPanel;
	}

	public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
		if(sender == tabPanel) return true;
		return false;
	}

	public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
		if(sender == tabPanel) {
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		tabPanel.selectTab(0); // reset state
	}

}
