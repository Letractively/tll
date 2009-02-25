package com.tll.client.mock;

import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.ui.field.FieldPanel;
import com.tll.common.model.Model;

/**
 * MockFieldPanel - Simplified {@link FieldPanel} for testing purposes.
 * @author jpk
 */
abstract class MockFieldPanel extends FieldPanel<FlowPanel, Model> {
	
	protected final FlowPanel panel = new FlowPanel();

	/**
	 * Constructor
	 */
	public MockFieldPanel() {
		super();
		initWidget(panel);
	}
}