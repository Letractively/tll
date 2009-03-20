package com.tll.client.mock;

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
import com.tll.common.model.mock.AccountStatus;
import com.tll.common.model.mock.AddressType;
import com.tll.common.model.mock.MockModelStubber;

/**
 * ComplexFieldPanel - Contains a simple field panel mocking a related one
 * model, and an indexed field panel mocking a related many model collection.
 * @author jpk
 */
public class ComplexFieldPanel extends FlowFieldPanel {

	/**
	 * RelatedOnePanel
	 * @author jpk
	 */
	class RelatedOnePanel extends FlowFieldPanel {

		@Override
		protected FieldGroup generateFieldGroup() {
			return new MockFieldGroupProviders.PaymentInfoFieldsProvider().getFieldGroup();
		}

		@Override
		public IFieldRenderer<FlowPanel> getRenderer() {
			return new IFieldRenderer<FlowPanel>() {

				@Override
				public void render(FlowPanel panel, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(panel);

					cmpsr.addField(fg.getFieldWidgetByName("ccType"));

					cmpsr.addField(fg.getFieldWidgetByName("ccNum"));
					cmpsr.addField(fg.getFieldWidgetByName("ccCvv2"));
					cmpsr.addField(fg.getFieldWidgetByName("ccExpMonth"));
					cmpsr.addField(fg.getFieldWidgetByName("ccExpYear"));

					cmpsr.newRow();
					cmpsr.addField(fg.getFieldWidgetByName("ccName"));
				}
			};
		}
	} // RelatedOnePanel

	/**
	 * IndexFieldPanel
	 * @author jpk
	 */
	class IndexFieldPanel extends FlowFieldPanel {

		@Override
		protected FieldGroup generateFieldGroup() {
			return new MockFieldGroupProviders.AccountAddressFieldsProvider().getFieldGroup();
		}

		@Override
		public IFieldRenderer<FlowPanel> getRenderer() {
			return new IFieldRenderer<FlowPanel>() {

				public void render(FlowPanel panel, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(panel);

					// account address type/name row
					cmpsr.addField(fg.getFieldWidgetByName("type"));
					cmpsr.addField(fg.getFieldWidget(Model.NAME_PROPERTY));

					// address row
					cmpsr.newRow();
					final FlowPanel fp = new FlowPanel();
					(new IFieldRenderer<FlowPanel>() {

						@Override
						public void render(FlowPanel widget, FieldGroup fg) {
							final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
							cmpsr.setCanvas(widget);

							cmpsr.addField(fg.getFieldWidgetByName("adrsEmailAddress"));

							cmpsr.newRow();
							cmpsr.addField(fg.getFieldWidgetByName("adrsFirstName"));
							cmpsr.addField(fg.getFieldWidgetByName("adrsMi"));
							cmpsr.addField(fg.getFieldWidgetByName("adrsLastName"));

							//cmpsr.newRow();
							//cmpsr.addField(fg.getFieldWidgetByName("adrsAttn"));
							//cmpsr.addField(fg.getFieldWidgetByName("adrsCompany"));

							//cmpsr.newRow();
							//cmpsr.addField(fg.getFieldWidgetByName("adrsAddress1"));

							//cmpsr.newRow();
							//cmpsr.addField(fg.getFieldWidgetByName("adrsAddress2"));

							cmpsr.newRow();
							cmpsr.addField(fg.getFieldWidgetByName("adrsCity"));
							cmpsr.addField(fg.getFieldWidgetByName("adrsProvince"));

							cmpsr.newRow();
							//cmpsr.addField(fg.getFieldWidgetByName("adrsPostalCode"));
							cmpsr.addField(fg.getFieldWidgetByName("adrsCountry"));

							cmpsr.addField(fg.getFieldWidgetByName("adrsBoolean"));
							cmpsr.addField(fg.getFieldWidgetByName("adrsFloat"));
							cmpsr.addField(fg.getFieldWidgetByName("adrsDouble"));
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
	class IndexedFieldPanel extends TabbedIndexedFieldPanel<IndexFieldPanel> {

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

	final RelatedOnePanel relatedOnePanel;

	final IndexedFieldPanel indexedPanel;

	/**
	 * Constructor
	 */
	public ComplexFieldPanel() {
		super();
		relatedOnePanel = new RelatedOnePanel();
		indexedPanel = new IndexedFieldPanel();
	}

	@Override
	public IFieldRenderer<FlowPanel> getRenderer() {
		return new IFieldRenderer<FlowPanel>() {

			public void render(FlowPanel widget, FieldGroup fg) {
				final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
				cmpsr.setCanvas(widget);

				// first row
				cmpsr.addField(fg.getFieldWidgetByName(Model.NAME_PROPERTY));
				cmpsr.addField(fg.getFieldWidgetByName("acntStatus"));
				cmpsr.addField(fg.getFieldWidgetByName("acntDateCancelled"));
				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				cmpsr.addField(fg.getFieldWidgetByName("acntParentName"));
				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				cmpsr.addField(fg.getFieldWidgetByName(Model.DATE_CREATED_PROPERTY));
				cmpsr.stopFlow();
				cmpsr.addField(fg.getFieldWidgetByName(Model.DATE_MODIFIED_PROPERTY));

				// second row (billing)
				cmpsr.newRow();
				cmpsr.addField(fg.getFieldWidgetByName("acntBillingModel"));
				cmpsr.addField(fg.getFieldWidgetByName("acntBillingCycle"));
				cmpsr.addField(fg.getFieldWidgetByName("acntDateLastCharged"));
				cmpsr.addField(fg.getFieldWidgetByName("acntNextChargeDate"));

				// related one panel
				cmpsr.newRow();
				cmpsr.addWidget(relatedOnePanel);

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
				final FieldGroup fg = (new MockFieldGroupProviders.AccountFieldsProvider()).getFieldGroup();
				fg.addField("paymentInfo", relatedOnePanel.getFieldGroup());
				fg.addField("addresses", indexedPanel.getFieldGroup());
				return fg;
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