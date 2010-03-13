package com.tll.client.test;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.AbstractBindableFlowFieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldGroupProvider;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.IIndexedFieldBoundWidget;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.common.model.test.TestModelStubber;
import com.tll.model.test.AccountStatus;
import com.tll.model.test.AddressType;

/**
 * TestFieldPanel - Contains a simple field panel mocking a related one
 * model, and an indexed field panel mocking a related many model collection.
 * @author jpk
 */
public class TestFieldPanel extends AbstractBindableFlowFieldPanel {

	/**
	 * TestIndexFieldPanel
	 * @author jpk
	 */
	static class TestIndexFieldPanel extends AbstractBindableFlowFieldPanel {

		@Override
		protected FieldGroup generateFieldGroup() {
			return new TestFieldGroupProviders.AccountAddressFieldsProvider().getFieldGroup();
		}

		@Override
		public IFieldRenderer<FlowPanel> getRenderer() {
			return new IFieldRenderer<FlowPanel>() {

				public void render(FlowPanel pnl, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(pnl);

					// account address type/name row
					cmpsr.addField(fg.getFieldWidget("type"));
					cmpsr.addField(fg.getFieldWidget("aa" + Model.NAME_PROPERTY));

					// address row
					cmpsr.newRow();
					final FlowPanel fp = new FlowPanel();
					(new IFieldRenderer<FlowPanel>() {

						@Override
						public void render(FlowPanel widget, FieldGroup fgroup) {
							final FlowPanelFieldComposer c = new FlowPanelFieldComposer();
							c.setCanvas(widget);

							c.addField(fgroup.getFieldWidget("adrsEmailAddress"));

							c.newRow();
							c.addField(fgroup.getFieldWidget("adrsFirstName"));
							c.addField(fgroup.getFieldWidget("adrsMi"));
							c.addField(fgroup.getFieldWidget("adrsLastName"));

							//cmpsr.newRow();
							//cmpsr.addField(fg.getFieldWidgetByName("adrsAttn"));
							//cmpsr.addField(fg.getFieldWidgetByName("adrsCompany"));

							//cmpsr.newRow();
							//cmpsr.addField(fg.getFieldWidgetByName("adrsAddress1"));

							//cmpsr.newRow();
							//cmpsr.addField(fg.getFieldWidgetByName("adrsAddress2"));

							c.newRow();
							c.addField(fgroup.getFieldWidget("adrsCity"));
							c.addField(fgroup.getFieldWidget("adrsProvince"));

							c.newRow();
							//cmpsr.addField(fg.getFieldWidgetByName("adrsPostalCode"));
							c.addField(fgroup.getFieldWidget("adrsCountry"));

							c.addField(fgroup.getFieldWidget("adrsBoolean"));
							c.addField(fgroup.getFieldWidget("adrsFloat"));
							c.addField(fgroup.getFieldWidget("adrsDouble"));
						}
					}).render(fp, (FieldGroup) fg.getFieldByName("address"));
					cmpsr.addWidget(fp);
				}
			};
		}

	} // TestIndexFieldPanel

	/**
	 * TestIndexedFieldPanel
	 * @author jpk
	 */
	static class TestIndexedFieldPanel extends TabbedIndexedFieldPanel<TestIndexFieldPanel> {

		/**
		 * Constructor
		 */
		public TestIndexedFieldPanel() {
			super("Addresses", "addresses", true, true);
		}

		@Override
		protected String getIndexTypeName() {
			return "Account Address";
		}

		@Override
		protected String getInstanceName(TestIndexFieldPanel index) {
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
			final Model m = TestModelStubber.stubAccountAddress(null, TestModelStubber.stubAddress(1), 1);
			m.clearPropertyValues(false, true);
			return m;
		}

		@Override
		protected TestIndexFieldPanel createIndexPanel() {
			return new TestIndexFieldPanel();
		}

	} // TestIndexedFieldPanel

	final TestIndexedFieldPanel indexedPanel;

	/**
	 * Constructor
	 */
	public TestFieldPanel() {
		super();
		indexedPanel = new TestIndexedFieldPanel();
	}

	@Override
	public IFieldRenderer<FlowPanel> getRenderer() {
		return new IFieldRenderer<FlowPanel>() {

			public void render(FlowPanel widget, FieldGroup fg) {
				final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
				cmpsr.setCanvas(widget);

				// first row
				cmpsr.addField(fg.getFieldWidget("acnt" + Model.NAME_PROPERTY));
				cmpsr.addField(fg.getFieldWidget("acntStatus"));
				cmpsr.addField(fg.getFieldWidget("acntDateCancelled"));
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

				// related one panel
				cmpsr.newRow();
				cmpsr.addField(fg.getFieldWidget("ccType"));

				cmpsr.addField(fg.getFieldWidget("ccNum"));
				cmpsr.addField(fg.getFieldWidget("ccCvv2"));
				cmpsr.addField(fg.getFieldWidget("ccExpMonth"));
				cmpsr.addField(fg.getFieldWidget("ccExpYear"));

				cmpsr.newRow();
				cmpsr.addField(fg.getFieldWidget("ccName"));

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
				final FieldGroup fgroup = (new TestFieldGroupProviders.AccountFieldsProvider()).getFieldGroup();
				fgroup.addField("paymentInfo", (new TestFieldGroupProviders.PaymentInfoFieldsProvider()).getFieldGroup());
				fgroup.addField("addresses", indexedPanel.getFieldGroup());
				return fgroup;
			}
		}).getFieldGroup();

		//relatedOnePanel.getFieldGroup().setFeedbackWidget(dpPaymentInfo);
		//addressesPanel.getFieldGroup().setFeedbackWidget(dpAddresses);

		fg.getFieldWidgetByProperty("parent.name").setReadOnly(true);

		((IFieldWidget<AccountStatus>) fg.getFieldWidgetByProperty("status"))
		.addValueChangeHandler(new ValueChangeHandler<AccountStatus>() {

			@Override
			public void onValueChange(ValueChangeEvent<AccountStatus> event) {
				final boolean closed = event.getValue() == AccountStatus.CLOSED;
				final IFieldWidget<?> f = getFieldGroup().getFieldWidgetByProperty("dateCancelled");
				f.setVisible(closed);
				f.setRequired(closed);
			}
		});

		((IFieldWidget<Boolean>) fg.getFieldWidgetByProperty("persistPymntInfo"))
		.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			public void onValueChange(ValueChangeEvent<Boolean> event) {
				indexedPanel.getFieldGroup().setEnabled(event.getValue().booleanValue());
			}
		});

		return fg;
	}

	@Override
	public IIndexedFieldBoundWidget[] getIndexedChildren() {
		return new IIndexedFieldBoundWidget[] { indexedPanel };
	}
}