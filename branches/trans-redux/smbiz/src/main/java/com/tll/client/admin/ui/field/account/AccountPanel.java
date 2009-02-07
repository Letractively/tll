/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.admin.ui.field.account;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.admin.ui.field.AddressFieldsRenderer;
import com.tll.client.bind.AbstractModelEditAction;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IField;
import com.tll.client.ui.field.IFieldGroupProvider;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.common.bind.IBindable;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.model.AddressType;
import com.tll.model.EntityType;

/**
 * AccountPanel
 * @param <M> the model type
 * @author jpk
 */
public class AccountPanel<M extends IBindable> extends FieldPanel<FlowPanel, M> {

	/**
	 * AccountEditAction
	 * @author jpk
	 */
	private class AccountEditAction extends AbstractModelEditAction<M, AccountPanel<M>> {

		@Override
		protected void populateBinding(AccountPanel<M> fp) throws PropertyPathException {
			addFieldBinding(fp, Model.NAME_PROPERTY);
			addFieldBinding(fp, Model.DATE_CREATED_PROPERTY);
			addFieldBinding(fp, Model.DATE_MODIFIED_PROPERTY);
			addFieldBinding(fp, "parent.name");
			addFieldBinding(fp, "status");
			addFieldBinding(fp, "dateCancelled");
			addFieldBinding(fp, "currency.id");
			addFieldBinding(fp, "billingModel");
			addFieldBinding(fp, "billingCycle");
			addFieldBinding(fp, "dateLastCharged");
			addFieldBinding(fp, "nextChargeDate");
			addFieldBinding(fp, "persistPymntInfo");

			addNestedFieldBindings(fp, "paymentInfo");

			addIndexedFieldBinding(fp.getModel(), "addresses", addressesPanel);
		}

		public void execute() {
			// TODO anything?
		}
	}

	class AccountFieldsRenderer implements IFieldRenderer<FlowPanel> {

		public void render(FlowPanel panel, FieldGroup fg) {
			final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
			cmpsr.setCanvas(panel);

			// first row
			cmpsr.addField(fg.getFieldByName(Model.NAME_PROPERTY));
			cmpsr.addField(fg.getFieldByName("acntStatus"));
			cmpsr.addField(fg.getFieldByName("acntDateCancelled"));
			cmpsr.addField(fg.getFieldByName("acntCurrencyId"));
			cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			cmpsr.addField(fg.getFieldByName("acntParentName"));
			cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			cmpsr.addField(fg.getFieldByName(Model.DATE_CREATED_PROPERTY));
			cmpsr.stopFlow();
			cmpsr.addField(fg.getFieldByName(Model.DATE_MODIFIED_PROPERTY));

			// second row (billing)
			cmpsr.newRow();
			cmpsr.addField(fg.getFieldByName("acntBillingModel"));
			cmpsr.addField(fg.getFieldByName("acntBillingCycle"));
			cmpsr.addField(fg.getFieldByName("acntDateLastCharged"));
			cmpsr.addField(fg.getFieldByName("acntNextChargeDate"));

			// third row
			cmpsr.newRow();
			// account addresses block
			dpAddresses.add(addressesPanel);
			cmpsr.addWidget(dpAddresses);

			// payment info block
			FlowPanel fp = new FlowPanel();
			fp.add((Widget) fg.getFieldByName("acntPersistPymntInfo"));
			fp.add(paymentInfoPanel);
			dpPaymentInfo.add(fp);
			cmpsr.addWidget(dpPaymentInfo);
		}
	}

	/**
	 * AccountAddressPanel
	 * @author jpk
	 */
	private static final class AccountAddressPanel<M extends IBindable> extends FieldPanel<FlowPanel, M> {

		final FlowPanel panel = new FlowPanel();

		/**
		 * Constructor
		 */
		public AccountAddressPanel() {
			super();
			initWidget(panel);
			setRenderer(new IFieldRenderer<FlowPanel>() {

				public void render(FlowPanel panel, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(panel);

					// account address type/name row
					cmpsr.addField(fg.getFieldByName("type"));
					cmpsr.addField(fg.getField(Model.NAME_PROPERTY));

					// address row
					cmpsr.newRow();
					FlowPanel fp = new FlowPanel();
					AddressFieldsRenderer r = new AddressFieldsRenderer();
					r.render(fp, (FieldGroup) fg.getFieldByName("address"));
					cmpsr.addWidget(fp);
				}
			});
		}

		@Override
		protected FieldGroup generateFieldGroup() {
			return (new AccountAddressFieldProvider()).getFieldGroup();
		}
	} // AccountAddressPanel

	/**
	 * AddressesPanel
	 * @author jpk
	 * @param <M> The model type
	 */
	private static final class AddressesPanel<M extends IBindable> extends
			TabbedIndexedFieldPanel<AccountAddressPanel<M>, M> {

		/**
		 * Constructor
		 */
		public AddressesPanel() {
			super("Addresses", true, true);
		}

		@Override
		protected String getIndexTypeName() {
			return "Account Address";
		}

		@Override
		protected String getTabLabelText(AccountAddressPanel<M> aap) {
			AddressType type;
			String aaName;
			try {
				type = (AddressType) aap.getModel().getProperty("type");
				aaName = (String) aap.getModel().getProperty("name");
			}
			catch(PropertyPathException e) {
				throw new IllegalStateException(e);
			}

			return aaName + " (" + type.getName() + ")";
		}

		@SuppressWarnings("unchecked")
		@Override
		protected M createPrototypeModel() {
			return (M) AuxDataCache.instance().getEntityPrototype(EntityType.ACCOUNT_ADDRESS);
		}

		@Override
		protected AccountAddressPanel<M> createIndexPanel(M indexModel) {
			return new AccountAddressPanel<M>();
		}

	} // AddressesPanel

	private final FlowPanel panel = new FlowPanel();

	protected final DisclosurePanel dpPaymentInfo = new DisclosurePanel("Payment Info", false);
	protected final PaymentInfoPanel<M> paymentInfoPanel = new PaymentInfoPanel<M>();

	protected final DisclosurePanel dpAddresses = new DisclosurePanel("Addresses", false);
	protected final AddressesPanel<M> addressesPanel = new AddressesPanel<M>();

	/**
	 * Constructor
	 */
	@SuppressWarnings("synthetic-access")
	public AccountPanel() {
		super();
		initWidget(panel);
		setAction(new AccountEditAction());
		setRenderer(new AccountFieldsRenderer());
	}

	@Override
	protected FieldGroup generateFieldGroup() {
		FieldGroup fg = (new IFieldGroupProvider() {

			public FieldGroup getFieldGroup() {
				FieldGroup fg = (new AccountFieldsProvider()).getFieldGroup();
				fg.addField("paymentInfo", paymentInfoPanel.getFieldGroup());
				fg.addField("addresses", addressesPanel.getFieldGroup());
				return fg;
			}
		}).getFieldGroup();

		paymentInfoPanel.getFieldGroup().setFeedbackWidget(dpPaymentInfo);
		addressesPanel.getFieldGroup().setFeedbackWidget(dpAddresses);

		fg.getField("parent.name").setReadOnly(true);

		fg.getField("status").addChangeListener(new ChangeListener() {

			public void onChange(Widget sender) {
				String s = getFieldGroup().getField("status").getText().toLowerCase();
				final boolean closed = "closed".equals(s);
				IField<?, ?> f = getFieldGroup().getField("dateCancelled");
				f.setVisible(closed);
				f.setRequired(closed);
			}
		});

		fg.getField("persistPymntInfo").addChangeListener(new ChangeListener() {

			public void onChange(Widget sender) {
				paymentInfoPanel.getFieldGroup().setEnabled(((CheckboxField<?>) sender).isChecked());
			}
		});

		return fg;
	}
}
