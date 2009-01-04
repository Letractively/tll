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
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.admin.ui.field.AddressPanel;
import com.tll.client.admin.ui.field.PaymentInfoPanel;
import com.tll.client.bind.AbstractModelEditAction;
import com.tll.client.bind.Binding;
import com.tll.client.bind.IBindable;
import com.tll.client.bind.convert.IConverter;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPathException;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowFieldPanelComposer;
import com.tll.client.ui.field.IField;
import com.tll.client.ui.field.IndexedFieldPanel;
import com.tll.client.ui.field.SelectField;
import com.tll.client.ui.field.TextField;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.IValidationFeedback;
import com.tll.client.validate.ValidationFeedbackManager;
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
			final IValidationFeedback vb = ValidationFeedbackManager.instance();
			children.add(new Binding(ap, name, vb, Model.NAME_PROPERTY));
			children.add(new Binding(ap, timestamps[0], vb, Model.DATE_CREATED_PROPERTY));
			children.add(new Binding(ap, timestamps[1], vb, Model.DATE_MODIFIED_PROPERTY));
			children.add(new Binding(ap, parent, vb, "parent.name"));
			children.add(new Binding(ap, status, vb, "status"));
			children.add(new Binding(ap, dateCancelled, vb, "dateCancelled"));
			children.add(new Binding(ap, currency, vb, "currency.id"));
			children.add(new Binding(ap, billingModel, vb, "billingModel"));
			children.add(new Binding(ap, billingCycle, vb, "billingCycle"));
			children.add(new Binding(ap, dateLastCharged, vb, "dateLastCharged"));
			children.add(new Binding(ap, nextChargeDate, vb, "nextChargeDate"));
			children.add(new Binding(ap, persistPymntInfo, vb, "persistPymntInfo"));
			children.add(new Binding(ap, "paymentInfo"));
			children.add(new Binding(ap, "addresses"));
		}
	}

	/**
	 * AccountAddressPanel
	 * @author jpk
	 */
	static final class AccountAddressPanel<M extends IBindable> extends FieldPanel<M> {

		final FlowPanel panel = new FlowPanel();
		final AddressType addressType;
		TextField name;
		AddressPanel<M> addressPanel;

		/**
		 * Constructor
		 * @param addressType
		 */
		public AccountAddressPanel(AddressType addressType) {
			super();
			this.addressType = addressType;
			initWidget(panel);
		}

		@Override
		protected void populateFieldGroup(FieldGroup fields) {
			name = FieldFactory.entityNameField();
			// TODO fix since we don't have data fields anymore
			// fields.addField(fdata("type", addressType));
			fields.addField(name);
			fields.addField("address", addressPanel.getFieldGroup());
		}

		@Override
		protected void draw() {
			final FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
			cmpsr.setCanvas(panel);

			// account address name row
			cmpsr.addField(name);

			// address row
			cmpsr.newRow();
			addressPanel = new AddressPanel<M>();
			cmpsr.addWidget(addressPanel);
		}
	}

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

		@Override
		protected void draw() {
		}

		@Override
		protected void populateFieldGroup(FieldGroup fields) {
			// nothing
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

	protected TextField parent;
	protected TextField name;
	protected DateField[] timestamps;
	protected SelectField status;
	protected DateField dateCancelled;
	protected SelectField currency;
	protected TextField billingModel;
	protected TextField billingCycle;
	protected DateField dateLastCharged;
	protected DateField nextChargeDate;
	protected CheckboxField persistPymntInfo;

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
	public void populateFieldGroup(FieldGroup fields) {
		name = FieldFactory.entityNameField();
		fields.addField(name);

		timestamps = FieldFactory.entityTimestampFields();
		fields.addFields(timestamps);

		parent = FieldFactory.ftext("parent.name", "Parent", "Parent Account", 15);
		parent.setReadOnly(true);
		fields.addField(parent);

		status = FieldFactory.fselect("status", "Status", "Status", false, Arrays.asList(AccountStatus.values()));
		status.addChangeListener(new ChangeListener() {

			public void onChange(Widget sender) {
				final FieldGroup fields = getFieldGroup();
				String s = getFieldGroup().getField("status").getText().toLowerCase();
				final boolean closed = "closed".equals(s);
				IField<?> f = fields.getField("dateCancelled");
				f.setVisible(closed);
				f.setRequired(closed);
			}
		});
		fields.addField(status);

		dateCancelled = FieldFactory.fdate("dateCancelled", "Date Cancelled", "Date Cancelled", GlobalFormat.DATE);
		fields.addField(dateCancelled);

		currency =
				FieldFactory.fselect("currency.id", "Currency", "Currency", false, AuxDataCache.instance().getCurrencyDataMap()
						.values());
		fields.addField(currency);

		billingModel = FieldFactory.ftext("billingModel", "Billing Model", "Billing Model", 18);
		fields.addField(billingModel);

		billingCycle = FieldFactory.ftext("billingCycle", "Billing Cycle", "Billing Cycle", 18);
		fields.addField(billingCycle);

		dateLastCharged = FieldFactory.fdate("dateLastCharged", "Last Charged", "Last Charged", GlobalFormat.DATE);
		fields.addField(dateLastCharged);

		nextChargeDate = FieldFactory.fdate("nextChargeDate", "Next Charge", "Next Charge", GlobalFormat.DATE);
		fields.addField(nextChargeDate);

		persistPymntInfo = FieldFactory.fcheckbox("persistPymntInfo", "PersistPayment Info?", "PersistPayment Info?");
		persistPymntInfo.addChangeListener(new ChangeListener() {

			public void onChange(Widget sender) {
				paymentInfoPanel.getFieldGroup().setEnabled(((CheckBox) sender).isChecked());
			}
		});
		fields.addField(persistPymntInfo);

		fields.addField("paymentInfo", paymentInfoPanel.getFieldGroup());
		paymentInfoPanel.getFieldGroup().setFeedbackWidget(dpPaymentInfo);

		fields.addField("addresses", addressesPanel.getFieldGroup());
		addressesPanel.getFieldGroup().setFeedbackWidget(dpAddresses);
	}

	@Override
	protected void draw() {
		final FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
		cmpsr.setCanvas(panel);

		// first row
		cmpsr.addField(name);
		cmpsr.addField(status);
		cmpsr.addField(dateCancelled);
		cmpsr.addField(currency);
		cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		cmpsr.addField(parent);
		cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		cmpsr.addField(timestamps[0]);
		cmpsr.stopFlow();
		cmpsr.addField(timestamps[1]);

		// second row (billing)
		cmpsr.newRow();
		cmpsr.addField(billingModel);
		cmpsr.addField(billingCycle);
		cmpsr.addField(dateLastCharged);
		cmpsr.addField(nextChargeDate);

		// third row
		cmpsr.newRow();
		// account addresses block
		dpAddresses.add(addressesPanel);
		cmpsr.addWidget(dpAddresses);

		// payment info block
		FlowPanel fp = new FlowPanel();
		fp.add(persistPymntInfo);
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
