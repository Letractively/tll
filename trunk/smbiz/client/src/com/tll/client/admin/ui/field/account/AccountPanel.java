/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.admin.ui.field.account;

import java.util.Iterator;

import com.google.gwt.user.client.ui.ChangeListener;
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
import com.tll.client.field.DataField;
import com.tll.client.model.IndexedProperty;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPath;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.FlowFieldPanelComposer;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.DeleteTabWidget;
import com.tll.client.ui.field.FieldGroupPanel;
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
public class AccountPanel extends FieldGroupPanel implements ClickListener, TabListener, DisclosureHandler, ChangeListener {

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
	protected final DisclosurePanel dpAddresses = new DisclosurePanel("Addresses", false);
	protected PaymentInfoPanel paymentInfoPanel;
	protected final TabPanel tabAddresses = new TabPanel();

	/**
	 * AccountAddressPanel
	 * @author jpk
	 */
	static final class AccountAddressPanel extends FieldGroupPanel {

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
		public void populateFieldGroup() {
			name = createNameEntityField();
			addField(new DataField("type", addressType));
			addField(name);
			addField("address", addressPanel.getFields());
		}

		@Override
		protected void draw(Panel canvas) {
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
	public void populateFieldGroup() {
		name = createNameEntityField();
		timestamps = createTimestampEntityFields();

		parent = ftext("parent.name", "Parent", 15);
		parent.setReadOnly(true);
		// parent.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		status = fselect("status", "Status", ClientEnumUtil.toMap(AccountStatus.class));
		status.getListBox().addChangeListener(this);

		dateCancelled = fdate("dateCancelled", "Date Cancelled", GlobalFormat.DATE);

		currency = fselect("currency.id", "Currency", AuxDataCache.instance().getCurrencyDataMap());

		billingModel = ftext("billingModel", "Billing Model", 18);
		billingCycle = ftext("billingCycle", "Billing Cycle", 18);
		dateLastCharged = fdate("dateLastCharged", "Last Charged", GlobalFormat.DATE);
		nextChargeDate = fdate("nextChargeDate", "Next Charge", GlobalFormat.DATE);

		persistPymntInfo = fbool("persistPymntInfo", "PersistPayment Info?");
		persistPymntInfo.getCheckBox().addClickListener(this);

		paymentInfoPanel.setFeedbackWidget(dpPaymentInfo);

		// listen to tab events
		tabAddresses.addTabListener(this);

		addField(name);
		addFields(timestamps);
		addField(parent);
		addField(status);
		addField(dateCancelled);
		addField(billingModel);
		addField(billingCycle);
		addField(dateLastCharged);
		addField(nextChargeDate);
		addField(currency);
		addField(persistPymntInfo);
		addField("paymentInfo", paymentInfoPanel.getFields());
	}

	@Override
	protected void draw(Panel canvas) {
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
		FlowPanel fp = new FlowPanel();
		fp.add(persistPymntInfo);
		fp.add(paymentInfoPanel);
		dpPaymentInfo.add(fp);
		cmpsr.addWidget(dpPaymentInfo);

		dpPaymentInfo.addEventHandler(this);
		dpAddresses.addEventHandler(this);
	}

	@Override
	protected void applyModel(Model model) {
		// un-bind existing
		for(Widget w : tabAddresses) {
			if(w instanceof AccountAddressPanel) {
				removeField(((AccountAddressPanel) w).getFields());
			}
		}
		tabAddresses.clear();

		final PropertyPath path = new PropertyPath();

		// bind
		path.parse("addresses");
		RelatedManyProperty pvAddresses = model.relatedMany(path);
		path.parse("type");
		for(AddressType at : AddressType.values()) {
			AccountAddressPanel aap = null;
			if(pvAddresses != null) {
				Iterator<IndexedProperty> itr = pvAddresses.iterator();
				while(itr.hasNext()) {
					IndexedProperty ip = itr.next();
					if(at == ip.getModel().getValue(path).getValue()) {
						aap = new AccountAddressPanel(at);
						addField(ip.getPropertyName(), aap.getFields());
						tabAddresses.add(aap, new DeleteTabWidget(at.getName(), aap.getFields(), ip.getPropertyName()));
						break;
					}
				}
			}
			if(aap == null) {
				NoEntityExistsPanel neep =
						new NoEntityExistsPanel(at, "No " + at.getName() + " address set.", "Create " + at.getName() + " address");
				neep.addClickListener(this);
				tabAddresses.add(neep, at.getName());
			}
		}

		// show/hide date cancelled according to the account's status
		String status = model.asString("status");
		status = status == null ? null : status.toLowerCase();
		dateCancelled.setVisible("closed".equals(status));

		dpPaymentInfo.setOpen(false);
		dpAddresses.setOpen(false);
		tabAddresses.selectTab(0);
	}

	public void onClick(Widget sender) {
		if(sender == persistPymntInfo.getCheckBox()) {
			paymentInfoPanel.getFields().setEnabled(persistPymntInfo.getCheckBox().isChecked());
		}
		else if(sender instanceof NoEntityExistsPanel) {
			// non-existant account address

			// resolve tab index
			int selTabIndx = tabAddresses.getTabBar().getSelectedTab();
			assert selTabIndx >= 0;

			// stub aa panel
			AddressType at = (AddressType) ((NoEntityExistsPanel) sender).getRefToken();
			AccountAddressPanel aap = new AccountAddressPanel(at);
			String parentPropPath = PropertyPath.indexedUnbound("addresses");
			addField(parentPropPath, aap.getFields());

			// bind to prototype
			Model aaproto = AuxDataCache.instance().getEntityPrototype(EntityType.ACCOUNT_ADDRESS);
			if(aaproto == null) throw new IllegalStateException();
			// NOTE: we need a property path offset here since the fields' property
			// paths are relative to ACCOUNT!
			aap.getFields().bindModel(1, aaproto.getBindingRef());

			tabAddresses.insert(aap, new DeleteTabWidget(at.getName(), aap.getFields(), null), selTabIndx == 0 ? 0
					: selTabIndx);
			tabAddresses.remove(selTabIndx + 1);
			tabAddresses.selectTab(selTabIndx);
		}
	}

	public void onClose(DisclosureEvent event) {
		// need to hide any field messages that are "cloaked" by the disclosure
		// panel
		if(event.getSource() == dpAddresses) {
			MsgManager.instance.show(dpAddresses, false, true);
		}
		else if(event.getSource() == dpPaymentInfo) {
			MsgManager.instance.show(dpPaymentInfo, false, true);
		}
	}

	public void onOpen(DisclosureEvent event) {
		if(event.getSource() == this.dpAddresses) {
			tabAddresses.selectTab(0);
		}
	}

	public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
		if(sender == tabAddresses) {
			// need to hide any field messages bound to fields on the tab that is
			// going out of view
			int csti = tabAddresses.getTabBar().getSelectedTab();
			if(csti != -1) {
				Widget w = tabAddresses.getWidget(csti);
				if(w instanceof AccountAddressPanel) MsgManager.instance.show(w, false, true);
			}
		}
		return true;
	}

	public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
		// no-op
	}

	public void onChange(Widget sender) {
		if(sender == status.getListBox()) {
			String s = status.getValue().toLowerCase();
			final boolean closed = "closed".equals(s);
			dateCancelled.setVisible(closed);
			dateCancelled.setRequired(closed);
		}
	}

}
