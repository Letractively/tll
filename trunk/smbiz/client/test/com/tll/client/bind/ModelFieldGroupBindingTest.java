package com.tll.client.bind;

import com.google.gwt.junit.client.GWTTestCase;
import com.tll.client.ClientTestUtils;
import com.tll.client.model.Model;
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
			binding.getChildren().add(new Binding(model, fg, Model.NAME_PROPERTY));
			binding.getChildren().add(new Binding(model, fg, Model.DATE_CREATED_PROPERTY));
			binding.getChildren().add(new Binding(model, fg, Model.DATE_MODIFIED_PROPERTY));
			binding.getChildren().add(new Binding(model, fg, "parent.name"));
			binding.getChildren().add(new Binding(model, fg, "status"));
			binding.getChildren().add(new Binding(model, fg, "dateCancelled"));
			binding.getChildren().add(new Binding(model, fg, "currency.id"));
			binding.getChildren().add(new Binding(model, fg, "billingModel"));
			binding.getChildren().add(new Binding(model, fg, "billingCycle"));
			binding.getChildren().add(new Binding(model, fg, "dateLastCharged"));
			binding.getChildren().add(new Binding(model, fg, "nextChargeDate"));
			binding.getChildren().add(new Binding(model, fg, "persistPymntInfo"));
			binding.getChildren().add(new Binding(model, fg, "parent.id"));
			// binding.getChildren().add(new Binding(model, fg, "addresses"));
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
