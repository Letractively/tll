/**
 * The Logic Lab
 * @author jpk
 * May 24, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * GridFieldComposer - Lays out fields in a vertical style having the following
 * attributes:
 * <ol>
 * <li>Only one field exists on a row
 * <li>Field labels are placed to the left of the fields
 * </ol>
 * @author jpk
 */
public class GridFieldComposer implements IFieldComposer {

	/**
	 * Styles - (field.css)
	 * @author jpk
	 */
	static final class Styles {

		/**
		 * Style applied to the grid containing the fields.
		 */
		public static final String FIELD_GRID = "fgrid";
		
		public static final String CELL_LABEL = "cell-lbl";

		public static final String CELL_FIELD = "cell-fld";
	}

	/**
	 * The root canvas panel for this field canvas implementation.
	 */
	private Grid grid;

	private int rowIndex = -1;

	/**
	 * Constructor
	 */
	public GridFieldComposer() {
		super();
	}

	public void setCanvas(Panel canvas) {
		// clear state
		if(grid == null) {
			grid = new Grid(0, 2);
			grid.addStyleName(Styles.FIELD_GRID);
		}
		else {
			grid.clear();
		}
		rowIndex = -1;

		// bind
		canvas.add(grid);
	}

	private void add(FieldLabel fldLbl, Widget w) {
		grid.resizeRows(++rowIndex + 1);
		if(fldLbl != null) {
			grid.getCellFormatter().setStyleName(rowIndex, 0, Styles.CELL_LABEL);
			grid.setWidget(rowIndex, 0, fldLbl);
		}
		grid.setWidget(rowIndex, 1, w);
		grid.getCellFormatter().setStyleName(rowIndex, 1, Styles.CELL_FIELD);
	}

	/**
	 * Adds a non-field Widget row.
	 * @param w The non-field Widget to add
	 */
	public void addWidget(Widget w) {
		add(null, w);
	}

	/**
	 * Adds a field label and Widget row. If the label text is <code>null</code>,
	 * no label is added. If the Widget is an IField
	 * {@link #addField(IFieldWidget)} should be called instead.
	 * @param label The label text
	 * @param w The non-IField and non-FieldPanel Widget to add
	 */
	public void addWidget(String label, Widget w) {
		add(label == null ? null : new FieldLabel(label), w);
	}

	/**
	 * Adds a field to the canvas. The field label is extracted from the given
	 * field and if non-<code>null</code>, is added as well.
	 * @param field The field to add
	 */
	public void addField(IFieldWidget<?> field) {
		add(field.getFieldLabel(), field.getWidget());
	}
}
