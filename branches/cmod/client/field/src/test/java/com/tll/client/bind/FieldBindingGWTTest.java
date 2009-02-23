package com.tll.client.bind;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.tll.client.mock.MockBinding;
import com.tll.client.mock.MockFieldPanels;
import com.tll.client.ui.field.FieldPanel;
import com.tll.common.model.Model;
import com.tll.common.model.mock.MockModelStubber;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class FieldBindingGWTTest extends GWTTestCase {

	/**
	 * Must refer to a valid module that sources this class.
	 */
	@Override
	public String getModuleName() {
		return "com.tll.FieldTest";
	}

	/**
	 * Tests the initial binding of model properties to fields.
	 */
	public void testBind() {
		final FieldPanel<FlowPanel, Model> fieldPanel = new MockFieldPanels.ComplexFieldPanel();
		fieldPanel.setAction(new MockBinding());
		fieldPanel.setModel(MockModelStubber.stubAccount());
		RootPanel.get().add(fieldPanel);
	}
}
