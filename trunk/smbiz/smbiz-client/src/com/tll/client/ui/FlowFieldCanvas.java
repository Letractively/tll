/**
 * The Logic Lab
 * @author jpk
 * May 24, 2008
 */
package com.tll.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
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
public class FlowFieldCanvas implements IFieldCanvas {

	private final VerticalPanel vp = new VerticalPanel();
	private HorizontalPanel currentRow;
	private Widget last;

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

	private void add(FieldLabel fldLbl, Widget w, boolean atCurrent) {
		FlowPanel fp;
		if(!atCurrent) {
			fp = new FlowPanel();
			fp.setStyleName(AbstractField.CSS_FIELD);
		}
		else {
			if(last == null) throw new IllegalStateException("Empty row");
			fp = (FlowPanel) last.getParent();
			fp.add(new Br());
		}
		if(fldLbl != null) fp.add(fldLbl);
		fp.add(w);
		// addWidget(fp);
		getCurrentRow().add(fp);
		last = w;
	}

	/**
	 * Adds a new.
	 * @param w The non-field Widget to add
	 */
	public void addWidget(Widget w) {
		add(null, w, false);
	}

	/**
	 * Adds a field label and Widget to the current row creating a new slot.
	 * @param label The label text
	 * @param w The widget
	 */
	public void addWidget(String label, Widget w) {
		add(label == null ? null : new FieldLabel(label), w, false);
	}

	/**
	 * Adds a field to the current row creating a new slot.
	 * @param field The field to add
	 */
	public void addField(AbstractField field) {
		add(field.getFieldLabel(), field, false);
	}

	/**
	 * Adds a field beneath the last added field.
	 * @param field The field to add
	 */
	public void addFieldAtCurrent(AbstractField field) {
		add(field.getFieldLabel(), field, true);
	}

	/**
	 * Adds a Widget beneath the last added field.
	 * @param w The Widget to add
	 */
	public void addWidgetAtCurrent(Widget w) {
		add(null, w, true);
	}

	/**
	 * Adds a Widget beneath the last added field along with a field label.
	 * @param label The label text
	 * @param w The Widget to add
	 */
	public void addWidgetAtCurrent(String label, Widget w) {
		add(label == null ? null : new FieldLabel(label), w, true);
	}

	/**
	 * Forces a new row to be created before the next field or Widget is added.
	 */
	public void newRow() {
		// this will cause a new row the next time addField is called
		currentRow = null;
	}

	private Element getFieldCell(AbstractField field) {
		return field.getParent().getElement().getParentElement();
	}

	public void hideField(AbstractField field) {
		getFieldCell(field).getStyle().setProperty("display", "none");
	}

	public void showField(AbstractField field) {
		getFieldCell(field).getStyle().setProperty("display", "");
	}
}
