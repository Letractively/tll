package com.tll.client.ui;

import java.util.Collection;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * GridRenderer
 * @author jpk
 */
public final class GridRenderer implements IWidgetRenderer {

	private final int numCols;

	/**
	 * Constructor
	 * @param numCols
	 */
	public GridRenderer(int numCols) {
		super();
		this.numCols = numCols;
	}

	@Override
	public Panel render(Collection<? extends Widget> buttons) {
		final int numRows = (int) Math.ceil((double) buttons.size() / (double) numCols);
		final Grid panel = new Grid(numRows, numCols);
		int row, col;
		row = col = 0;
		for(final Widget rb : buttons) {
			if(col == numCols) {
				// new row
				row++;
				col = 0;
			}
			panel.setWidget(row, col++, rb);
		}
		return panel;
	}
}