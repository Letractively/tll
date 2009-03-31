/**
 * The Logic Lab
 * @author jpk
 * Mar 4, 2009
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * FlowFieldPanel - {@link FieldPanel} employing a {@link FlowPanel} as its
 * wrapped widget.
 * @author jpk
 */
public abstract class FlowFieldPanel extends FieldPanel<FlowPanel> {

	/**
	 * The wrapped widget.
	 */
	protected final FlowPanel panel = new FlowPanel();

	/**
	 * Constructor
	 */
	public FlowFieldPanel() {
		super();
		initWidget(panel);
	}

}
