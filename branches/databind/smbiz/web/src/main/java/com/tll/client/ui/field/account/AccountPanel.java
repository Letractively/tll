/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.ui.field.account;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.ModelAssembler;
import com.tll.client.ui.field.AbstractBindableFlowFieldPanel;
import com.tll.client.ui.field.AddressFieldsRenderer;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldGroupProvider;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.IIndexedFieldBoundWidget;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.common.model.SmbizEntityType;
import com.tll.model.AccountStatus;
import com.tll.model.AddressType;

/**
 * AccountPanel
 * @author jpk
 */
public class AccountPanel extends AbstractBindableFlowFieldPanel {

	/**
	 * AccountAddressPanel
	 * @author jpk
	 */
	static final class AccountAddressPanel extends AbstractBindableFlowFieldPanel {

		@Override
		protected FieldGroup generateFieldGroup() {
			return (new AccountAddressFieldProvider()).getFieldGroup();
		}

		@Override
		public IFieldRenderer<FlowPanel> getRenderer() {
			return new IFieldRenderer<FlowPanel>() {

				public void render(FlowPanel pnl, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(pnl);

					// account address type/name row
					cmpsr.addField(fg.getFieldWidget("aatype"));
					cmpsr.addField(fg.getFieldWidget("aa" + Model.NAME_PROPERTY));

					// address row
					cmpsr.newRow();
					final FlowPanel fp = new FlowPanel();
					final AddressFieldsRenderer r = new AddressFieldsRenderer();
					r.render(fp, (FieldGroup) fg.getFieldByName("address"));
					cmpsr.addWidget(fp);
				}
			};
		}
	} // AccountAddressPanel

	/**
	 * AddressesPanel
	 * @author jpk
	 */
	static final class AddressesPanel extends TabbedIndexedFieldPanel<AccountAddressPanel> {

		/**
		 * Constructor
		 */
		public AddressesPanel() {
			super("Addresses", "addresses", true, true);
		}

		@Override
		protected String getIndexTypeName() {
			return "Account Address";
		}

		@Override
		protected String getInstanceName(AccountAddressPanel index) {
			AddressType type;
			String aaName;
			try {
				type = (AddressType) index.getModel().getProperty("type");
				aaName = (String) index.getModel().getProperty("name");
			}
			catch(final PropertyPathException e) {
				throw new IllegalStateException(e);
			}

			return aaName + " (" + type.getName() + ")";
		}

		@Override
		protected Model createPrototypeModel() {
			return ModelAssembler.assemble(SmbizEntityType.ACCOUNT_ADDRESS);
		}

		@Override
		protected AccountAddressPanel createIndexPanel() {
			return new AccountAddressPanel();
		}

	} // AddressesPanel

	protected final DisclosurePanel dpPaymentInfo = new DisclosurePanel("Payment Info");
	protected final PaymentInfoPanel paymentInfoPanel = new PaymentInfoPanel();

	protected final DisclosurePanel dpAddresses = new DisclosurePanel("Addresses");
	protected final AddressesPanel addressesPanel = new AddressesPanel();

	@SuppressWarnings("unchecked")
	@Override
	protected FieldGroup generateFieldGroup() {
		final FieldGroup fg = (new IFieldGroupProvider() {

			public FieldGroup getFieldGroup() {
				final FieldGroup fgroup = (new AccountFieldsProvider()).getFieldGroup();
				fgroup.addField("paymentInfo", paymentInfoPanel.getFieldGroup());
				fgroup.addField("addresses", addressesPanel.getFieldGroup());
				return fgroup;
			}
		}).getFieldGroup();

		paymentInfoPanel.getFieldGroup().setWidget(dpPaymentInfo);
		addressesPanel.getFieldGroup().setWidget(dpAddresses);

		fg.getFieldWidget("acntParentName").setReadOnly(true);

		((IFieldWidget<AccountStatus>) fg.getFieldWidget("acntStatus"))
		.addValueChangeHandler(new ValueChangeHandler<AccountStatus>() {

			@Override
			public void onValueChange(ValueChangeEvent<AccountStatus> event) {
				final boolean closed = event.getValue() == AccountStatus.CLOSED;
				final IFieldWidget<?> f = getFieldGroup().getFieldWidget("acntDateCancelled");
				f.setVisible(closed);
				f.setRequired(closed);
			}
		});

		((IFieldWidget<Boolean>) fg.getFieldWidget("acntPersistPymntInfo"))
		.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			public void onValueChange(ValueChangeEvent<Boolean> event) {
				paymentInfoPanel.enable(event.getValue().booleanValue());
			}
		});

		return fg;
	}

	@Override
	public IFieldRenderer<FlowPanel> getRenderer() {
		return new IFieldRenderer<FlowPanel>() {

			@Override
			public void render(FlowPanel widget, FieldGroup fg) {
				final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
				cmpsr.setCanvas(widget);

				// first row
				cmpsr.addField(fg.getFieldWidget("acnt" + Model.NAME_PROPERTY));
				cmpsr.addField(fg.getFieldWidget("acntStatus"));
				cmpsr.addField(fg.getFieldWidget("acntDateCancelled"));
				cmpsr.addField(fg.getFieldWidget("acntCurrencyId"));
				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				cmpsr.addField(fg.getFieldWidget("acntParentName"));
				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				cmpsr.addField(fg.getFieldWidget("acnt" + Model.DATE_CREATED_PROPERTY));
				cmpsr.stopFlow();
				cmpsr.addField(fg.getFieldWidget("acnt" + Model.DATE_MODIFIED_PROPERTY));

				// second row (billing)
				cmpsr.newRow();
				cmpsr.addField(fg.getFieldWidget("acntBillingModel"));
				cmpsr.addField(fg.getFieldWidget("acntBillingCycle"));
				cmpsr.addField(fg.getFieldWidget("acntDateLastCharged"));
				cmpsr.addField(fg.getFieldWidget("acntNextChargeDate"));

				// third row
				cmpsr.newRow();
				// account addresses block
				dpAddresses.add(addressesPanel);
				cmpsr.addWidget(dpAddresses);

				// fourth row
				cmpsr.newRow();
				// payment info block
				final FlowPanel fp = new FlowPanel();
				fp.add((Widget) fg.getFieldWidget("acntPersistPymntInfo"));
				fp.add(paymentInfoPanel);
				dpPaymentInfo.add(fp);
				cmpsr.addWidget(dpPaymentInfo);
			}
		};
	}

	@Override
	public IIndexedFieldBoundWidget[] getIndexedChildren() {
		return new IIndexedFieldBoundWidget[] { addressesPanel };
	}
}