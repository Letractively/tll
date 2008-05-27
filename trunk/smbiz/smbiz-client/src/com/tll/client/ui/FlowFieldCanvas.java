/**
 * The Logic Lab
 * @author jpk
 * May 24, 2008
 */
package com.tll.client.ui;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.field.AbstractField;
import com.tll.client.ui.field.FieldLabel;
import com.tll.client.ui.field.IFieldCanvas;

/**
 * FlowFieldCanvas - Lays out fields in a flow style having the following
 * attributes:
 * <ol>
 * <li>Field labels are placed on top of the field
 * <li>Fields are added horizontally to the canvas
 * <li>New rows are created by calling {@link #newRow()}
 * </ol>
 * @author jpk
 */
public class FlowFieldCanvas implements IFieldCanvas, HasAlignment {

	private static final String CSS_FIELD = "fld";

	/**
	 * The root canvas panel for this field canvas implementation.
	 */
	private final VerticalPanel vp = new VerticalPanel();

	private HorizontalPanel currentRow;

	private Widget last;

	private boolean atCurrent;

	/**
	 * Constructor
	 * @param parentPanel The panel that will be appended with this canvas
	 */
	public FlowFieldCanvas(Panel parentPanel) {
		super();
		parentPanel.add(vp);
	}

	private HorizontalPanel getCurrentRow() {
		if(currentRow == null) {
			currentRow = new HorizontalPanel();
			vp.add(currentRow);
		}
		return currentRow;
	}

	private void add(FieldLabel fldLbl, Widget w) {
		FlowPanel fp;
		if(!atCurrent) {
			fp = new FlowPanel();
			fp.setStyleName(CSS_FIELD);
		}
		else {
			if(last == null) throw new IllegalStateException("Empty row");
			fp = (FlowPanel) last.getParent();
			fp.add(new Br());
		}
		if(fldLbl != null) fp.add(fldLbl);
		fp.add(w);
		getCurrentRow().add(fp);
		last = w;
	}

	/**
	 * Adds a new.
	 * @param w The non-field Widget to add
	 */
	public void addWidget(Widget w) {
		add(null, w);
	}

	/**
	 * Adds a field label and Widget to the current row creating a new slot.
	 * @param label The label text
	 * @param w The widget
	 */
	public void addWidget(String label, Widget w) {
		add(label == null ? null : new FieldLabel(label), w);
	}

	/**
	 * Adds a field to the current row creating a new slot.
	 * @param field The field to add
	 */
	public void addField(AbstractField field) {
		add(field.getFieldLabel(), field);
		field.setFieldParent(last.getParent());
		field.setFieldLabelParent(last.getParent());
	}

	/**
	 * Forces a new row to be created before the next field or Widget is added.
	 */
	public void newRow() {
		// this will cause a new row the next time addField is called
		currentRow = null;
	}

	/**
	 * Forces subsequently added fields/widgets to be added at the same "slot" at
	 * the last added field/widget.
	 */
	public void stopFlow() {
		atCurrent = true;
	}

	/**
	 * Re-establishes the flow so subsequently added fields/widgets will have a
	 * newly created "slot".
	 */
	public void resetFlow() {
		atCurrent = false;
	}

	/**
	 * Resets the alignment for subsequently added Widgets/fields to their initial
	 * values.
	 */
	public void resetAlignment() {
		setHorizontalAlignment(ALIGN_DEFAULT);
		setVerticalAlignment(ALIGN_TOP);
	}

	public HorizontalAlignmentConstant getHorizontalAlignment() {
		return getCurrentRow().getHorizontalAlignment();
	}

	public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
		getCurrentRow().setHorizontalAlignment(align);
	}

	public VerticalAlignmentConstant getVerticalAlignment() {
		return getCurrentRow().getVerticalAlignment();
	}

	public void setVerticalAlignment(VerticalAlignmentConstant align) {
		getCurrentRow().setVerticalAlignment(align);
	}

	/*
	private Element getFieldCell(AbstractField field) {
		return field.getParent().getElement().getParentElement();
	}

	public void hideField(AbstractField field) {
		getFieldCell(field).getStyle().setProperty("display", "none");
	}

	public void showField(AbstractField field) {
		getFieldCell(field).getStyle().setProperty("display", "");
	}
	*/
}
