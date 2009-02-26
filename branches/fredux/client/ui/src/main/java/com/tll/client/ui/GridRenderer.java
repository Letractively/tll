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

	private final int numRows, numCols;

	/**
	 * Constructor
	 * @param numRows
	 * @param numCols
	 */
	public GridRenderer(int numRows, int numCols) {
		super();
		this.numRows = numRows;
		this.numCols = numCols;
	}

	@Override
	public Panel render(Collection<? extends Widget> buttons) {
		final Grid panel = new Grid(numRows, numCols);
		int row, col;
		row = col = 0;
		for(final Widget rb : buttons) {
			panel.setWidget(row, col++, rb);
			if(col == numCols) {
				// new row
				row++;
				col = 0;
			}
			panel.setWidget(row, col, rb);
		}
		return panel;
	}
}