/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.ui.field.account;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.ui.field.AddressFieldsRenderer;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldGroupProvider;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.common.bind.IModel;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.model.AddressType;
import com.tll.model.SmbizEntityType;

/**
 * AccountPanel
 * @author jpk
 */
public class AccountPanel extends FieldPanel<FlowPanel> {

	/**
	 * AccountAddressPanel
	 * @author jpk
	 */
	static final class AccountAddressPanel extends FieldPanel<FlowPanel> {

		@Override
		protected FieldGroup generateFieldGroup() {
			return (new AccountAddressFieldProvider()).getFieldGroup();
		}

		@Override
		public IFieldRenderer<FlowPanel> getRenderer() {
			return new IFieldRenderer<FlowPanel>() {

				public void render(FlowPanel panel, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(panel);

					// account address type/name row
					cmpsr.addField(fg.getFieldWidgetByName("type"));
					cmpsr.addField(fg.getFieldWidget(Model.NAME_PROPERTY));

					// address row
					cmpsr.newRow();
					final FlowPanel fp = new FlowPanel();
					final AddressFieldsRenderer r = new AddressFieldsRenderer();
					r.render(fp, (FieldGroup) fg.getFieldByName("address"));
					cmpsr.addWidget(fp);
				}
			};
		}
	} // AccountAddressPanel

	/**
	 * AddressesPanel
	 * @author jpk
	 */
	final class AddressesPanel extends TabbedIndexedFieldPanel<AccountAddressPanel> {

		/**
		 * Constructor
		 */
		public AddressesPanel() {
			super("Addresses", "addresses", true, true);
		}

		@Override
		public FieldPanel<?> getParentFieldPanel() {
			return AccountPanel.this;
		}

		@Override
		protected String getIndexTypeName() {
			return "Account Address";
		}

		@Override
		protected String getInstanceName(AccountAddressPanel index) {
			AddressType type;
			String aaName;
			try {
				type = (AddressType) index.getModel().getProperty("type");
				aaName = (String) index.getModel().getProperty("name");
			}
			catch(final PropertyPathException e) {
				throw new IllegalStateException(e);
			}

			return aaName + " (" + type.getName() + ")";
		}

		@Override
		protected IModel createPrototypeModel() {
			return AuxDataCache.instance().getEntityPrototype(SmbizEntityType.ACCOUNT_ADDRESS);
		}

		@Override
		protected AccountAddressPanel createIndexPanel() {
			return new AccountAddressPanel();
		}

	} // AddressesPanel

	private final FlowPanel panel = new FlowPanel();

	protected final DisclosurePanel dpPaymentInfo = new DisclosurePanel("Payment Info", false);
	protected final PaymentInfoPanel paymentInfoPanel = new PaymentInfoPanel();

	protected final DisclosurePanel dpAddresses = new DisclosurePanel("Addresses", false);
	protected final AddressesPanel addressesPanel = new AddressesPanel();

	/**
	 * Constructor
	 */
	public AccountPanel() {
		super();
		initWidget(panel);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected FieldGroup generateFieldGroup() {
		final FieldGroup fg = (new IFieldGroupProvider() {

			public FieldGroup getFieldGroup() {
				final FieldGroup fg = (new AccountFieldsProvider()).getFieldGroup();
				fg.addField("paymentInfo", paymentInfoPanel.getFieldGroup());
				fg.addField("addresses", addressesPanel.getFieldGroup());
				return fg;
			}
		}).getFieldGroup();

		paymentInfoPanel.getFieldGroup().setWidget(dpPaymentInfo);
		addressesPanel.getFieldGroup().setWidget(dpAddresses);

		fg.getFieldWidget("parent.name").setReadOnly(true);

		((IFieldWidget<String>) fg.getFieldWidget("status")).addValueChangeHandler(new ValueChangeHandler<String>() {

			public void onValueChange(ValueChangeEvent<String> event) {
				final String s = event.getValue().toLowerCase();
				final boolean closed = "closed".equals(s);
				final IFieldWidget<?> f = getFieldGroup().getFieldWidget("dateCancelled");
				f.setVisible(closed);
				f.setRequired(closed);
			}
		});

		((IFieldWidget<Boolean>) fg.getFieldWidget("persistPymntInfo")).addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			public void onValueChange(ValueChangeEvent<Boolean> event) {
				paymentInfoPanel.getFieldGroup().setEnabled(event.getValue());
			}
		});

		return fg;
	}

	@Override
	public IFieldRenderer<FlowPanel> getRenderer() {
		return new IFieldRenderer<FlowPanel>() {

			@Override
			public void render(FlowPanel widget, FieldGroup fg) {
				final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
				cmpsr.setCanvas(widget);

				// first row
				cmpsr.addField(fg.getFieldWidgetByName(Model.NAME_PROPERTY));
				cmpsr.addField(fg.getFieldWidgetByName("acntStatus"));
				cmpsr.addField(fg.getFieldWidgetByName("acntDateCancelled"));
				cmpsr.addField(fg.getFieldWidgetByName("acntCurrencyId"));
				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				cmpsr.addField(fg.getFieldWidgetByName("acntParentName"));
				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				cmpsr.addField(fg.getFieldWidgetByName(Model.DATE_CREATED_PROPERTY));
				cmpsr.stopFlow();
				cmpsr.addField(fg.getFieldWidgetByName(Model.DATE_MODIFIED_PROPERTY));

				// second row (billing)
				cmpsr.newRow();
				cmpsr.addField(fg.getFieldWidgetByName("acntBillingModel"));
				cmpsr.addField(fg.getFieldWidgetByName("acntBillingCycle"));
				cmpsr.addField(fg.getFieldWidgetByName("acntDateLastCharged"));
				cmpsr.addField(fg.getFieldWidgetByName("acntNextChargeDate"));

				// third row
				cmpsr.newRow();
				// account addresses block
				dpAddresses.add(addressesPanel);
				cmpsr.addWidget(dpAddresses);

				// payment info block
				final FlowPanel fp = new FlowPanel();
				fp.add((Widget) fg.getFieldWidgetByName("acntPersistPymntInfo"));
				fp.add(paymentInfoPanel);
				dpPaymentInfo.add(fp);
				cmpsr.addWidget(dpPaymentInfo);
			}
		};
	}
}
