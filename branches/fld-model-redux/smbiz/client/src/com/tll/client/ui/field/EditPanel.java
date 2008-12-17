/**
 * The Logic Lab
 * @author jpk Nov 3, 2007
 */
package com.tll.client.ui.field;

import java.util.List;

import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.event.IEditListener;
import com.tll.client.event.ISourcesEditEvents;
import com.tll.client.event.type.EditEvent;
import com.tll.client.event.type.EditEvent.EditOp;
import com.tll.client.field.FieldBindingGroup;
import com.tll.client.field.FieldGroup;
import com.tll.client.model.Model;
import com.tll.client.msg.Msg;
import com.tll.client.ui.CSS;
import com.tll.client.ui.FocusCommand;
import com.tll.client.validate.ValidationException;

/**
 * EditPanel - Composite panel targeting a {@link FlowPanel} whose children
 * consist of a {@link ScrollPanel} containing a {@link FieldPanel} and another
 * {@link FlowPanel} containing edit buttons. The {@link ScrollPanel} enables
 * the the {@link FieldPanel} content to always be navigable and keeps the edit
 * and cancel buttons in constant position.
 * @author jpk
 */
public final class EditPanel extends Composite implements ClickListener, ISourcesEditEvents {

	/**
	 * The style name for {@link EditPanel}s.
	 */
	private static final String STYLE_ENTITY_EDIT = "entityEdit";

	/**
	 * The button row style
	 */
	private static final String STYLE_BTN_ROW = "btnRow";

	/**
	 * The composite's target widget
	 */
	private final FlowPanel panel = new FlowPanel();

	/**
	 * This panel contains the {@link FieldPanel}.
	 */
	private final SimplePanel portal = new SimplePanel();

	/**
	 * Contains the actual edit fields.
	 */
	private final FieldPanel fieldPanel;

	/**
	 * The field bindings.
	 */
	private final FieldBindingGroup bindings;

	/**
	 * The panel containing the edit buttons
	 */
	private final FlowPanel pnlButtonRow = new FlowPanel();

	private final Button btnSave, btnDelete, btnReset, btnCancel;

	private final EditListenerCollection editListeners = new EditListenerCollection();

	/**
	 * Constructor
	 * @param fieldPanel The field group panel
	 * @param showCancelBtn Show the cancel button? Causes a cancel edit event
	 *        when clicked.
	 * @param showDeleteBtn Show the delete button? Causes a delete edit event
	 *        when clicked.
	 */
	public EditPanel(FieldPanel fieldPanel, boolean showCancelBtn, boolean showDeleteBtn) {
		pnlButtonRow.setStyleName(STYLE_BTN_ROW);

		// hide the button row until initialized
		pnlButtonRow.setVisible(false);

		btnSave = new Button("", this);
		pnlButtonRow.add(btnSave);

		btnReset = new Button("Reset", this);
		pnlButtonRow.add(btnReset);

		if(showDeleteBtn) {
			btnDelete = new Button("Delete", this);
			pnlButtonRow.add(btnDelete);
		}
		else {
			btnDelete = null;
		}

		if(showCancelBtn) {
			btnCancel = new Button("Cancel", this);
			pnlButtonRow.add(btnCancel);
		}
		else {
			btnCancel = null;
		}

		portal.setStyleName(CSS.PORTAL);

		panel.add(portal);
		panel.add(pnlButtonRow);
		initWidget(panel);

		setStyleName(STYLE_ENTITY_EDIT);
		this.fieldPanel = fieldPanel;
		portal.setWidget(fieldPanel);

		bindings = new FieldBindingGroup();
	}

	public void addEditListener(IEditListener listener) {
		editListeners.add(listener);
	}

	public void removeEditListener(IEditListener listener) {
		editListeners.remove(listener);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		// portal.addScrollListener(MsgManager.instance);
		if(btnCancel != null) {
			DeferredCommand.addCommand(new FocusCommand(btnCancel, true));
		}
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		// portal.removeScrollListener(MsgManager.instance);
	}

	public void addClickListener(ClickListener listener) {
		if(btnCancel != null) {
			btnCancel.addClickListener(listener);
		}
	}

	public void removeClickListener(ClickListener listener) {
		if(btnCancel != null) {
			btnCancel.removeClickListener(listener);
		}
	}

	/**
	 * Binds the given model to the fields contained in this edit panel.
	 * @param model The model to bind
	 */
	public void bindModel(Model model) {
		// [re]create field bindings
		bindings.clear();
		fieldPanel.createFieldBindings(bindings, model);
	}

	/**
	 * Binds a model to the fields within this edit panel. First, the fields are
	 * validated and if successful, the field data is transferred to the model. If
	 * one or more fields are invalid, no model alteration occurs.
	 * @return <code>true</code> if the model is successfully updated.
	 */
	public boolean updateModel() {
		if(bindings.size() < 1) throw new IllegalStateException("No field bindings exist.");

		// first validate the fields
		try {
			getFields().validate();
		}
		catch(ValidationException e) {
			return false;
		}

		// fields are all valid to send field values to the model
		bindings.pull();

		return true;
	}

	public void applyErrorMsgs(final List<Msg> msgs) {
		getFields().markInvalid(true, msgs);
	}

	public FieldGroup getFields() {
		return fieldPanel.getFieldGroup();
	}

	public void setEditMode(boolean isAdd) {
		btnSave.setText(isAdd ? "Add" : "Update");
		// now show the button row
		pnlButtonRow.setVisible(true);
	}

	private boolean isAdd() {
		return "Add".equals(btnSave.getText());
	}

	public void onClick(Widget sender) {
		if(sender == btnSave) {
			if(!updateModel()) return;
			editListeners.fireEditEvent(new EditEvent(this, isAdd() ? EditOp.ADD : EditOp.UPDATE));
		}
		else if(sender == btnReset) {
			getFields().reset();
		}
		else if(sender == btnDelete) {
			editListeners.fireEditEvent(new EditEvent(this, EditOp.DELETE));
		}
		else if(sender == btnCancel) {
			editListeners.fireEditEvent(new EditEvent(this, EditOp.CANCEL));
		}
	}
}
