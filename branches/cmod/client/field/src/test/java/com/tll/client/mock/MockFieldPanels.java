package com.tll.client.mock;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.mock.MockFieldGroupProviders.MockFieldGroupProvider;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.common.bind.IBindable;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.common.model.mock.AddressType;
import com.tll.common.model.mock.MockEntityType;

/**
 * MockFieldPanels - Factory methods for obtaining mock {@link FieldPanel}s
 * intended for use in testing.
 * @author jpk
 */
public abstract class MockFieldPanels {

	/**
	 * SimpleFieldPanel
	 * @author jpk
	 * @param <M>
	 */
	static class SimpleFieldPanel<M extends IBindable> extends FieldPanel<FlowPanel, M> {

		final FlowPanel panel = new FlowPanel();

		/**
		 * Constructor
		 */
		public SimpleFieldPanel() {
			super();
			initWidget(panel);
			setRenderer(new IFieldRenderer<FlowPanel>() {

				public void render(FlowPanel widget, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(panel);

					cmpsr.addField(fg.getFieldByName("adrsEmailAddress"));

					cmpsr.newRow();
					cmpsr.addField(fg.getFieldByName("adrsFirstName"));
					cmpsr.addField(fg.getFieldByName("adrsMi"));
					cmpsr.addField(fg.getFieldByName("adrsLastName"));

					cmpsr.newRow();
					cmpsr.addField(fg.getFieldByName("adrsAttn"));
					cmpsr.addField(fg.getFieldByName("adrsCompany"));

					cmpsr.newRow();
					cmpsr.addField(fg.getFieldByName("adrsAddress1"));

					cmpsr.newRow();
					cmpsr.addField(fg.getFieldByName("adrsAddress2"));

					cmpsr.newRow();
					cmpsr.addField(fg.getFieldByName("adrsCity"));
					cmpsr.addField(fg.getFieldByName("adrsProvince"));

					cmpsr.newRow();
					cmpsr.addField(fg.getFieldByName("adrsPostalCode"));
					cmpsr.addField(fg.getFieldByName("adrsCountry"));
				}
			});
		} // SimpleFieldPanel

		@Override
		protected FieldGroup generateFieldGroup() {
			return (new MockFieldGroupProviders.AccountFieldsProvider()).getFieldGroup();
		}

	} // SimpleFieldPanel

	/**
	 * IndexFieldPanel
	 * @author jpk
	 * @param <M>
	 */
	static class IndexFieldPanel<M extends IBindable> extends FieldPanel<FlowPanel, M> {

		@Override
		protected FieldGroup generateFieldGroup() {
			return null;
		}

	}

	/**
	 * AddressesPanel
	 * @author jpk
	 * @param <M> The model type
	 */
	public static final class IndexedFieldPanel<M extends IBindable> extends
			TabbedIndexedFieldPanel<IndexFieldPanel<M>, M> {
		
		/**
		 * Constructor
		 */
		public IndexedFieldPanel() {
			super("Indexes", true, true);
		}

		@Override
		protected String getIndexTypeName() {
			return "Account Address";
		}

		@Override
		protected String getTabLabelText(IndexFieldPanel<M> aap) {
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

		@SuppressWarnings("unchecked")
		@Override
		protected M createPrototypeModel() {
			return (M) AuxDataCache.instance().getEntityPrototype(MockEntityType.ACCOUNT_ADDRESS);
		}

		@Override
		protected IndexFieldPanel<M> createIndexPanel(M indexModel) {
			return new IndexFieldPanel<M>();
		}

	} // IndexedFieldPanel

	/**
	 * MockRootFieldPanel
	 * @author jpk
	 */
	static class MockRootFieldPanel extends FieldPanel<FlowPanel, Model> {

		FlowPanel panel = new FlowPanel();
		
		SimpleFieldPanel<Model> relatedOnePanel;
		
		IndexedFieldPanel<Model> indexedPanel;
		
		/**
		 * Constructor
		 */
		public MockRootFieldPanel() {
			super();
			initWidget(panel);
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

					// third row
					cmpsr.newRow();
					
					// related many (indexed) panel
					cmpsr.addWidget(indexedPanel);

					// related one panel
					cmpsr.addWidget(relatedOnePanel);
				}
			});
		}

		@Override
		protected FieldGroup generateFieldGroup() {
			return MockFieldGroupProvider.INSTANCE.getFieldGroup();
		}
	}
	
	public static MockRootFieldPanel getMockRootFieldPanel() {
		return new MockRootFieldPanel();
	}
}