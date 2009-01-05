/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.admin.ui.field.account;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosureEvent;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.admin.ui.field.AddressFieldsProvider;
import com.tll.client.admin.ui.field.AddressFieldsRenderer;
import com.tll.client.bind.AbstractModelEditAction;
import com.tll.client.bind.Binding;
import com.tll.client.bind.IBindable;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.convert.IConverter;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPathException;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IField;
import com.tll.client.ui.field.IFieldGroupProvider;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IndexedFieldPanel;
import com.tll.client.util.GlobalFormat;
import com.tll.model.impl.AccountStatus;
import com.tll.model.impl.AddressType;

/**
 * AccountPanel
 * @author jpk
 */
public class AccountPanel<M extends IBindable> extends FieldPanel<M> {

	/**
	 * AccountEditAction
	 * @author jpk
	 */
	class AccountEditAction extends AbstractModelEditAction<M, AccountPanel<M>> {

		@Override
		protected void populateBinding(AccountPanel<M> ap) {
			final List<Binding> children = binding.getChildren();
			children.add(new Binding(ap, Model.NAME_PROPERTY));
			children.add(new Binding(ap, Model.DATE_CREATED_PROPERTY));
			children.add(new Binding(ap, Model.DATE_MODIFIED_PROPERTY));
			children.add(new Binding(ap, "parent.name"));
			children.add(new Binding(ap, "status"));
			children.add(new Binding(ap, "dateCancelled"));
			children.add(new Binding(ap, "currency.id"));
			children.add(new Binding(ap, "billingModel"));
			children.add(new Binding(ap, "billingCycle"));
			children.add(new Binding(ap, "dateLastCharged"));
			children.add(new Binding(ap, "nextChargeDate"));
			children.add(new Binding(ap, "persistPymntInfo"));
			children.add(new Binding(ap, "paymentInfo"));
			children.add(new Binding(ap, "addresses"));
		}
	}

	class AccountFieldsRenderer implements IFieldRenderer {

		public void render(Panel panel, String parentPropPath, FieldGroup fg) {
			final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
			cmpsr.setCanvas(panel);

			// first row
			cmpsr.addField(fg.getField(Model.NAME_PROPERTY));
			cmpsr.addField(fg.getField("status"));
			cmpsr.addField(fg.getField("dateCancelled"));
			cmpsr.addField(fg.getField("currency.id"));
			cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			cmpsr.addField(fg.getField("parent.name"));
			cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			cmpsr.addField(fg.getField(Model.DATE_CREATED_PROPERTY));
			cmpsr.stopFlow();
			cmpsr.addField(fg.getField(Model.DATE_MODIFIED_PROPERTY));

			// second row (billing)
			cmpsr.newRow();
			cmpsr.addField(fg.getField("billingModel"));
			cmpsr.addField(fg.getField("billingCycle"));
			cmpsr.addField(fg.getField("dateLastCharged"));
			cmpsr.addField(fg.getField("nextChargeDate"));

			// third row
			cmpsr.newRow();
			// account addresses block
			dpAddresses.add(addressesPanel);
			cmpsr.addWidget(dpAddresses);

			// payment info block
			FlowPanel fp = new FlowPanel();
			fp.add((Widget) fg.getField("persistPymntInfo"));
			fp.add(paymentInfoPanel);
			dpPaymentInfo.add(fp);
			cmpsr.addWidget(dpPaymentInfo);

			dpPaymentInfo.addEventHandler(new DisclosureHandler() {

				public void onOpen(DisclosureEvent event) {
					// TODO default select first tab if none currently selected
				}

				public void onClose(DisclosureEvent event) {
				}
			});

			dpAddresses.addEventHandler(new DisclosureHandler() {

				public void onOpen(DisclosureEvent event) {
					addressesPanel.tabAddresses.selectTab(0);
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
	static final class AccountAddressPanel<M extends IBindable> extends FieldPanel<M> {

		final FlowPanel panel = new FlowPanel();

		final AddressType addressType;

		/**
		 * Constructor
		 * @param addressType
		 */
		public AccountAddressPanel(AddressType addressType) {
			super();
			this.addressType = addressType;
			initWidget(panel);
			setRenderer(new IFieldRenderer() {

				public void render(Panel panel, String parentPropPath, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(panel);

					// account address name row
					cmpsr.addField(fg.getField(Model.NAME_PROPERTY));

					// address row
					cmpsr.newRow();
					FlowPanel fp = new FlowPanel();
					AddressFieldsRenderer r = new AddressFieldsRenderer();
					r.render(fp, "address", fg);
					cmpsr.addWidget(fp);
				}
			});
		}

		@Override
		protected FieldGroup generateFieldGroup() {
			return (new IFieldGroupProvider() {

				public FieldGroup getFieldGroup() {
					FieldGroup fg = new FieldGroup();
					fg.addField(FieldFactory.entityNameField());
					// TODO fix since we don't have data fields anymore
					// fields.addField(fdata("type", addressType));
					fg.addField("address", (new AddressFieldsProvider()).getFieldGroup());
					return fg;
				}
			}).getFieldGroup();
		}
	} // AccountAddressPanel

	/**
	 * AddressesPanel
	 * @author jpk
	 * @param <M>
	 */
	static final class AddressesPanel<M extends IBindable> extends IndexedFieldPanel<M> implements IConverter<FieldGroup, M>, TabListener {

		private final IConverter<FieldGroup, M> accountAddressRenderer = new IConverter<FieldGroup, M>() {

			/*
			name = FieldFactory.entityNameField();
			// TODO fix since we don't have data fields anymore
			// fields.addField(fdata("type", addressType));
			fields.addField(name);
			fields.addField("address", addressPanel.getFieldGroup());
			*/

			public FieldGroup convert(M m) throws IllegalArgumentException {
				// assume the model to be an account address
				FieldGroup fg = new FieldGroup();

				try {
					m.getProperty(Model.NAME_PROPERTY);
					m.getProperty("address.firstName");
				}
				catch(PropertyPathException e) {
					throw new IllegalArgumentException(e);
				}

				return fg;
			}
		};

		private final TabPanel tabAddresses = new TabPanel();

		/**
		 * Constructor
		 */
		public AddressesPanel() {
			super(null);

			// TODO fix
			setConverter(this);

			// listen to tab events
			tabAddresses.addTabListener(this);
			initWidget(tabAddresses);
		}

		public FieldGroup convert(M o) throws IllegalArgumentException {
			FieldGroup fg = new FieldGroup();
			populateFieldGroup(fg);
			return fg;
		}

		public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
			if(sender == tabAddresses) {
				// need to hide any field messages bound to fields on the tab that is
				// going out of view
				int csti = tabAddresses.getTabBar().getSelectedTab();
				if(csti != -1) {
					Widget w = tabAddresses.getWidget(csti);
					if(w instanceof AccountAddressPanel) MsgManager.instance().show(w, false, true);
				}
			}
			return true;
		}

		public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
			// no-op
		}

		@Override
		protected FieldGroup getPrototypeIndexedFieldGroup() {
			return null;
		}
	}

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
	}

	@Override
	public Object getProperty(String propPath) throws PropertyPathException {
		if("paymentInfo".equals(propPath)) {
			return paymentInfoPanel;
		}
		else if("addresses".equals(propPath)) {
			return addressesPanel;
		}
		return super.getProperty(propPath);
	}

	@Override
	public void setProperty(String propPath, Object value) throws PropertyPathException, Exception {
		if("paymentInfo".equals(propPath)) {
			// return paymentInfoPanel;
		}
		else if("addresses".equals(propPath)) {
			// return addressesPanel;
		}
		super.setProperty(propPath, value);
	}

	@Override
	protected FieldGroup generateFieldGroup() {
		return (new IFieldGroupProvider() {

			public FieldGroup getFieldGroup() {
				FieldGroup fg = new FieldGroup();

				fg.addField(FieldFactory.entityNameField());

				fg.addFields(FieldFactory.entityTimestampFields());

				fg.addField(FieldFactory.ftext("parent.name", "Parent", "Parent Account", 15));
				fg.getField("parent.name").setReadOnly(true);

				fg.addField(FieldFactory.fselect("status", "Status", "Status", false, Arrays.asList(AccountStatus.values())));
				fg.getField("status").addChangeListener(new ChangeListener() {

					public void onChange(Widget sender) {
						final FieldGroup fields = getFieldGroup();
						String s = getFieldGroup().getField("status").getText().toLowerCase();
						final boolean closed = "closed".equals(s);
						IField<?> f = fields.getField("dateCancelled");
						f.setVisible(closed);
						f.setRequired(closed);
					}
				});

				fg.addField(FieldFactory.fdate("dateCancelled", "Date Cancelled", "Date Cancelled", GlobalFormat.DATE));

				fg.addField(FieldFactory.fselect("currency.id", "Currency", "Currency", false, AuxDataCache.instance()
						.getCurrencyDataMap().values()));

				fg.addField(FieldFactory.ftext("billingModel", "Billing Model", "Billing Model", 18));

				fg.addField(FieldFactory.ftext("billingCycle", "Billing Cycle", "Billing Cycle", 18));

				fg.addField(FieldFactory.fdate("dateLastCharged", "Last Charged", "Last Charged", GlobalFormat.DATE));

				fg.addField(FieldFactory.fdate("nextChargeDate", "Next Charge", "Next Charge", GlobalFormat.DATE));

				fg.addField(FieldFactory.fcheckbox("persistPymntInfo", "PersistPayment Info?", "PersistPayment Info?"));
				fg.getField("persistPymntInfo").addChangeListener(new ChangeListener() {

					public void onChange(Widget sender) {
						paymentInfoPanel.getFieldGroup().setEnabled(((CheckBox) sender).isChecked());
					}
				});

				fg.addField("paymentInfo", paymentInfoPanel.getFieldGroup());
				paymentInfoPanel.getFieldGroup().setFeedbackWidget(dpPaymentInfo);

				fg.addField("addresses", addressesPanel.getFieldGroup());
				addressesPanel.getFieldGroup().setFeedbackWidget(dpAddresses);

				return fg;
			}
		}).getFieldGroup();
	}

	/*
	private void rebuildAddresses(final IFieldGroupModelBinding binding) {
		final FieldGroup fields = getFieldGroup();
		final Model accountModel = binding.resolveModel(EntityType.ACCOUNT);
		assert accountModel != null && fields != null;

		// un-bind existing addresses
		for(Widget w : tabAddresses) {
			if(w instanceof AccountAddressPanel) {
				fields.removeField(((AccountAddressPanel) w).getFieldGroup());
			}
		}
		tabAddresses.clear();

		// bind addresses contained in the bound model
		RelatedManyProperty pvAddresses;
		try {
			pvAddresses = accountModel.relatedMany("addresses");
		}
		catch(PropertyPathException e) {
			pvAddresses = null;
		}
		for(AddressType at : AddressType.values()) {
			AccountAddressPanel aap = null;
			if(pvAddresses != null) {
				Iterator<IndexedProperty> itr = pvAddresses.iterator();
				while(itr.hasNext()) {
					IndexedProperty ip = itr.next();
					try {
						if(at == ip.getModel().getPropertyValue("type").getValue()) {
							aap = new AccountAddressPanel(at);
							fields.addField(ip.getPropertyName(), aap.getFieldGroup());
							aap.draw();
							tabAddresses.add(aap, new DeleteTabWidget(at.getName(), aap.getFieldGroup(), binding, ip
									.getPropertyName()));
							break;
						}
					}
					catch(PropertyPathException e) {
						throw new IllegalStateException(e);
					}
				}
			}
			if(aap == null) {
				NoEntityExistsPanel neep =
						new NoEntityExistsPanel(at, "No " + at.getName() + " address set.", "Create " + at.getName() + " address");
				neep.addClickListener(new ClickListener() {

					public void onClick(Widget sender) {
						// non-existant account address

						// resolve tab index
						int selTabIndx = tabAddresses.getTabBar().getSelectedTab();
						assert selTabIndx >= 0;

						// stub aa panel
						AddressType at = (AddressType) ((NoEntityExistsPanel) sender).getRefToken();
						AccountAddressPanel aap = new AccountAddressPanel(at);

						// tentatively bind the indexed model
						String indexedModelPath =
								binding.bindIndexedModel(aap.getFieldGroup(), "addresses", EntityType.ACCOUNT_ADDRESS);

						// insert the tab widget holding the new address fields
						tabAddresses.insert(aap, new DeleteTabWidget(at.getName(), aap.getFieldGroup(), binding, indexedModelPath),
								selTabIndx == 0 ? 0 : selTabIndx);
						tabAddresses.remove(selTabIndx + 1);
						tabAddresses.selectTab(selTabIndx);
					}
				});
				tabAddresses.add(neep, at.getName());
			}
		}

		// show/hide date cancelled according to the account's status
		String status = accountModel.asString("status");
		status = status == null ? null : status.toLowerCase();
		dateCancelled.setVisible("closed".equals(status));

		dpPaymentInfo.setOpen(false);
		dpAddresses.setOpen(false);
		tabAddresses.selectTab(0);
	}
	*/
}
