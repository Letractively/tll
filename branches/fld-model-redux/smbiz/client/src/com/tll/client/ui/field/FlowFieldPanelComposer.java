/**
 * The Logic Lab
 * @author jpk
 * May 24, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.Br;

/**
 * FlowFieldPanelComposer - Lays out fields in a flow style having the following
 * attributes:
 * <ol>
 * <li>Field labels are placed on top of the field
 * <li>Fields are added horizontally to the canvas
 * <li>New rows are created by calling {@link #newRow()}
 * </ol>
 * @author jpk
 */
public class FlowFieldPanelComposer implements IFieldPanelComposer, HasAlignment {

	/**
	 * The root canvas panel for this field canvas implementation.
	 */
	private final VerticalPanel vp = new VerticalPanel();

	private HorizontalPanel currentRow;

	private Widget last;

	private boolean atCurrent;

	/**
	 * Constructor
	 */
	public FlowFieldPanelComposer() {
		super();
		vp.setStyleName(IField.STYLE_FIELD);
	}

	public void setCanvas(Panel canvas) {
		canvas.add(vp);
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
			// fp.setStyleName(IField.STYLE_FIELD);
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
	 * Adds a field label and Widget to the canvas. If the label text is
	 * <code>null</code>, no label is added. If the Widget is an IField
	 * {@link #addField(IField)} should be called instead.
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
	public void addField(IField field) {
		add(field.getFieldLabel(), field.getWidget());
		field.setFieldContainer(last.getParent());
		field.setFieldLabelContainer(last.getParent());
	}

	/**
	 * Forces a new row to be created before the next field or Widget is added.
	 * Also, the flow and alignment state is reset.
	 */
	public void newRow() {
		// this will cause a new row the next time addField is called
		currentRow = null;
		reset();
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

	/**
	 * Resets both the flow and alignment.
	 */
	public void reset() {
		resetFlow();
		resetAlignment();
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
}
