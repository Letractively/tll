/**
 * The Logic Lab
 * @author jpk
 * Mar 4, 2009
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * {@link AbstractBindableFieldPanel} employing a {@link FlowPanel} as its
 * wrapped widget.
 * @author jpk
 */
public abstract class AbstractBindableFlowFieldPanel extends AbstractBindableFieldPanel<FlowPanel> {

	/**
	 * The wrapped widget.
	 */
	protected final FlowPanel panel = new FlowPanel();

	/**
	 * Constructor
	 */
	public AbstractBindableFlowFieldPanel() {
		super();
		initWidget(panel);
	}
}
