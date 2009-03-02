/**
 * The Logic Lab
 * @author jpk Nov 4, 2007
 */
package com.tll.client.ui.field.account;

import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.tll.client.ui.field.BankFieldsProvider;
import com.tll.client.ui.field.BankFieldsRenderer;
import com.tll.client.ui.field.CreditCardFieldsProvider;
import com.tll.client.ui.field.CreditCardFieldsRenderer;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.IFieldRenderer;

/**
 * PaymentInfoPanel
 * @author jpk
 */
public final class PaymentInfoPanel extends FieldPanel<TabPanel> implements
		HasSelectionHandlers<Integer>, HasBeforeSelectionHandlers<Integer> {

	private static class CreditCardPanel extends FieldPanel<FlowPanel> {

		/**
		 * Constructor
		 */
		public CreditCardPanel() {
			super();
			initWidget(new FlowPanel());
		}

		@Override
		public FieldGroup generateFieldGroup() {
			return (new CreditCardFieldsProvider()).getFieldGroup();
		}

		@Override
		public IFieldRenderer<FlowPanel> getRenderer() {
			return new CreditCardFieldsRenderer();
		}

	}

	private static class BankPanel extends FieldPanel<FlowPanel> {

		/**
		 * Constructor
		 */
		public BankPanel() {
			super();
			initWidget(new FlowPanel());
		}

		@Override
		public FieldGroup generateFieldGroup() {
			return (new BankFieldsProvider()).getFieldGroup();
		}

		@Override
		public IFieldRenderer<FlowPanel> getRenderer() {
			return new BankFieldsRenderer();
		}
	}

	final TabPanel tabPanel = new TabPanel();

	private final CreditCardPanel creditCardPanel;
	private final BankPanel bankPanel;

	/**
	 * Constructor
	 */
	public PaymentInfoPanel() {
		super();
		creditCardPanel = new CreditCardPanel();
		bankPanel = new BankPanel();
		initWidget(tabPanel);
	}

	@Override
	public IFieldRenderer<TabPanel> getRenderer() {
		return new IFieldRenderer<TabPanel>() {

			@SuppressWarnings("synthetic-access")
			public void render(TabPanel panel, FieldGroup fg) {
				panel.add(creditCardPanel, "Credit Card");
				panel.add(bankPanel, "Bank");
			}
		};
	}

	@Override
	public FieldGroup generateFieldGroup() {
		final FieldGroup fg = new FieldGroup("Payment Info");
		fg.addField(creditCardPanel.getFieldGroup());
		fg.addField(bankPanel.getFieldGroup());
		return fg;
	}

	public HandlerRegistration addSelectionHandler(SelectionHandler<Integer> handler) {
		// TODO finish
		return null;
	}

	public HandlerRegistration addBeforeSelectionHandler(BeforeSelectionHandler<Integer> handler) {
		// TODO finish
		return null;
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		if(tabPanel.getTabBar().getSelectedTab() == -1) {
			tabPanel.selectTab(0);
		}
	}

}
