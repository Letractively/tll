/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.admin.ui.field;

import java.util.List;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DisclosureEvent;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.EntityPayload;
import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.event.ICrudListener;
import com.tll.client.event.type.CrudEvent;
import com.tll.client.field.IField;
import com.tll.client.model.Model;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.FieldPanel;
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
public class AccountPanel extends NamedTimeStampEntityPanel implements ClickListener, TabListener, DisclosureHandler, ICrudListener, ChangeListener {

	/**
	 * Non-generated prototype acount address entity used to stub new account
	 * addresses.
	 */
	private Model accountAddressPrototype;

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
	protected AccountAddressPanel[] aaPanels;

	private AccountAddressPanel bindPending;

	/**
	 * Array of indexes representing "open" (non-existant) account addresses for
	 * the backing account entity. <br>
	 * <code>-1</code> indicates non-open, zero and positive indicates an open
	 * slot and corres. to the account address list index in the backing account
	 * entity.
	 */
	protected int[] availAAPVIs;

	/**
	 * Constructor
	 * @param propName
	 */
	public AccountPanel(String propName) {
		super(propName, "Account");
		paymentInfoPanel = new PaymentInfoPanel("paymentInfo");
	}

	protected final int getNextAvailableAccountAddressPropertyIndex() {
		for(int i = 0; i < availAAPVIs.length; i++) {
			if(availAAPVIs[i] != -1) {
				return i;
			}
		}
		return -1; // no open slots left
	}

	protected final AccountAddressPanel getAccountAddressPanel(AddressType addressType) {
		for(AccountAddressPanel element2 : aaPanels) {
			if(element2.getAddressType() == addressType) {
				return element2;
			}
		}
		return null; // shouldn't happen
	}

	private void applyPrototypeAccountAddress(AccountAddressPanel aap) {
		if(accountAddressPrototype == null) {
			// drat! we gotta go to the server
			bindPending = aap;
			CrudCommand cc = new CrudCommand(this);
			cc.receiveEmpty(EntityType.ACCOUNT_ADDRESS, false);
			cc.addCrudListener(this);
			cc.execute();
		}
		else {
			aap.bind(accountAddressPrototype.copy());
		}
	}

	public void onCrudEvent(CrudEvent event) {
		EntityPayload p = event.getPayload();
		if(!p.hasErrors()) {
			accountAddressPrototype = p.getEntity();
			assert bindPending != null;
			applyPrototypeAccountAddress(bindPending);
		}
	}

	/**
	 * Propagates the property path based on the dynamic <code>propValIndex</code>
	 * argument and adds the {@link AccountAddressPanel}s field group to this
	 * panel's field group and finally updates the {@link #availAAPVIs} array.
	 * @param aap The {@link AccountAddressPanel} to bind.
	 * @param propValIndex The index relative to the account entities' collection
	 *        of current account addresses.
	 */
	private void bindAccountAddressPanel(AccountAddressPanel aap, int propValIndex) {
		final String propName = "addresses[" + propValIndex + "]";
		aap.setPropertyName(propName);
		aap.setPropValIndex(propValIndex);
		if(fields.getField(propName) == null) {
			fields.addField(aap.getFields());
		}
		availAAPVIs[propValIndex] = -1; // indicates occupied
	}

	/**
	 * Removes the {@link AccountAddressPanel}s field group from this panel's
	 * field group and opens up the taken slot in the {@link #availAAPVIs} array.
	 * @param aap The {@link AccountAddressPanel} to unbind.
	 * @param propValIndex The index relative to the account entities' collection
	 *        of current account addresses.
	 */
	private void unbindAccountAddressPanel(AccountAddressPanel aap) {
		assert aap != null;
		int pvi = aap.getPropValIndex();
		if(pvi != -1) {
			String propName = aap.getPropertyName();
			fields.removeField(propName);
			availAAPVIs[pvi] = pvi;
			aap.setPropValIndex(-1);
			aap.setPropertyName(null);
		}
	}

	@Override
	protected void neededAuxData(AuxDataRequest auxDataRequest) {
		auxDataRequest.requestEntityList(EntityType.CURRENCY);
		paymentInfoPanel.neededAuxData(auxDataRequest);
		// NOTE: we can't use AccountAddressPanel as instances of this type are
		// dynamically loaded
		auxDataRequest.requestAppRefData("usps-state-abbrs");
		auxDataRequest.requestAppRefData("iso-country-codes");
	}

	@Override
	protected void configure() {
		super.configure();

		parent = ftext("parent.name", "Parent", IField.LBL_ABOVE, 15);
		parent.setReadOnly(true);
		parent.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		status = fselect("status", "Status", IField.LBL_ABOVE, ClientEnumUtil.toMap(AccountStatus.class));
		status.getListBox().addChangeListener(this);

		dateCancelled = fdate("dateCancelled", "Date Cancelled", IField.LBL_ABOVE, GlobalFormat.DATE);

		currency = fselect("currency.id", "Currency", IField.LBL_ABOVE, AuxDataCache.instance().getCurrencyDataMap());

		billingModel = ftext("billingModel", "Billing Model", IField.LBL_ABOVE, 18);
		billingCycle = ftext("billingCycle", "Billing Cycle", IField.LBL_ABOVE, 18);
		dateLastCharged = fdate("dateLastCharged", "Last Charged", IField.LBL_ABOVE, GlobalFormat.DATE);
		nextChargeDate = fdate("nextChargeDate", "Next Charge", IField.LBL_ABOVE, GlobalFormat.DATE);

		persistPymntInfo = fbool("persistPymntInfo", "PersistPayment Info?");
		persistPymntInfo.getCheckBox().addClickListener(this);

		paymentInfoPanel.configure();
		paymentInfoPanel.setRefWidget(dpPaymentInfo);

		// account address sub-panels prep
		availAAPVIs = new int[AddressType.values().length];

		// stub the account address panels
		aaPanels = new AccountAddressPanel[AddressType.values().length];
		int adrTypeIndex = 0;
		for(AddressType at : AddressType.values()) {
			AccountAddressPanel aap = new AccountAddressPanel(null, at, -1);
			aap.setRefWidget(tabAddresses);
			// NOTE: the aa panel property path is set just before the entity is
			// applied
			aaPanels[adrTypeIndex++] = aap;
			// aap.configure();
		}
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

		FieldPanel frow, fcol, fldp;

		// first row
		frow = new FieldPanel(IField.CSS_FIELD_ROW);
		add(frow);
		frow.add(name);
		frow.add(status);
		frow.add(dateCancelled);
		frow.add(currency);
		frow.add(parent);
		fcol = new FieldPanel(IField.CSS_FIELD_COL);
		fcol.add(dateCreated);
		fcol.add(dateModified);
		frow.add(fcol);

		// second row (billing)
		frow = new FieldPanel(IField.CSS_FIELD_ROW);
		frow.add(billingModel);
		frow.add(billingCycle);
		frow.add(dateLastCharged);
		frow.add(nextChargeDate);
		add(frow);

		// third row
		// NOTE: we use a horizontal panel to ensure both the address and payment
		// info disclosure panels remain on the same row at all times.
		frow = new FieldPanel(IField.CSS_FIELD_ROW);
		HorizontalPanel hp = new HorizontalPanel();
		frow.add(hp);
		add(frow);

		// account addresses block
		dpAddresses.add(tabAddresses);
		fldp = new FieldPanel(IField.CSS_FIELD);
		fldp.add(dpAddresses);
		hp.add(fldp);

		// payment info block
		fcol = new FieldPanel(IField.CSS_FIELD_COL);
		fcol.add(persistPymntInfo);
		fcol.add(paymentInfoPanel);
		dpPaymentInfo.add(fcol);
		fldp = new FieldPanel(IField.CSS_FIELD);
		fldp.add(dpPaymentInfo);
		hp.add(fldp);

		dpPaymentInfo.addEventHandler(this);
		dpAddresses.addEventHandler(this);
	}

	@Override
	protected void onBeforeBind(Model model) {
		super.onBeforeBind(model);

		// reset
		for(int i = 0; i < availAAPVIs.length; i++) {
			availAAPVIs[i] = i;
		}

		tabAddresses.clear();

		// [re-]set the account address panels
		RelatedManyProperty pvAddresses = model.relatedMany("addresses");
		List<Model> addresses = pvAddresses.getList();
		for(AddressType at : AddressType.values()) {
			// resolve the entity property path index for this adrs type
			int i = -1;
			boolean exists = false;
			if(addresses != null) {
				for(Model aa : addresses) {
					i++;
					// set the account address prototype
					if(accountAddressPrototype == null) {
						accountAddressPrototype = aa.copy();
						accountAddressPrototype.clearPropertyValues();
					}
					if(at.getValue().equals(aa.asString("type"))) {
						exists = true;
						break;
					}
				}
				if(!exists) i = -1;
			}

			AccountAddressPanel aap = getAccountAddressPanel(at);
			if(i != -1) { // exists
				assert aap != null;
				bindAccountAddressPanel(aap, i);
				tabAddresses.add(aap, at.getName());
			}
			else { // doesn't exist
				unbindAccountAddressPanel(aap);
				NoEntityExistsPanel neep =
						new NoEntityExistsPanel(at.getName(), "No " + at.getName() + " address set.", "Create " + at.getName()
								+ " address");
				neep.addClickListener(this);
				tabAddresses.add(neep, at.getName());
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
	}

	@Override
	protected void onBeforeUpdateModel(Model model) {
		super.onBeforeUpdateModel(model);

		// we need to stub related many account addresses if they exist in the field
		// group but don't in the account entity (propValGroup)
		for(AccountAddressPanel aap : aaPanels) {
			String propName = aap.getPropertyName();
			// NOTE: if the prop name is null then we can assume the entity doesn't
			// have it or has it so we are safe
			if(propName != null && !model.isPropertyDefined(propName)) {
				// account address is new so stub it in the account entity
				assert accountAddressPrototype != null;
				Model newAccountAddress = accountAddressPrototype.copy();
				AddressType at = aap.getAddressType();
				newAccountAddress.setProp("type", at.getValue());
				newAccountAddress.setRelatedOne("account", model);
				model.addIndexedModel(propName, newAccountAddress);
			}
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		dpPaymentInfo.setOpen(false);
		dpAddresses.setOpen(false);
		// tabAddresses.selectTab(0);
	}

	public void onClick(Widget sender) {
		if(sender == persistPymntInfo.getCheckBox()) {
			paymentInfoPanel.setEnabled(persistPymntInfo.getCheckBox().isChecked());
		}
		else if(sender instanceof NoEntityExistsPanel) {
			// this is a non-existant account address
			int selTabIndx = tabAddresses.getTabBar().getSelectedTab();
			assert selTabIndx >= 0;
			String adrsTypeName = ((NoEntityExistsPanel) sender).getRefToken();
			int propValIndex = getNextAvailableAccountAddressPropertyIndex();
			assert propValIndex != -1;
			AccountAddressPanel aap = aaPanels[selTabIndx];
			bindAccountAddressPanel(aap, propValIndex);

			// ensure the account address panel fields have their property values set
			applyPrototypeAccountAddress(aap);
			// aap.reset();

			tabAddresses.insert(aap, adrsTypeName, selTabIndx == 0 ? 0 : selTabIndx);
			aap.getFields().render();
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
