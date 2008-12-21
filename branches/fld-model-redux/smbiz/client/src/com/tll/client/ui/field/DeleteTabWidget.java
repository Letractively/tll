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
		configure(field, binding, modelRefPropertyPath);
	}

	/**
	 * Sets this Widget to be fully functional.
	 * @param field
	 * @param binding
	 * @param modelRefPropertyPath
	 */
	private void configure(IField field, IFieldGroupModelBinding binding, String modelRefPropertyPath) {
		if(field == null || binding == null || modelRefPropertyPath == null) {
			throw new IllegalArgumentException("One or more init args is null.");
		}
		this.field = field;
		this.binding = binding;
		this.modelRefPropertyPath = modelRefPropertyPath;
		toggle(binding.isMarkedDeleted(modelRefPropertyPath));
	}

	private void toggle(boolean markDeleted) {
		binding.markDeleted(modelRefPropertyPath, markDeleted);
	}

	public void onClick(Widget sender) {
		if(field != null) toggle(btnDeleteToggle.isDown());
	}
}
