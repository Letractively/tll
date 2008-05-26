/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.admin.ui.field;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DisclosureEvent;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.field.FieldGroup;
import com.tll.client.model.IndexedProperty;
import com.tll.client.model.Model;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.FlowFieldCanvas;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.NamedTimeStampEntityPanel;
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
public class AccountPanel extends NamedTimeStampEntityPanel implements ClickListener, TabListener, DisclosureHandler, ChangeListener {

	protected TextField parent;
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
	 * AccountAddressPanelModelBinding
	 * @author jpk
	 */
	private final class AccountAddressPanelModelBinding {

		public AccountAddressPanel panel;
		public Model proto;
	}

	private final Map<AddressType, AccountAddressPanelModelBinding> aamap =
			new HashMap<AddressType, AccountAddressPanelModelBinding>(AddressType.values().length);

	/**
	 * Constructor
	 * @param propName
	 */
	public AccountPanel(String propName) {
		super(propName, "Account");
		paymentInfoPanel = new PaymentInfoPanel("paymentInfo");
	}

	@Override
	protected void neededAuxData(AuxDataRequest auxDataRequest) {
		auxDataRequest.requestEntityList(EntityType.CURRENCY);
		paymentInfoPanel.neededAuxData(auxDataRequest);
		// NOTE: we can't use AccountAddressPanel as instances of this type are
		// dynamically loaded
		auxDataRequest.requestAppRefData("usps-state-abbrs");
		auxDataRequest.requestAppRefData("iso-country-codes");
		auxDataRequest.requestEntityPrototype(EntityType.ACCOUNT_ADDRESS);
	}

	@Override
	protected void configure() {
		super.configure();

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

		paymentInfoPanel.configure();
		paymentInfoPanel.setRefWidget(dpPaymentInfo);

		// stub the address type to account address panel bindings
		for(AddressType at : AddressType.values()) {
			aamap.put(at, new AccountAddressPanelModelBinding());
		}

		// listen to tab events
		tabAddresses.addTabListener(this);

		fields.addField(parent);
		fields.addField(status);
		fields.addField(dateCancelled);
		fields.addField(billingModel);
		fields.addField(billingCycle);
		fields.addField(dateLastCharged);
		fields.addField(nextChargeDate);
		fields.addField(currency);
		fields.addField(persistPymntInfo);
		fields.addField(paymentInfoPanel.getFields());

		FlowFieldCanvas canvas = new FlowFieldCanvas(panel);

		// first row
		canvas.addField(name);
		canvas.addField(status);
		canvas.addField(dateCancelled);
		canvas.addField(currency);
		canvas.addField(parent);
		canvas.addField(dateCreated);
		canvas.addFieldAtCurrent(dateModified);

		// second row (billing)
		canvas.newRow();
		canvas.addField(billingModel);
		canvas.addField(billingCycle);
		canvas.addField(dateLastCharged);
		canvas.addField(nextChargeDate);

		// third row
		canvas.newRow();
		// account addresses block
		dpAddresses.add(tabAddresses);
		canvas.addWidget(dpAddresses);

		// payment info block
		canvas.addField(persistPymntInfo);
		dpPaymentInfo.add(paymentInfoPanel);
		canvas.addWidgetAtCurrent(dpPaymentInfo);

		dpPaymentInfo.addEventHandler(this);
		dpAddresses.addEventHandler(this);
	}

	@Override
	protected void onBeforeBind(Model model) {
		super.onBeforeBind(model);

		// un-bind existing
		for(AddressType at : aamap.keySet()) {
			AccountAddressPanelModelBinding binding = aamap.get(at);
			if(binding.panel != null) {
				fields.removeField(binding.panel.getFields());
				binding.panel = null;
				binding.proto = null;
			}
		}

		// clear out address tabs
		tabAddresses.clear();

		// bind from model
		RelatedManyProperty pvAddresses = model.relatedMany("addresses");
		if(pvAddresses != null) {
			Iterator<IndexedProperty> itr = pvAddresses.iterator();
			while(itr.hasNext()) {
				IndexedProperty ip = itr.next();
				AddressType at = (AddressType) ip.getModel().getProp("type").getValue();
				AccountAddressPanel aap = new AccountAddressPanel(ip.getPropertyName(), at);
				aamap.get(at).panel = aap;
				fields.addField(aap.getFields());
				aap.configure();
			}
		}

		// add address tabs
		for(AddressType at : aamap.keySet()) {
			AccountAddressPanelModelBinding binding = aamap.get(at);
			if(binding.panel == null) {
				NoEntityExistsPanel neep =
						new NoEntityExistsPanel(at, "No " + at.getName() + " address set.", "Create " + at.getName() + " address");
				neep.addClickListener(this);
				tabAddresses.add(neep, at.getName());
			}
			else {
				tabAddresses.add(binding.panel, at.getName());
			}
		}
	}

	@Override
	protected void onAfterBind(Model model) {
		super.onAfterBind(model);
		// show/hide date cancelled according to the account's status
		String status = model.asString("status");
		status = status == null ? null : status.toLowerCase();
		dateCancelled.setVisible("closed".equals(status));

		dpPaymentInfo.setOpen(false);
		dpAddresses.setOpen(false);
		tabAddresses.selectTab(0);
	}

	@Override
	protected void onBeforeUpdateModel(Model model) {
		super.onBeforeUpdateModel(model);

		// we need to stub related many account addresses if they exist in the field
		// group but don't in the account entity (propValGroup)

		RelatedManyProperty addresses = model.relatedMany("addresses");
		assert addresses != null;

		for(AddressType at : aamap.keySet()) {
			AccountAddressPanel aap = aamap.get(at).panel;
			if(aap != null) {
				if(aap.getFields().isPending()) {
					Model aaproto = aamap.get(at).proto;
					assert aaproto != null;
					String actualPropName = addresses.add(aaproto);
					aaproto.setProp("type", at);
					aaproto.setRelatedOne("account", model);
					aap.getFields().setPropertyName(actualPropName);
				}
			}
		}
	}

	public void onClick(Widget sender) {
		if(sender == persistPymntInfo.getCheckBox()) {
			paymentInfoPanel.setEnabled(persistPymntInfo.getCheckBox().isChecked());
		}
		else if(sender instanceof NoEntityExistsPanel) {
			// this is a non-existant account address
			int selTabIndx = tabAddresses.getTabBar().getSelectedTab();
			assert selTabIndx >= 0;

			AddressType at = (AddressType) ((NoEntityExistsPanel) sender).getRefToken();
			AccountAddressPanelModelBinding binding = aamap.get(at);
			assert binding.panel == null;
			String pendingIndexedPropName = FieldGroup.getPendingPropertyName();
			binding.panel = new AccountAddressPanel(pendingIndexedPropName, at);
			binding.proto = AuxDataCache.instance().getEntityPrototype(EntityType.ACCOUNT_ADDRESS);
			assert binding.proto != null;
			binding.panel.bind(binding.proto);
			fields.addField(binding.panel.getFields());

			tabAddresses.insert(binding.panel, at.getName(), selTabIndx == 0 ? 0 : selTabIndx);
			// binding.panel.getFields().render();
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
