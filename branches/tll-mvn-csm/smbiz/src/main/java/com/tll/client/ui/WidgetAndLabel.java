/**
 * The Logic Lab
 * @author jpk
 * Jan 14, 2009
 */
package com.tll.client.ui;

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
	 * Styles - (widget-tll.css)
	 * @author jpk
	 */
	protected static class Styles {

		/**
		 * Style applied to the {@link WidgetAndLabel} widget.
		 */
		static final String WAL = "wal";

		/**
		 * Style applied to the child widget.
		 */
		static final String WIDGET = "widget";

		/**
		 * Style applied to the child label.
		 */
		static final String LABEL = "label";

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
		pnl.setStyleName(Styles.WAL);
		label.setStyleName(Styles.LABEL);
		initWidget(pnl);
		setTheWidget(widget);
		setLabelText(labelText);
	}

	/**
	 * @return the theWidget
	 */
	public Widget getTheWidget() {
		return theWidget;
	}

	/**
	 * @return the label
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * @param theWidget the widget to set
	 */
	public void setTheWidget(Widget theWidget) {
		this.theWidget = theWidget;
		theWidget.addStyleName(Styles.WIDGET);
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

	@Override
	protected void onUnload() {
		super.onUnload();
		theWidget.removeStyleName(Styles.WIDGET);
	}
}
