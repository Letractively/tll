/**
 * The Logic Lab
 * @author jpk Nov 4, 2007
 */
package com.tll.client.admin.ui.field.account;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.tll.client.admin.ui.field.BankFieldsProvider;
import com.tll.client.admin.ui.field.BankFieldsRenderer;
import com.tll.client.admin.ui.field.CreditCardFieldsProvider;
import com.tll.client.admin.ui.field.CreditCardFieldsRenderer;
import com.tll.client.bind.IBindable;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.IFieldRenderer;

/**
 * PaymentInfoPanel
 * @author jpk
 */
public final class PaymentInfoPanel<M extends IBindable> extends FieldPanel<M> implements SourcesTabEvents {

	class CreditCardPanel extends FieldPanel<M> {

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

	class BankPanel extends FieldPanel<M> {

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

	private final TabPanel tabPanel = new TabPanel();

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
		setRenderer(new IFieldRenderer() {

			public void render(Panel panel, FieldGroup fg) {
				(new CreditCardFieldsRenderer()).render(creditCardPanel.getPanel(), creditCardPanel.getFieldGroup());
				(new BankFieldsRenderer()).render(bankPanel.getPanel(), bankPanel.getFieldGroup());
			}
		});
	}

	@Override
	public FieldGroup generateFieldGroup() {
		FieldGroup fg = new FieldGroup();
		fg.addField(creditCardPanel.getFieldGroup());
		fg.addField(bankPanel.getFieldGroup());
		return fg;
	}

	public void addTabListener(TabListener listener) {
		tabPanel.addTabListener(listener);
	}

	public void removeTabListener(TabListener listener) {
		tabPanel.removeTabListener(listener);
	}
}