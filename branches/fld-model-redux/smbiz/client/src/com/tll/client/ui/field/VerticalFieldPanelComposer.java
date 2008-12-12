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
 * VerticalFieldPanelComposer - Lays out fields in a vertical style having the
 * following attributes:
 * <ol>
 * <li>Only one field exists on a row
 * <li>Field labels are placed to the left of the fields
 * </ol>
 * @author jpk
 */
public class VerticalFieldPanelComposer implements IFieldPanelComposer {

	/**
	 * The root canvas panel for this field canvas implementation.
	 */
	private final Grid grid = new Grid(0, 2);

	private int rowIndex = -1;

	public void setCanvas(Panel canvas) {
		canvas.add(grid);
	}

	private void add(FieldLabel fldLbl, Widget w) {
		grid.resizeRows(++rowIndex + 1);
		if(fldLbl != null) grid.setWidget(rowIndex, 0, fldLbl);
		grid.setWidget(rowIndex, 1, w);
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
	 * {@link #addField(AbstractField)} should be called instead.
	 * @param label The label text
	 * @param w The non-IField and non-FieldGroupPanel Widget to add
	 */
	public void addWidget(String label, Widget w) {
		add(label == null ? null : new FieldLabel(label), w);
	}

	/**
	 * Adds a field to the canvas. The field label is extracted from the given
	 * field and if non-<code>null</code>, is added as well.
	 * @param field The field to add
	 */
	public void addField(AbstractField field) {
		add(field.getFieldLabel(), field);
	}
}
