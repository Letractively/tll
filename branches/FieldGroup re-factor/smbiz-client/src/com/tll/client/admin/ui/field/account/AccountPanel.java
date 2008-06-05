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
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.admin.ui.field.AddressPanel;
import com.tll.client.admin.ui.field.PaymentInfoPanel;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.model.IndexedProperty;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPath;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.CSS;
import com.tll.client.ui.FlowFieldPanelComposer;
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

	protected int lastAccountAddressIndex = -1;

	/**
	 * AccountAddressPanel
	 * @author jpk
	 */
	static final class AccountAddressPanel extends NamedTimeStampEntityPanel implements ClickListener {

		final AddressType addressType;
		final int index;
		final PushButton btnDeleteToggle;
		final AddressPanel addressPanel;

		/**
		 * Constructor
		 * @param addressType
		 * @param index
		 */
		public AccountAddressPanel(AddressType addressType, int index) {
			super(addressType.getName());

			this.addressType = addressType;
			this.index = index;

			// delete img btn
			btnDeleteToggle = new PushButton();
			btnDeleteToggle.addClickListener(this);
			btnDeleteToggle.addStyleName(CSS.FLOAT_RIGHT);

			addressPanel = new AddressPanel();
		}

		@Override
		protected void doInit() {
			super.doInit();

			addressPanel.init();
			fields.addField("address", addressPanel.getFields());

			// TODO determine why we need this as we shouldn't!!!
			// setMarkDeleted(false);

			FlowFieldPanelComposer canvas = new FlowFieldPanelComposer(panel);

			// account address name row
			canvas.addField(name);
			canvas.addWidget(btnDeleteToggle);

			// address row
			canvas.newRow();
			canvas.addWidget(addressPanel);
		}

		private void setMarkDeleted(boolean markDeleted) {
			getFields().setMarkedDeleted(markDeleted);
			if(markDeleted) {
				btnDeleteToggle.getUpFace().setImage(App.imgs().undo().createImage());
				btnDeleteToggle.setTitle("Un-delete " + addressType.getName() + " Address");
			}
			else {
				btnDeleteToggle.getUpFace().setImage(App.imgs().delete().createImage());
				btnDeleteToggle.setTitle("Delete " + addressType.getName() + " Address");
			}
		}

		public void onClick(Widget sender) {
			if(sender == btnDeleteToggle) {
				setMarkDeleted(!getFields().isMarkedDeleted());
			}
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
	public void neededAuxData(AuxDataRequest auxDataRequest) {
		auxDataRequest.requestEntityList(EntityType.CURRENCY);
		paymentInfoPanel.neededAuxData(auxDataRequest);
		// NOTE: we can't use AccountAddressPanel as instances of this type are
		// dynamically loaded
		auxDataRequest.requestAppRefData("usps-state-abbrs");
		auxDataRequest.requestAppRefData("iso-country-codes");
		auxDataRequest.requestEntityPrototype(EntityType.ACCOUNT_ADDRESS);
	}

	@Override
	protected void doInit() {
		super.doInit();

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

		paymentInfoPanel.init();
		paymentInfoPanel.setRefWidget(dpPaymentInfo);

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
		fields.addField("paymentInfo", paymentInfoPanel.getFields());

		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer(panel);

		// first row
		canvas.addField(name);
		canvas.addField(status);
		canvas.addField(dateCancelled);
		canvas.addField(currency);
		canvas.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		canvas.addField(parent);
		canvas.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		canvas.addField(dateCreated);
		canvas.stopFlow();
		canvas.addField(dateModified);

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
		FlowPanel fp = new FlowPanel();
		fp.add(persistPymntInfo);
		fp.add(paymentInfoPanel);
		dpPaymentInfo.add(fp);
		canvas.addWidget(dpPaymentInfo);

		dpPaymentInfo.addEventHandler(this);
		dpAddresses.addEventHandler(this);
	}

	@Override
	protected void onBeforeBind(Model model) {
		super.onBeforeBind(model);

		// un-bind existing
		for(Widget w : tabAddresses) {
			fields.removeField(((AccountAddressPanel) w).getFields());
		}
		tabAddresses.clear();

		final PropertyPath path = new PropertyPath();

		// bind
		path.parse("addresses");
		RelatedManyProperty pvAddresses = model.relatedMany(path);
		for(AddressType at : AddressType.values()) {
			AccountAddressPanel aap = null;
			if(pvAddresses != null) {
				Iterator<IndexedProperty> itr = pvAddresses.iterator();
				path.parse("type");
				while(itr.hasNext()) {
					IndexedProperty ip = itr.next();
					if(at == ip.getModel().getValue(path).getValue()) {
						int index = ip.getIndex();
						if(lastAccountAddressIndex < index) lastAccountAddressIndex = index;
						aap = new AccountAddressPanel(at, index);
						tabAddresses.add(aap, at.getName());
						aap.init();
						fields.addField(ip.getPropertyName(), aap.getFields());
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

	public void onClick(Widget sender) {
		if(sender == persistPymntInfo.getCheckBox()) {
			paymentInfoPanel.getFields().setEnabled(persistPymntInfo.getCheckBox().isChecked());
		}
		else if(sender instanceof NoEntityExistsPanel) {
			// this is a non-existant account address
			int selTabIndx = tabAddresses.getTabBar().getSelectedTab();
			assert selTabIndx >= 0;

			AddressType at = (AddressType) ((NoEntityExistsPanel) sender).getRefToken();
			AccountAddressPanel aap = new AccountAddressPanel(at, ++lastAccountAddressIndex);
			aap.init();
			String parentPropPath = PropertyPath.indexedUnbound("addresses", aap.index);
			fields.addField(parentPropPath, aap.getFields());

			tabAddresses.insert(aap, at.getName(), selTabIndx == 0 ? 0 : selTabIndx);
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
