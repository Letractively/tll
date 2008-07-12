/**
 * The Logic Lab
 * @author jpk
 * Jun 8, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.field.FieldGroup;

/**
 * DeleteTabWidget - Tab Widget used for displaying a related many entity in a
 * TabPanel with a delete/un-delete ToggleButton to the right of the tab text.
 * @author jpk
 */
public class DeleteTabWidget extends FlowPanel implements ClickListener {

	private final Label label;
	private final ToggleButton btnDeleteToggle;
	private FieldGroup fieldGroup;
	/**
	 * If specified, it is presumed that model ref property is existing.
	 */
	private String modelRefPropertyPath;

	/**
	 * Constructor
	 * @param text The label text. May not be <code>null</code>.
	 */
	public DeleteTabWidget(String text) {
		super();
		if(text == null) throw new IllegalArgumentException();
		this.label = new Label(text);
		// delete img btn
		final Image imgDelete = App.imgs().delete().createImage();
		imgDelete.setTitle("Delete " + text);
		final Image imgUndo = App.imgs().undo().createImage();
		imgUndo.setTitle("Un-delete " + text);
		btnDeleteToggle = new ToggleButton(imgDelete, imgUndo, this);
		btnDeleteToggle.addStyleName("btnDeleteToggle");
		add(label);
		add(btnDeleteToggle);
	}

	/**
	 * Constructor
	 * @param text
	 * @param fieldGroup The bound FieldGroup that is displayed in the associated
	 *        tab Widget. May be <code>null</code> in which case, the
	 *        delete/un-delete toggle is <em>not</em> displayed.
	 * @param modelRefPropertyPath The model ref property path to be specified
	 *        <em>only</em> for existing model ref proeprties.
	 */
	public DeleteTabWidget(String text, FieldGroup fieldGroup, String modelRefPropertyPath) {
		this(text);
		setFieldGroup(fieldGroup, modelRefPropertyPath);
	}

	private void toggle(boolean markDeleted) {
		assert fieldGroup != null;
		if(modelRefPropertyPath == null) {
			fieldGroup.setUpdateModel(!markDeleted);
		}
		else {
			if(markDeleted) {
				fieldGroup.addPendingDelete(modelRefPropertyPath);
			}
			else {
				fieldGroup.removePendingDelete(modelRefPropertyPath);
			}
		}
		fieldGroup.setEnabled(!markDeleted);
	}

	public void onClick(Widget sender) {
		if(fieldGroup != null) toggle(btnDeleteToggle.isDown());
	}

	public void setFieldGroup(FieldGroup fieldGroup, String modelRefPropertyPath) {
		// show or hide the toggle button
		if(this.fieldGroup != null && this.modelRefPropertyPath != null
				&& !this.modelRefPropertyPath.equals(modelRefPropertyPath)) {
			this.fieldGroup.removePendingDelete(this.modelRefPropertyPath);
		}
		this.fieldGroup = fieldGroup;
		this.modelRefPropertyPath = modelRefPropertyPath;
		getWidget(1).setVisible(fieldGroup != null);
		if(fieldGroup != null) {
			boolean deleted =
					modelRefPropertyPath == null ? !fieldGroup.isUpdateModel() : fieldGroup.isPendingDelete(modelRefPropertyPath);
			toggle(deleted);
		}
	}

}
