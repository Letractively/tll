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
public final class UIFieldTests extends AbstractUITest {

	@Override
	protected String getTestSubjectName() {
		return "client-field module";
	}

	@Override
	protected UITestCase[] getTestCases() {
		return new UITestCase[] { new FieldPanelTest() };
	}

	/**
	 * FieldPanelTest
	 * @author jpk
	 */
	static final class FieldPanelTest extends UITestCase {

		SimplePanel context;
		FieldPanel<FlowPanel, Model> fp;

		@Override
		public String getName() {
			return "FieldPanel";
		}

		@Override
		public String getDescription() {
			return "Displays all available field widgets in a simple mocked FieldPanel.";
		}

		@Override
		public void load() {
			fp = MockFieldPanels.getMockRootFieldPanel();

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

	} // FieldPanelTest
}
