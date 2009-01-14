/**
 * The Logic Lab
 * @author jpk
 * Jan 14, 2009
 */
package com.tll.client.ui;

import com.google.gwt.gen2.table.override.client.Panel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * WidgetAndLabel - A composite {@link Widget} containing a {@link Widget} and
 * label text to the right of it.
 * @author jpk
 */
public class WidgetAndLabel extends Composite {

	/**
	 * Styles - The defined styles for a {@link WidgetAndLabel} widget.
	 * @author jpk
	 */
	public interface Styles {

		/**
		 * The general widget and label style that is ascribed to the {@link Panel}
		 * containing the {@link Widget} and {@link Label}.
		 */
		static final String STYLE_WAL = "wal";

	} // Styles

	/**
	 * Contains the {@link Widget} and {@link Label}.
	 */
	private final FlowPanel pnl = new FlowPanel();

	/**
	 * The {@link Widget}.
	 */
	private Widget theWidget;

	/**
	 * The {@link Label}.
	 */
	private final Label label = new Label();

	/**
	 * Constructor
	 * @param widget
	 * @param labelText
	 */
	public WidgetAndLabel(Widget widget, String labelText) {
		super();
		initWidget(pnl);
		setTheWidget(widget);
		setLabelText(labelText);
	}

	/**
	 * @param theWidget the widget to set
	 */
	public void setTheWidget(Widget theWidget) {
		this.theWidget = theWidget;
	}

	/**
	 * @param labelText the label text to set
	 */
	public void setLabelText(String labelText) {
		label.setText(labelText);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if(pnl.getWidgetCount() == 0) {
			pnl.add(theWidget);
			pnl.add(label);
		}
	}
}
