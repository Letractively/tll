/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.admin.ui.field.account;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.DisclosureEvent;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.admin.ui.field.AddressFieldsRenderer;
import com.tll.client.bind.AbstractModelEditAction;
import com.tll.client.bind.IBindable;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPathException;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IField;
import com.tll.client.ui.field.IFieldGroupProvider;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IndexedFieldPanel;
import com.tll.model.impl.AddressType;

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
			cmpsr.addField(fg.getFieldByName("status"));
			cmpsr.addField(fg.getFieldByName("dateCancelled"));
			cmpsr.addField(fg.getFieldByName("currencyId"));
			cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			cmpsr.addField(fg.getFieldByName("parentName"));
			cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			cmpsr.addField(fg.getFieldByName(Model.DATE_CREATED_PROPERTY));
			cmpsr.stopFlow();
			cmpsr.addField(fg.getFieldByName(Model.DATE_MODIFIED_PROPERTY));

			// second row (billing)
			cmpsr.newRow();
			cmpsr.addField(fg.getFieldByName("billingModel"));
			cmpsr.addField(fg.getFieldByName("billingCycle"));
			cmpsr.addField(fg.getFieldByName("dateLastCharged"));
			cmpsr.addField(fg.getFieldByName("nextChargeDate"));

			// third row
			cmpsr.newRow();
			// account addresses block
			dpAddresses.add(addressesPanel);
			cmpsr.addWidget(dpAddresses);

			// payment info block
			FlowPanel fp = new FlowPanel();
			fp.add((Widget) fg.getFieldByName("persistPymntInfo"));
			fp.add(paymentInfoPanel);
			dpPaymentInfo.add(fp);
			cmpsr.addWidget(dpPaymentInfo);

			dpPaymentInfo.addEventHandler(new DisclosureHandler() {

				public void onOpen(DisclosureEvent event) {
					if(paymentInfoPanel.tabPanel.getTabBar().getSelectedTab() == -1) {
						paymentInfoPanel.tabPanel.selectTab(0);
					}
				}

				public void onClose(DisclosureEvent event) {
				}
			});

			dpAddresses.addEventHandler(new DisclosureHandler() {

				public void onOpen(DisclosureEvent event) {
					if(addressesPanel.tabAddresses.getWidgetCount() > 0) {
						if(addressesPanel.tabAddresses.getTabBar().getSelectedTab() == -1) {
							addressesPanel.tabAddresses.selectTab(0);
						}
					}
				}

				public void onClose(DisclosureEvent event) {
				}
			});
		}
	}

	/**
	 * AccountAddressPanel
	 * @author jpk
	 */
	private static final class AccountAddressPanel<M extends IBindable> extends FieldPanel<FlowPanel, M> {

		final FlowPanel panel = new FlowPanel();

		AddressType addressType;

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

					// account address name row
					cmpsr.addField(fg.getFieldByName(Model.NAME_PROPERTY));

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
	 * @param <M>
	 */
	static final class AddressesPanel<M extends IBindable> extends IndexedFieldPanel<AccountAddressPanel<M>, M> implements
			TabListener {

		private final TabPanel tabAddresses = new TabPanel();

		/**
		 * Constructor
		 */
		public AddressesPanel() {
			super("Addresses");

			// listen to tab events
			tabAddresses.addTabListener(this);
			initWidget(tabAddresses);
		}

		@Override
		protected void draw() {
			tabAddresses.clear();

			// add the index field panels to the tab panel
			for(AccountAddressPanel<M> ap : indexPanels) {
				tabAddresses.add(ap, ap.addressType.getName());
			}
		}

		@Override
		protected AccountAddressPanel<M> createIndexPanel(M indexModel) {
			AccountAddressPanel<M> aap = new AccountAddressPanel<M>();
			try {
				aap.addressType = (AddressType) indexModel.getProperty("type");
			}
			catch(PropertyPathException e) {
				throw new IllegalStateException("Unable to obtain the account address type", e);
			}
			return aap;
		}

		public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
			assert sender == tabAddresses;
			// need to hide any field messages bound to fields on the tab that is
			// going out of view
			int csti = tabAddresses.getTabBar().getSelectedTab();
			if(csti != -1) {
				Widget w = tabAddresses.getWidget(csti);
				if(w instanceof AccountAddressPanel) MsgManager.instance().show(w, false, true);
			}
			return true;
		}

		public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
			// no-op
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
				final FieldGroup fields = getFieldGroup();
				String s = getFieldGroup().getField("status").getText().toLowerCase();
				final boolean closed = "closed".equals(s);
				IField<?, ?> f = fields.getField("dateCancelled");
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
