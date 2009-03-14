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
		return "client-listing module";
	}

	@Override
	protected UITestCase[] getTestCases() {
		return new UITestCase[] { new ListingWidgetTest() };
	}

	static final class ListingWidgetTest extends DefaultUITestCase {
		
		//ListingFactory.

		/**
		 * Constructor
		 */
		public ListingWidgetTest() {
			super("Listing Test", "Tests the core listing widget functionality");
		}

		@Override
		protected Widget getContext() {
			return null;
		}

		@Override
		protected Button[] getTestActions() {
			return null;
		}
	}
}
