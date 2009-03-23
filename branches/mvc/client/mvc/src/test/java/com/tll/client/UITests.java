package com.tll.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * UI Tests - GWT module for the sole purpose of verifying the DOM/Style of
 * compiled GWT code.
 */
public final class UITests extends AbstractUITest {

	@Override
	protected String getTestSubjectName() {
		return "client-mvc module";
	}

	@Override
	protected UITestCase[] getTestCases() {
		return new UITestCase[] { new MvcTest() };
	}

	/**
	 * MvcTest - tests the rendering of fields and their value change
	 * event handling for all defined field widget types.
	 * @author jpk
	 */
	static final class MvcTest extends DefaultUITestCase {

		/**
		 * Constructor
		 */
		public MvcTest() {
			super("MVC Test", "Tests the basic MVC operation");
		}

		@Override
		protected Widget getContext() {
			return null;
		}

		@Override
		protected Button[] getTestActions() {
			return null;
		}
	} // MvcTest

}
