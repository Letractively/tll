package com.tll.client.bind;

import com.google.gwt.junit.client.GWTTestCase;
import com.tll.client.ClientTestUtils;
import com.tll.client.model.Model;
import com.tll.client.ui.field.FieldBindingHelper;
import com.tll.client.ui.field.FieldGroup;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class ModelFieldGroupBindingTest extends GWTTestCase {

	/**
	 * Must refer to a valid module that sources this class.
	 */
	@Override
	public String getModuleName() {
		return "com.tll.Test";
	}

	/**
	 * AbstractTestModelFieldGroupBindingAction
	 * @author jpk
	 */
	static abstract class AbstractTestModelFieldGroupBindingAction implements IBindingAction<FieldGroup> {

		Binding binding = new Binding();
		Model model = ClientTestUtils.getTestRootModel();
		FieldGroup fg = ClientTestUtils.getRootFieldGroupProvider().getFieldGroup();

		public void setBindable(FieldGroup bindable) {
			FieldBindingHelper.addBinding(binding, model, fg, Model.NAME_PROPERTY);
			FieldBindingHelper.addBinding(binding, model, fg, Model.DATE_CREATED_PROPERTY);
			FieldBindingHelper.addBinding(binding, model, fg, Model.DATE_MODIFIED_PROPERTY);
			FieldBindingHelper.addBinding(binding, model, fg, "parent.name");
			FieldBindingHelper.addBinding(binding, model, fg, "status");
			FieldBindingHelper.addBinding(binding, model, fg, "dateCancelled");
			FieldBindingHelper.addBinding(binding, model, fg, "currency.id");
			FieldBindingHelper.addBinding(binding, model, fg, "billingModel");
			FieldBindingHelper.addBinding(binding, model, fg, "billingCycle");
			FieldBindingHelper.addBinding(binding, model, fg, "dateLastCharged");
			FieldBindingHelper.addBinding(binding, model, fg, "nextChargeDate");
			FieldBindingHelper.addBinding(binding, model, fg, "persistPymntInfo");
			FieldBindingHelper.addBinding(binding, model, fg, "parent");
			FieldBindingHelper.addBinding(binding, model, fg, "addresses");
		}

		public void bind() {
			binding.bind();
		}

		public void unbind() {
			binding.unbind();
			binding.getChildren().clear();
		}
	}

	/**
	 * Tests the setting of non-relational fields from a model.
	 */
	public void testNonRelationalModelToFieldGroupBinding() {
		IBindingAction<FieldGroup> ba = new AbstractTestModelFieldGroupBindingAction() {

			public void execute() {
				// populate the fields
				binding.setRight();
			}

			@Override
			public void setBindable(FieldGroup bindable) {
				super.setBindable(bindable);
			}

		};

		ba.execute();
	}
}
