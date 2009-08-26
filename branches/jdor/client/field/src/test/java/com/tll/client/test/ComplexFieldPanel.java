package com.tll.client.test;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowFieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldGroupProvider;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.IIndexedFieldBoundWidget;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.common.model.test.AccountStatus;
import com.tll.common.model.test.AddressType;
import com.tll.common.model.test.MockModelStubber;

/**
 * ComplexFieldPanel - Contains a simple field panel mocking a related one
 * model, and an indexed field panel mocking a related many model collection.
 * @author jpk
 */
public class ComplexFieldPanel extends FlowFieldPanel {

	/**
	 * IndexFieldPanel
	 * @author jpk
	 */
	static class IndexFieldPanel extends FlowFieldPanel {

		@Override
		protected FieldGroup generateFieldGroup() {
			return new MockFieldGroupProviders.AccountAddressFieldsProvider().getFieldGroup();
		}

		@Override
		public IFieldRenderer<FlowPanel> getRenderer() {
			return new IFieldRenderer<FlowPanel>() {

				public void render(FlowPanel pnl, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(pnl);

					// account address type/name row
					cmpsr.addField(fg.getFieldWidgetByName("type"));
					cmpsr.addField(fg.getFieldWidgetByName("aa" + Model.NAME_PROPERTY));

					// address row
					cmpsr.newRow();
					final FlowPanel fp = new FlowPanel();
					(new IFieldRenderer<FlowPanel>() {

						@Override
						public void render(FlowPanel widget, FieldGroup fgroup) {
							final FlowPanelFieldComposer c = new FlowPanelFieldComposer();
							c.setCanvas(widget);

							c.addField(fgroup.getFieldWidgetByName("adrsEmailAddress"));

							c.newRow();
							c.addField(fgroup.getFieldWidgetByName("adrsFirstName"));
							c.addField(fgroup.getFieldWidgetByName("adrsMi"));
							c.addField(fgroup.getFieldWidgetByName("adrsLastName"));

							//cmpsr.newRow();
							//cmpsr.addField(fg.getFieldWidgetByName("adrsAttn"));
							//cmpsr.addField(fg.getFieldWidgetByName("adrsCompany"));

							//cmpsr.newRow();
							//cmpsr.addField(fg.getFieldWidgetByName("adrsAddress1"));

							//cmpsr.newRow();
							//cmpsr.addField(fg.getFieldWidgetByName("adrsAddress2"));

							c.newRow();
							c.addField(fgroup.getFieldWidgetByName("adrsCity"));
							c.addField(fgroup.getFieldWidgetByName("adrsProvince"));

							c.newRow();
							//cmpsr.addField(fg.getFieldWidgetByName("adrsPostalCode"));
							c.addField(fgroup.getFieldWidgetByName("adrsCountry"));

							c.addField(fgroup.getFieldWidgetByName("adrsBoolean"));
							c.addField(fgroup.getFieldWidgetByName("adrsFloat"));
							c.addField(fgroup.getFieldWidgetByName("adrsDouble"));
						}
					}).render(fp, (FieldGroup) fg.getFieldByName("address"));
					cmpsr.addWidget(fp);
				}
			};
		}

	} // IndexFieldPanel

	/**
	 * IndexedFieldPanel
	 * @author jpk
	 */
	static class IndexedFieldPanel extends TabbedIndexedFieldPanel<IndexFieldPanel> {

		/**
		 * Constructor
		 */
		public IndexedFieldPanel() {
			super("Addresses", "addresses", true, true);
		}

		@Override
		protected String getIndexTypeName() {
			return "Account Address";
		}

		@Override
		protected String getInstanceName(IndexFieldPanel index) {
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
			final Model m = MockModelStubber.stubAccountAddress(null, MockModelStubber.stubAddress(1), 1);
			m.clearPropertyValues(false);
			return m;
		}

		@Override
		protected IndexFieldPanel createIndexPanel() {
			return new IndexFieldPanel();
		}

	} // IndexedFieldPanel

	final IndexedFieldPanel indexedPanel;

	/**
	 * Constructor
	 */
	public ComplexFieldPanel() {
		super();
		indexedPanel = new IndexedFieldPanel();
	}

	@Override
	public IFieldRenderer<FlowPanel> getRenderer() {
		return new IFieldRenderer<FlowPanel>() {

			public void render(FlowPanel widget, FieldGroup fg) {
				final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
				cmpsr.setCanvas(widget);

				// first row
				cmpsr.addField(fg.getFieldWidgetByName("acnt" + Model.NAME_PROPERTY));
				cmpsr.addField(fg.getFieldWidgetByName("acntStatus"));
				cmpsr.addField(fg.getFieldWidgetByName("acntDateCancelled"));
				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				cmpsr.addField(fg.getFieldWidgetByName("acntParentName"));
				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				cmpsr.addField(fg.getFieldWidgetByName("acnt" + Model.DATE_CREATED_PROPERTY));
				cmpsr.stopFlow();
				cmpsr.addField(fg.getFieldWidgetByName("acnt" + Model.DATE_MODIFIED_PROPERTY));

				// second row (billing)
				cmpsr.newRow();
				cmpsr.addField(fg.getFieldWidgetByName("acntBillingModel"));
				cmpsr.addField(fg.getFieldWidgetByName("acntBillingCycle"));
				cmpsr.addField(fg.getFieldWidgetByName("acntDateLastCharged"));
				cmpsr.addField(fg.getFieldWidgetByName("acntNextChargeDate"));

				// related one panel
				cmpsr.newRow();
				cmpsr.addField(fg.getFieldWidgetByName("ccType"));

				cmpsr.addField(fg.getFieldWidgetByName("ccNum"));
				cmpsr.addField(fg.getFieldWidgetByName("ccCvv2"));
				cmpsr.addField(fg.getFieldWidgetByName("ccExpMonth"));
				cmpsr.addField(fg.getFieldWidgetByName("ccExpYear"));

				cmpsr.newRow();
				cmpsr.addField(fg.getFieldWidgetByName("ccName"));

				// related many (indexed) panel
				cmpsr.newRow();
				cmpsr.addWidget(indexedPanel);
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	protected FieldGroup generateFieldGroup() {
		final FieldGroup fg = (new IFieldGroupProvider() {

			public FieldGroup getFieldGroup() {
				final FieldGroup fgroup = (new MockFieldGroupProviders.AccountFieldsProvider()).getFieldGroup();
				fgroup.addField("paymentInfo", (new MockFieldGroupProviders.PaymentInfoFieldsProvider()).getFieldGroup());
				fgroup.addField("addresses", indexedPanel.getFieldGroup());
				return fgroup;
			}
		}).getFieldGroup();

		//relatedOnePanel.getFieldGroup().setFeedbackWidget(dpPaymentInfo);
		//addressesPanel.getFieldGroup().setFeedbackWidget(dpAddresses);

		fg.getFieldWidget("parent.name").setReadOnly(true);

		((IFieldWidget<AccountStatus>) fg.getFieldWidget("status"))
		.addValueChangeHandler(new ValueChangeHandler<AccountStatus>() {

			@Override
			public void onValueChange(ValueChangeEvent<AccountStatus> event) {
				final boolean closed = event.getValue() == AccountStatus.CLOSED;
				final IFieldWidget<?> f = getFieldGroup().getFieldWidget("dateCancelled");
				f.setVisible(closed);
				f.setRequired(closed);
			}
		});

		((IFieldWidget<Boolean>) fg.getFieldWidget("persistPymntInfo"))
		.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			public void onValueChange(ValueChangeEvent<Boolean> event) {
				indexedPanel.getFieldGroup().setEnabled(event.getValue());
			}
		});

		return fg;
	}

	@Override
	public IIndexedFieldBoundWidget[] getIndexedChildren() {
		return new IIndexedFieldBoundWidget[] { indexedPanel };
	}
}