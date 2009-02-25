package com.tll.client.mock;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.tll.client.bind.AbstractBindingAction;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IField;
import com.tll.client.ui.field.IFieldGroupProvider;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.common.model.mock.AddressType;
import com.tll.common.model.mock.MockEntityType;

/**
 * ComplexFieldPanel - Contains a simple field panel mocking a related one
 * model, and an indexed field panel mocking a related many model collection.
 * @author jpk
 */
public class ComplexFieldPanel extends MockFieldPanel {

	/**
	 * AccountEditAction
	 * @author jpk
	 */
	class BindingAction extends AbstractBindingAction<Model, ComplexFieldPanel> {

		@Override
		protected void populateBinding(ComplexFieldPanel fp) throws PropertyPathException {
			addFieldBinding(fp, Model.NAME_PROPERTY);
			addFieldBinding(fp, Model.DATE_CREATED_PROPERTY);
			addFieldBinding(fp, Model.DATE_MODIFIED_PROPERTY);
			addFieldBinding(fp, "parent.name");
			addFieldBinding(fp, "status");
			addFieldBinding(fp, "dateCancelled");
			//addFieldBinding(fp, "currency.id");
			addFieldBinding(fp, "billingModel");
			addFieldBinding(fp, "billingCycle");
			addFieldBinding(fp, "dateLastCharged");
			addFieldBinding(fp, "nextChargeDate");
			addFieldBinding(fp, "persistPymntInfo");

			addNestedFieldBindings(fp, "paymentInfo");

			addIndexedFieldBinding(fp.getModel(), "addresses", indexedPanel);
		}
	} // BindingAction

	/**
	 * RelatedOnePanel
	 * @author jpk
	 */
	class RelatedOnePanel extends MockFieldPanel {

		/**
		 * Constructor
		 */
		public RelatedOnePanel() {
			super();
			setRenderer(new IFieldRenderer<FlowPanel>() {

				@Override
				public void render(FlowPanel panel, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(panel);

					cmpsr.addField(fg.getFieldByName("ccType"));

					cmpsr.addField(fg.getFieldByName("ccNum"));
					cmpsr.addField(fg.getFieldByName("ccCvv2"));
					cmpsr.addField(fg.getFieldByName("ccExpMonth"));
					cmpsr.addField(fg.getFieldByName("ccExpYear"));

					cmpsr.newRow();
					cmpsr.addField(fg.getFieldByName("ccName"));
				}
			});
		}

		@Override
		protected FieldGroup generateFieldGroup() {
			return new MockFieldGroupProviders.PaymentInfoFieldsProvider().getFieldGroup();
		}
	} // RelatedOnePanel

	/**
	 * IndexFieldPanel
	 * @author jpk
	 */
	class IndexFieldPanel extends MockFieldPanel {

		/**
		 * Constructor
		 */
		public IndexFieldPanel() {
			super();
			setRenderer(new IFieldRenderer<FlowPanel>() {

				public void render(FlowPanel panel, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(panel);

					// account address type/name row
					cmpsr.addField(fg.getFieldByName("type"));
					cmpsr.addField(fg.getField(Model.NAME_PROPERTY));

					// address row
					cmpsr.newRow();
					final FlowPanel fp = new FlowPanel();
					(new IFieldRenderer<FlowPanel>() {

						@Override
						public void render(FlowPanel widget, FieldGroup fg) {
							final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
							cmpsr.setCanvas(widget);

							cmpsr.addField(fg.getFieldByName("adrsEmailAddress"));

							cmpsr.newRow();
							cmpsr.addField(fg.getFieldByName("adrsFirstName"));
							cmpsr.addField(fg.getFieldByName("adrsMi"));
							cmpsr.addField(fg.getFieldByName("adrsLastName"));

							//cmpsr.newRow();
							//cmpsr.addField(fg.getFieldByName("adrsAttn"));
							//cmpsr.addField(fg.getFieldByName("adrsCompany"));

							//cmpsr.newRow();
							//cmpsr.addField(fg.getFieldByName("adrsAddress1"));

							//cmpsr.newRow();
							//cmpsr.addField(fg.getFieldByName("adrsAddress2"));

							cmpsr.newRow();
							cmpsr.addField(fg.getFieldByName("adrsCity"));
							cmpsr.addField(fg.getFieldByName("adrsProvince"));

							cmpsr.newRow();
							//cmpsr.addField(fg.getFieldByName("adrsPostalCode"));
							cmpsr.addField(fg.getFieldByName("adrsCountry"));
							
							cmpsr.addField(fg.getFieldByName("adrsBoolean"));
							cmpsr.addField(fg.getFieldByName("adrsFloat"));
						}
					}).render(fp, (FieldGroup) fg.getFieldByName("address"));
					cmpsr.addWidget(fp);
				}
			});
		}

		@Override
		protected FieldGroup generateFieldGroup() {
			return new MockFieldGroupProviders.AccountAddressFieldsProvider().getFieldGroup();
		}
	} // IndexFieldPanel

	/**
	 * IndexedFieldPanel
	 * @author jpk
	 */
	class IndexedFieldPanel extends TabbedIndexedFieldPanel<IndexFieldPanel, Model> {

		/**
		 * Constructor
		 */
		public IndexedFieldPanel() {
			super("Addresses", true, true);
		}

		@Override
		protected String getIndexTypeName() {
			return "Account Address";
		}

		@Override
		protected String getTabLabelText(IndexFieldPanel aap) {
			AddressType type;
			String aaName;
			try {
				type = (AddressType) aap.getModel().getProperty("type");
				aaName = (String) aap.getModel().getProperty("name");
			}
			catch(final PropertyPathException e) {
				throw new IllegalStateException(e);
			}

			return aaName + " (" + type.getName() + ")";
		}

		@Override
		protected Model createPrototypeModel() {
			return AuxDataCache.instance().getEntityPrototype(MockEntityType.ACCOUNT_ADDRESS);
		}

		@Override
		protected IndexFieldPanel createIndexPanel(Model indexModel) {
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
		setRenderer(new IFieldRenderer<FlowPanel>() {

			public void render(FlowPanel widget, FieldGroup fg) {
				final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
				cmpsr.setCanvas(panel);

				// first row
				cmpsr.addField(fg.getFieldByName(Model.NAME_PROPERTY));
				cmpsr.addField(fg.getFieldByName("acntStatus"));
				cmpsr.addField(fg.getFieldByName("acntDateCancelled"));
				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				cmpsr.addField(fg.getFieldByName("acntParentName"));
				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				cmpsr.addField(fg.getFieldByName(Model.DATE_CREATED_PROPERTY));
				cmpsr.stopFlow();
				cmpsr.addField(fg.getFieldByName(Model.DATE_MODIFIED_PROPERTY));

				// second row (billing)
				cmpsr.newRow();
				cmpsr.addField(fg.getFieldByName("acntBillingModel"));
				cmpsr.addField(fg.getFieldByName("acntBillingCycle"));
				cmpsr.addField(fg.getFieldByName("acntDateLastCharged"));
				cmpsr.addField(fg.getFieldByName("acntNextChargeDate"));

				// related one panel
				cmpsr.newRow();
				cmpsr.addWidget(relatedOnePanel);

				// related many (indexed) panel
				cmpsr.newRow();
				cmpsr.addWidget(indexedPanel);
			}
		});
		setAction(new BindingAction());
	}

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

		fg.getField("parent.name").setReadOnly(true);

		fg.getField("status").addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				final String s = getFieldGroup().getField("status").getText().toLowerCase();
				final boolean closed = "closed".equals(s);
				final IField<?, ?> f = getFieldGroup().getField("dateCancelled");
				f.setVisible(closed);
				f.setRequired(closed);
			}
		});

		fg.getField("persistPymntInfo").addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				relatedOnePanel.getFieldGroup().setEnabled(((CheckboxField<?>) event.getSource()).isChecked());
			}
		});

		return fg;
	}
}