/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.admin.ui.field.account;

import java.util.Iterator;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
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
import com.tll.client.event.type.FieldBindingEvent;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.IField;
import com.tll.client.field.IFieldGroupModelBinding;
import com.tll.client.model.IndexedProperty;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPathException;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DeleteTabWidget;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowFieldPanelComposer;
import com.tll.client.ui.field.NoEntityExistsPanel;
import com.tll.client.ui.field.SelectField;
import com.tll.client.ui.field.TextField;
import com.tll.client.util.ClientEnumUtil;
import com.tll.client.util.GlobalFormat;
import com.tll.model.EntityType;
import com.tll.model.impl.AccountStatus;
import com.tll.model.impl.AddressType;

/**
 * AccountPanel
 * @author jpk
 */
public class AccountPanel extends FieldPanel implements TabListener, DisclosureHandler {

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
			name = entityNameField();
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
		IField f;

		fields.addField(entityNameField());
		fields.addFields(entityTimestampFields());

		f = ftext("parent.name", "Parent", 15);
		f.setReadOnly(true);
		fields.addField(f);

		f = fselect("status", "Status", ClientEnumUtil.toMap(AccountStatus.class));
		((SelectField) f).getListBox().addChangeListener(new ChangeListener() {

			public void onChange(Widget sender) {
				final FieldGroup fields = getFieldGroup();
				String s = getFieldGroup().getField("status").getValue().toLowerCase();
				final boolean closed = "closed".equals(s);
				IField f = fields.getField("dateCancelled");
				f.setVisible(closed);
				f.setRequired(closed);
			}
		});

		fields.addField(fdate("dateCancelled", "Date Cancelled", GlobalFormat.DATE));

		fields.addField(fselect("currency.id", "Currency", AuxDataCache.instance().getCurrencyDataMap()));

		fields.addField(ftext("billingModel", "Billing Model", 18));
		fields.addField(ftext("billingCycle", "Billing Cycle", 18));
		fields.addField(fdate("dateLastCharged", "Last Charged", GlobalFormat.DATE));
		fields.addField(fdate("nextChargeDate", "Next Charge", GlobalFormat.DATE));

		f = fbool("persistPymntInfo", "PersistPayment Info?");
		((CheckboxField) f).getCheckBox().addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				paymentInfoPanel.getFieldGroup().setEnabled(((CheckBox) sender).isChecked());
			}
		});

		paymentInfoPanel.getFieldGroup().setFeedbackWidget(dpPaymentInfo);

		// listen to tab events
		tabAddresses.addTabListener(this);

		fields.addField("paymentInfo", paymentInfoPanel.getFieldGroup());
	}

	@Override
	protected void drawInternal(Panel canvas) {
		final FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
		cmpsr.setCanvas(canvas);

		final FieldGroup fields = getFieldGroup();

		// first row
		cmpsr.addField(fields.getField(Model.NAME_PROPERTY));
		cmpsr.addField(fields.getField("status"));
		cmpsr.addField(fields.getField("dateCancelled"));
		cmpsr.addField(fields.getField("currency.id"));
		cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		cmpsr.addField(fields.getField("parent.name"));
		cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		cmpsr.addField(fields.getField("dateCreated"));
		cmpsr.stopFlow();
		cmpsr.addField(fields.getField("dateModified"));

		// second row (billing)
		cmpsr.newRow();
		cmpsr.addField(fields.getField("billingModel"));
		cmpsr.addField(fields.getField("billingCycle"));
		cmpsr.addField(fields.getField("dateLastCharged"));
		cmpsr.addField(fields.getField("nextChargeDate"));

		// third row
		cmpsr.newRow();
		// account addresses block
		dpAddresses.add(tabAddresses);
		cmpsr.addWidget(dpAddresses);

		// payment info block
		FlowPanel fp = new FlowPanel();
		fp.add((Widget) fields.getField("persistPymntInfo"));
		fp.add(paymentInfoPanel);
		dpPaymentInfo.add(fp);
		cmpsr.addWidget(dpPaymentInfo);

		dpPaymentInfo.addEventHandler(this);
		dpAddresses.addEventHandler(this);
	}

	@Override
	public void onFieldBindingEvent(FieldBindingEvent event) {
		switch(event.getType()) {
			case BEFORE_BIND:
				rebuildAddresses(event.getBinding());
				break;
			case AFTER_BIND:
				break;
		}
	}

	/**
	 * Handles the late binding of an account's related many addressses.
	 * @param binding The operating field/model binding
	 */
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
		fields.getField("dateCancelled").setVisible("closed".equals(status));

		dpPaymentInfo.setOpen(false);
		dpAddresses.setOpen(false);
		tabAddresses.selectTab(0);
	}

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
