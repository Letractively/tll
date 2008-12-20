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
import com.tll.client.field.IField;
import com.tll.client.field.IFieldGroupModelBinding;

/**
 * DeleteTabWidget - Tab Widget used for displaying a related many entity in a
 * TabPanel with a delete/un-delete ToggleButton to the right of the tab text.
 * @author jpk
 */
public class DeleteTabWidget extends FlowPanel implements ClickListener {

	private final Label label;

	private final ToggleButton btnDeleteToggle;

	/**
	 * The field/model binding.
	 */
	private IFieldGroupModelBinding binding;

	/**
	 * Points to the target model ref property in the binding relative to the root
	 * model.
	 */
	private String modelRefPropertyPath;

	/**
	 * The subject field (usually a group).
	 */
	private IField field;

	private boolean initialized;

	/**
	 * Constructor
	 * @param name The name describing the thing subject to delete toggling
	 */
	public DeleteTabWidget(String name) {
		super();
		if(name == null) throw new IllegalArgumentException();

		this.label = new Label(name);

		// delete img btn
		final Image imgDelete = App.imgs().delete().createImage();
		imgDelete.setTitle("Delete " + name);
		final Image imgUndo = App.imgs().undo().createImage();
		imgUndo.setTitle("Un-delete " + name);
		btnDeleteToggle = new ToggleButton(imgDelete, imgUndo, this);
		btnDeleteToggle.addStyleName("btnDeleteToggle");
		add(label);
		add(btnDeleteToggle);
	}

	/**
	 * Constructor
	 * @param name
	 * @param field
	 * @param binding
	 * @param modelRefPropertyPath
	 */
	public DeleteTabWidget(String name, IField field, IFieldGroupModelBinding binding, String modelRefPropertyPath) {
		this(name);
		init(field, binding, modelRefPropertyPath);
	}

	public void init(IField field, IFieldGroupModelBinding binding, String modelRefPropertyPath) {
		this.field = field;
		this.binding = binding;
		this.modelRefPropertyPath = modelRefPropertyPath;

		/*
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
		*/
	}

	private void toggle(boolean markDeleted) {
		assert initialized == true;
		if(markDeleted) {
		}
		else {
		}
		field.setEnabled(!markDeleted);
	}

	public void onClick(Widget sender) {
		if(field != null) toggle(btnDeleteToggle.isDown());
	}
}
