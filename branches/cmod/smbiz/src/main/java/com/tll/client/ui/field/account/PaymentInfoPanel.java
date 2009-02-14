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
import com.tll.common.bind.IBindable;

/**
 * PaymentInfoPanel
 * @author jpk
 * @param <M>
 */
public final class PaymentInfoPanel<M extends IBindable> extends FieldPanel<TabPanel, M> implements
		HasSelectionHandlers<Integer>, HasBeforeSelectionHandlers<Integer> {

	private static class CreditCardPanel<M extends IBindable> extends FieldPanel<FlowPanel, M> {

		/**
		 * Constructor
		 */
		public CreditCardPanel() {
			super();
			initWidget(new FlowPanel());
			setRenderer(new CreditCardFieldsRenderer());
		}

		@Override
		public FieldGroup generateFieldGroup() {
			return (new CreditCardFieldsProvider()).getFieldGroup();
		}

	}

	private static class BankPanel<M extends IBindable> extends FieldPanel<FlowPanel, M> {

		/**
		 * Constructor
		 */
		public BankPanel() {
			super();
			initWidget(new FlowPanel());
			setRenderer(new BankFieldsRenderer());
		}

		@Override
		public FieldGroup generateFieldGroup() {
			return (new BankFieldsProvider()).getFieldGroup();
		}

	}

	final TabPanel tabPanel = new TabPanel();

	private final CreditCardPanel<M> creditCardPanel;
	private final BankPanel<M> bankPanel;

	/**
	 * Constructor
	 */
	public PaymentInfoPanel() {
		super();
		creditCardPanel = new CreditCardPanel<M>();
		bankPanel = new BankPanel<M>();
		initWidget(tabPanel);
		setRenderer(new IFieldRenderer<TabPanel>() {

			@SuppressWarnings("synthetic-access")
			public void render(TabPanel panel, FieldGroup fg) {
				panel.add(creditCardPanel, "Credit Card");
				panel.add(bankPanel, "Bank");
			}
		});
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
