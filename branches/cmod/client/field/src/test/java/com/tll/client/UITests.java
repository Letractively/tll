package com.tll.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.tll.client.mock.MockFieldPanels;
import com.tll.client.ui.field.FieldPanel;
import com.tll.common.model.Model;

/**
 * UI Tests - GWT module for the sole purpose of verifying the DOM/Style of
 * compiled GWT code.
 */
public final class UITests extends AbstractUITest {

	@Override
	protected String getTestSubjectName() {
		return "client-field module";
	}

	@Override
	protected UITestCase[] getTestCases() {
		return new UITestCase[] { new ModelFieldBindingTest() };
	}

	/**
	 * ModelFieldBindingTest
	 * @author jpk
	 */
	static final class ModelFieldBindingTest extends UITestCase {

		SimplePanel context;
		FieldPanel<FlowPanel, Model> fp;

		@Override
		public String getName() {
			return "Model/Field binding";
		}

		@Override
		public String getDescription() {
			return "Tests model/field binding with handling a nested model collection.";
		}

		@Override
		public void load() {
			fp = new MockFieldPanels.ComplexFieldPanel();

			context = new SimplePanel();
			context.getElement().getStyle().setProperty("margin", "1em");
			context.getElement().getStyle().setProperty("padding", "1em");
			context.getElement().getStyle().setProperty("border", "1px solid gray");
			context.setWidget(fp);

			RootPanel.get().add(context);
		}

		@Override
		public void unload() {
			if(fp != null) {
				fp.removeFromParent();
				fp= null;
			}
			if(context != null) {
				context.removeFromParent();
				context = null;
			}
		}

	} // ModelFieldBindingTest
}
