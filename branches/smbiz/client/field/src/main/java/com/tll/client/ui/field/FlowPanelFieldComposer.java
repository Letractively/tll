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
 * FlowPanelFieldComposer - Lays out fields in a flow style having the following
 * attributes:
 * <ol>
 * <li>Field labels are placed on top of the field
 * <li>Fields are added horizontally to the canvas
 * <li>New rows are created by calling {@link #newRow()}
 * </ol>
 * @author jpk
 */
public class FlowPanelFieldComposer implements IFieldComposer, HasAlignment {

	/**
	 * Styles - (field.css)
	 * @author jpk
	 */
	static final class Styles {

		/**
		 * Style for wrapping divs containing a field and label.
		 */
		public static final String FIELD_CONTAINER = "fldc";
		
		/**
		 * Style applied to each row of fields.
		 */
		public static final String FIELD_ROW = "frow";
	}

	/**
	 * The root canvas panel for this field canvas implementation.
	 */
	private VerticalPanel vp;

	private HorizontalPanel currentRow;

	private Widget last;

	private boolean atCurrent;

	/**
	 * Constructor
	 */
	public FlowPanelFieldComposer() {
		super();
	}

	public void setCanvas(Panel canvas) {
		// reset state
		if(vp == null) {
			vp = new VerticalPanel();
		}
		else {
			vp.clear();
		}
		currentRow = null;
		last = null;
		atCurrent = false;

		// bind the canvas
		canvas.add(vp);
	}

	private HorizontalPanel getCurrentRow() {
		if(currentRow == null) {
			currentRow = new HorizontalPanel();
			currentRow.setStyleName(Styles.FIELD_ROW);
			vp.add(currentRow);
		}
		return currentRow;
	}

	private void add(FieldLabel fldLbl, Widget w) {
		FlowPanel fp;
		if(!atCurrent) {
			fp = new FlowPanel();
			fp.setStyleName(Styles.FIELD_CONTAINER);
		}
		else {
			if(last == null) throw new IllegalStateException("Empty row");
			fp = (FlowPanel) last.getParent();
		}
		if(fldLbl != null) {
			fp.add(fldLbl);
		}
		else {
			fp.add(new Br()); // this is too much space
		}
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