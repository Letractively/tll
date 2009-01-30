package com.tll.client.bind;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.tll.client.ClientTestUtils;
import com.tll.client.ClientTestUtils.TestFieldPanel;
import com.tll.client.ui.field.FieldPanel;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class FieldBindingTest extends GWTTestCase {

	/**
	 * Must refer to a valid module that sources this class.
	 */
	@Override
	public String getModuleName() {
		return "com.tll.Test";
	}

	/**
	 * TestModelFieldGroupBindingAction
	 * @author jpk
	 */
	static class TestModelFieldGroupBindingAction extends AbstractModelEditAction<Model, FieldPanel<FlowPanel, Model>> {

		@Override
		protected void populateBinding(FieldPanel<FlowPanel, Model> fp) throws PropertyPathException {

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

			// addIndexedFieldBinding(fp.getModel(), "addresses", addressesPanel);
		}

		public void execute() {
		}

	} // TestModelFieldGroupBindingAction

	/**
	 * Tests the initial binding of model properties to fields.
	 */
	public void testBind() {
		TestFieldPanel fieldPanel = new TestFieldPanel();
		fieldPanel.setAction(new TestModelFieldGroupBindingAction());
		fieldPanel.setModel(ClientTestUtils.getTestRootModel());
		RootPanel.get().add(fieldPanel);
	}
}
