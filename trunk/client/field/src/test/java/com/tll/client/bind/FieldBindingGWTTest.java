package com.tll.client.bind;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.RootPanel;
import com.tll.client.test.ComplexFieldPanel;
import com.tll.common.model.test.MockModelStubber;

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
		final ComplexFieldPanel fieldPanel = new ComplexFieldPanel();
		//fieldPanel.setAction(new SimpleBindingAction());
		fieldPanel.setModel(MockModelStubber.stubAccount(true));
		RootPanel.get().add(fieldPanel);
	}
}
