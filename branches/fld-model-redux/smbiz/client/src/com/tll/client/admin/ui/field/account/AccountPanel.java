/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.admin.ui.field.account;

import java.util.Arrays;

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
import com.tll.client.admin.ui.field.AddressPanel;
import com.tll.client.admin.ui.field.PaymentInfoPanel;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowFieldPanelComposer;
import com.tll.client.ui.field.IField;
import com.tll.client.ui.field.SelectField;
import com.tll.client.ui.field.TextField;
import com.tll.client.util.GlobalFormat;
import com.tll.model.impl.AccountStatus;
import com.tll.model.impl.AddressType;

/**
 * AccountPanel
 * @author jpk
 */
public class AccountPanel extends FieldPanel implements TabListener, DisclosureHandler {

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
	protected PaymentInfoPanel paymentInfoPanel;

	protected final DisclosurePanel dpAddresses = new DisclosurePanel("Addresses", false);
	protected final TabPanel tabAddresses = new TabPanel();

	/**
	 * AccountAddressPanel
	 * @author jpk
	 */
	static final class AccountAddressPanel extends FieldPanel {

		final AddressType addressType;
		TextField name;
		final AddressPanel addressPanel;

		/**
		 * Constructor
		 * @param addressType
		 */
		public AccountAddressPanel(AddressType addressType) {
			super(addressType.getName());
			this.addressType = addressType;
			addressPanel = new AddressPanel();
		}

		@Override
		public void populateFieldGroup(FieldGroup fields) {
			name = FieldFactory.entityNameField();
			// TODO fix since we don't have data fields anymore
			// fields.addField(fdata("type", addressType));
			fields.addField(name);
			fields.addField("address", addressPanel.getFieldGroup());
		}

		@Override
		protected void drawInternal(Panel canvas) {
			final FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
			cmpsr.setCanvas(canvas);

			// account address name row
			cmpsr.addField(name);

			// address row
			addressPanel.draw();
			cmpsr.newRow();
			cmpsr.addWidget(addressPanel);
		}
	}

	/**
	 * Constructor
	 */
	public AccountPanel() {
		super("Account");
		paymentInfoPanel = new PaymentInfoPanel();
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
				IField f = fields.getField("dateCancelled");
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

		// listen to tab events
		tabAddresses.addTabListener(this);

	}

	@Override
	protected void drawInternal(Panel canvas) {
		final FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
		cmpsr.setCanvas(canvas);

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
		dpAddresses.add(tabAddresses);
		cmpsr.addWidget(dpAddresses);

		// payment info block
		paymentInfoPanel.draw();
		FlowPanel fp = new FlowPanel();
		fp.add(persistPymntInfo);
		fp.add(paymentInfoPanel);
		dpPaymentInfo.add(fp);
		cmpsr.addWidget(dpPaymentInfo);

		dpPaymentInfo.addEventHandler(this);
		dpAddresses.addEventHandler(this);
	}

	/**
	 * Handles the late binding of an account's related many addressses.
	 * @param binding The operating field/model binding
	 */
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

	public void onClose(DisclosureEvent event) {
		// need to hide any field messages that are "cloaked" by the disclosure
		// panel
		if(event.getSource() == dpAddresses) {
			MsgManager.instance().show(dpAddresses, false, true);
		}
		else if(event.getSource() == dpPaymentInfo) {
			MsgManager.instance().show(dpPaymentInfo, false, true);
		}
	}

	public void onOpen(DisclosureEvent event) {
		if(event.getSource() == this.dpAddresses) {
			tabAddresses.selectTab(0);
		}
		else if(event.getSource() == this.dpPaymentInfo) {
			// TODO default select first tab if none currently selected
		}
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
}
