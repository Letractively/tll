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
import com.tll.client.field.FieldBindingGroup;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.IField;
import com.tll.client.model.IndexedProperty;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPath;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.field.AbstractField;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DeleteTabWidget;
import com.tll.client.ui.field.FieldFactory;
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
			name = FieldFactory.createNameEntityField();
			// TODO fix since we don't have data fields anymore
			// fields.addField(FieldFactory.fdata("type", addressType));
			fields.addField(name);
			fields.addField("address", addressPanel.getFieldGroup());
		}

		@Override
		protected void populateFieldBindingGroup(FieldBindingGroup bindings, String parentPropertyPath, FieldGroup fields,
				Model model) {
		}

		@Override
		protected void draw(Panel canvas, FieldGroup fields) {
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
	public void populateFieldGroup(final FieldGroup fields) {
		IField f;

		fields.addField(FieldFactory.createNameEntityField());
		fields.addFields(FieldFactory.createTimestampEntityFields());

		f = FieldFactory.ftext("parent.name", "Parent", 15);
		f.setReadOnly(true);
		fields.addField(f);

		f = FieldFactory.fselect("status", "Status", ClientEnumUtil.toMap(AccountStatus.class));
		((SelectField) f).getListBox().addChangeListener(new ChangeListener() {

			public void onChange(Widget sender) {
				String s = fields.getField("status").getValue().toLowerCase();
				final boolean closed = "closed".equals(s);
				IField f = fields.getField("dateCancelled");
				f.setVisible(closed);
				f.setRequired(closed);
			}
		});

		fields.addField(FieldFactory.fdate("dateCancelled", "Date Cancelled", GlobalFormat.DATE));

		fields.addField(FieldFactory.fselect("currency.id", "Currency", AuxDataCache.instance().getCurrencyDataMap()));

		fields.addField(FieldFactory.ftext("billingModel", "Billing Model", 18));
		fields.addField(FieldFactory.ftext("billingCycle", "Billing Cycle", 18));
		fields.addField(FieldFactory.fdate("dateLastCharged", "Last Charged", GlobalFormat.DATE));
		fields.addField(FieldFactory.fdate("nextChargeDate", "Next Charge", GlobalFormat.DATE));

		f = FieldFactory.fbool("persistPymntInfo", "PersistPayment Info?");
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
	protected void populateFieldBindingGroup(FieldBindingGroup bindings, String parentPropertyPath, FieldGroup fields,
			Model model) {
	}

	@Override
	protected void draw(Panel canvas, FieldGroup fields) {
		final FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
		cmpsr.setCanvas(canvas);

		// first row
		cmpsr.addField((AbstractField) fields.getField(Model.NAME_PROPERTY));
		cmpsr.addField((AbstractField) fields.getField("status"));
		cmpsr.addField((AbstractField) fields.getField("dateCancelled"));
		cmpsr.addField((AbstractField) fields.getField("currency.id"));
		cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		cmpsr.addField((AbstractField) fields.getField("parent.name"));
		cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		cmpsr.addField((AbstractField) fields.getField("dateCreated"));
		cmpsr.stopFlow();
		cmpsr.addField((AbstractField) fields.getField("dateModified"));

		// second row (billing)
		cmpsr.newRow();
		cmpsr.addField((AbstractField) fields.getField("billingModel"));
		cmpsr.addField((AbstractField) fields.getField("billingCycle"));
		cmpsr.addField((AbstractField) fields.getField("dateLastCharged"));
		cmpsr.addField((AbstractField) fields.getField("nextChargeDate"));

		// third row
		cmpsr.newRow();
		// account addresses block
		dpAddresses.add(tabAddresses);
		cmpsr.addWidget(dpAddresses);

		// payment info block
		FlowPanel fp = new FlowPanel();
		fp.add((AbstractField) fields.getField("persistPymntInfo"));
		fp.add(paymentInfoPanel);
		dpPaymentInfo.add(fp);
		cmpsr.addWidget(dpPaymentInfo);

		dpPaymentInfo.addEventHandler(this);
		dpAddresses.addEventHandler(this);
	}

	@Override
	protected void applyModel(Model model, final FieldGroup fields) {
		// un-bind existing
		for(Widget w : tabAddresses) {
			if(w instanceof AccountAddressPanel) {
				fields.removeField(((AccountAddressPanel) w).getFieldGroup());
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
					if(at == ip.getModel().getPropertyValue(path).getValue()) {
						aap = new AccountAddressPanel(at);
						fields.addField(ip.getPropertyName(), aap.getFieldGroup());
						tabAddresses.add(aap, new DeleteTabWidget(at.getName(), aap.getFieldGroup(), ip.getPropertyName()));
						break;
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
						fields.addField(PropertyPath.indexUnbound("addresses"), aap.getFieldGroup());

						// bind to prototype
						Model aaproto = AuxDataCache.instance().getEntityPrototype(EntityType.ACCOUNT_ADDRESS);
						if(aaproto == null) throw new IllegalStateException();
						// NOTE: we need a property path offset here since the fields'
						// property
						// paths are relative to ACCOUNT!
						aap.getFieldGroup().bindModel(1, aaproto.getSelfRef());

						tabAddresses.insert(aap, new DeleteTabWidget(at.getName(), aap.getFieldGroup(), null), selTabIndx == 0 ? 0
								: selTabIndx);
						tabAddresses.remove(selTabIndx + 1);
						tabAddresses.selectTab(selTabIndx);
					}
				});
				tabAddresses.add(neep, at.getName());
			}
		}

		// show/hide date cancelled according to the account's status
		String status = model.asString("status");
		status = status == null ? null : status.toLowerCase();
		fields.getField("dateCancelled").setVisible("closed".equals(status));

		// dpPaymentInfo.setOpen(false);
		// dpAddresses.setOpen(false);
		// tabAddresses.selectTab(0);
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
				if(w instanceof AccountAddressPanel) MsgManager.instance.show(w, false, true);
			}
		}
		return true;
	}

	public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
		// no-op
	}
}
