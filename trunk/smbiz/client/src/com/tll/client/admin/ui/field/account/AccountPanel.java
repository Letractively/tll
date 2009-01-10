/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.admin.ui.field.account;

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
import com.tll.client.admin.ui.field.AddressFieldsRenderer;
import com.tll.client.bind.AbstractModelEditAction;
import com.tll.client.bind.IBindable;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPathException;
import com.tll.client.msg.MsgManager;
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
public class AccountPanel<M extends IBindable> extends FieldPanel<M> {

	/**
	 * AccountEditAction
	 * @author jpk
	 */
	class AccountEditAction extends AbstractModelEditAction<M, AccountPanel<M>> {

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

	class AccountFieldsRenderer implements IFieldRenderer {

		public void render(Panel panel, FieldGroup fg, String parentPropPath) {
			final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
			cmpsr.setCanvas(panel);

			// first row
			cmpsr.addField(fg.getField(parentPropPath, Model.NAME_PROPERTY));
			cmpsr.addField(fg.getField(parentPropPath, "status"));
			cmpsr.addField(fg.getField(parentPropPath, "dateCancelled"));
			cmpsr.addField(fg.getField(parentPropPath, "currency.id"));
			cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			cmpsr.addField(fg.getField(parentPropPath, "parent.name"));
			cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			cmpsr.addField(fg.getField(parentPropPath, Model.DATE_CREATED_PROPERTY));
			cmpsr.stopFlow();
			cmpsr.addField(fg.getField(parentPropPath, Model.DATE_MODIFIED_PROPERTY));

			// second row (billing)
			cmpsr.newRow();
			cmpsr.addField(fg.getField(parentPropPath, "billingModel"));
			cmpsr.addField(fg.getField(parentPropPath, "billingCycle"));
			cmpsr.addField(fg.getField(parentPropPath, "dateLastCharged"));
			cmpsr.addField(fg.getField(parentPropPath, "nextChargeDate"));

			// third row
			cmpsr.newRow();
			// account addresses block
			dpAddresses.add(addressesPanel);
			cmpsr.addWidget(dpAddresses);

			// payment info block
			FlowPanel fp = new FlowPanel();
			fp.add((Widget) fg.getField(parentPropPath, "persistPymntInfo"));
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

				public void render(Panel panel, FieldGroup fg, String parentPropPath) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(panel);

					// account address name row
					cmpsr.addField(fg.getField(parentPropPath, Model.NAME_PROPERTY));

					// address row
					cmpsr.newRow();
					FlowPanel fp = new FlowPanel();
					AddressFieldsRenderer r = new AddressFieldsRenderer();
					r.render(fp, (FieldGroup) fg.getField(parentPropPath, "address"), parentPropPath);
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
	static final class AddressesPanel<M extends IBindable> extends IndexedFieldPanel<M> implements TabListener {

		private final TabPanel tabAddresses = new TabPanel();

		/**
		 * Constructor
		 */
		public AddressesPanel() {
			super(/*"addresses", */new AccountAddressFieldProvider());

			// listen to tab events
			tabAddresses.addTabListener(this);
			initWidget(tabAddresses);
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
		// setAction(new AccountEditAction<M, AccountPanel<M>>());
		setRenderer(new AccountFieldsRenderer());
	}

	/*
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
			paymentInfoPanel.setProperty(propPath, value);
		}
		else if("addresses".equals(propPath)) {
			addressesPanel.setProperty(propPath, value);
		}
		else {
			super.setProperty(propPath, value);
		}
	}
	*/

	@Override
	protected FieldGroup generateFieldGroup() {
		FieldGroup fg = (new IFieldGroupProvider() {

			public FieldGroup getFieldGroup() {
				FieldGroup fg = (new AccountFieldsProvider()).getFieldGroup();
				fg.addField("paymentInfo", paymentInfoPanel.getFieldGroup());
				// fg.addField("addresses", addressesPanel.getFieldGroup());
				return fg;
			}
		}).getFieldGroup();

		paymentInfoPanel.getFieldGroup().setFeedbackWidget(dpPaymentInfo);
		// addressesPanel.getFieldGroup().setFeedbackWidget(dpAddresses);

		fg.getField("parent.name").setReadOnly(true);

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

		fg.getField("persistPymntInfo").addChangeListener(new ChangeListener() {

			public void onChange(Widget sender) {
				paymentInfoPanel.getFieldGroup().setEnabled(((CheckBox) sender).isChecked());
			}
		});

		return fg;
	}
}
