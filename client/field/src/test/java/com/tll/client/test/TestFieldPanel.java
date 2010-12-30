package com.tll.client.test;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.tll.client.ui.field.AbstractFieldPanel;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldGroupProvider;
import com.tll.client.ui.field.IFieldRenderer;

/**
 * TestFieldPanel - Contains a simple field panel mocking a related one
 * model, and an indexed field panel mocking a related many model collection.
 * @author jpk
 */
public class TestFieldPanel extends AbstractFieldPanel<FlowPanel> {

	static class TestIndexFieldPanel extends AbstractFieldPanel<FlowPanel> {

		final FlowPanel fp = new FlowPanel();
		
		public TestIndexFieldPanel() {
			super();
			initWidget(fp);
		}

		@Override
		protected FieldGroup generateFieldGroup() {
			return new TestFieldGroupProviders.AccountAddressFieldsProvider().getFieldGroup();
		}

		@Override
		public IFieldRenderer<FlowPanel> getRenderer() {
			return new IFieldRenderer<FlowPanel>() {

				@Override
				public void render(FlowPanel pnl, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(pnl);

					// account address type/name row
					cmpsr.addField(fg.getFieldWidget("type"));
					cmpsr.addField(fg.getFieldWidget("aaname"));

					// address row
					cmpsr.newRow();
					final FlowPanel afp = new FlowPanel();
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
					}).render(afp, (FieldGroup) fg.getFieldByName("address"));
					cmpsr.addWidget(afp);
				}
			};
		}

	} // TestIndexFieldPanel

	/*
	static class TestIndexedFieldPanel extends TabbedIndexedFieldPanel<TestIndexFieldPanel> {

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
	*/

	final FlowPanel pnl = new FlowPanel();
	
	//final TestIndexedFieldPanel indexedPanel;

	public TestFieldPanel() {
		super();
		initWidget(pnl);
		//indexedPanel = new TestIndexedFieldPanel();
	}

	@Override
	public IFieldRenderer<FlowPanel> getRenderer() {
		return new IFieldRenderer<FlowPanel>() {

			@Override
			public void render(FlowPanel widget, FieldGroup fg) {
				final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
				cmpsr.setCanvas(widget);

				// first row
				cmpsr.addField(fg.getFieldWidget("acntname"));
				cmpsr.addField(fg.getFieldWidget("acntStatus"));
				cmpsr.addField(fg.getFieldWidget("acntDateCancelled"));
				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				cmpsr.addField(fg.getFieldWidget("acntParentName"));
				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				cmpsr.addField(fg.getFieldWidget("acntdateCreated"));
				cmpsr.stopFlow();
				cmpsr.addField(fg.getFieldWidget("acntdateModified"));

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
				//cmpsr.addWidget(indexedPanel);
			}
		};
	}

	@Override
	protected FieldGroup generateFieldGroup() {
		final FieldGroup fg = (new IFieldGroupProvider() {

			@Override
			public FieldGroup getFieldGroup() {
				final FieldGroup fgroup = (new TestFieldGroupProviders.AccountFieldsProvider()).getFieldGroup();
				fgroup.addField("paymentInfo", (new TestFieldGroupProviders.PaymentInfoFieldsProvider()).getFieldGroup());
				//fgroup.addField("addresses", indexedPanel.getFieldGroup());
				return fgroup;
			}
		}).getFieldGroup();

		//relatedOnePanel.getFieldGroup().setFeedbackWidget(dpPaymentInfo);
		//addressesPanel.getFieldGroup().setFeedbackWidget(dpAddresses);

		fg.getFieldWidgetByProperty("parent.name").setReadOnly(true);

		/*
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
		*/

		return fg;
	}

	/*
	@Override
	public IIndexedFieldBoundWidget[] getIndexedChildren() {
		return new IIndexedFieldBoundWidget[] { indexedPanel };
	}
	*/
}