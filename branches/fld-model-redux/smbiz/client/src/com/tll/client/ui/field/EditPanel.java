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
import com.tll.client.Style;
import com.tll.client.model.Model;
import com.tll.client.msg.Msg;
import com.tll.client.ui.EditEvent;
import com.tll.client.ui.FocusCommand;
import com.tll.client.ui.IEditListener;
import com.tll.client.ui.ISourcesEditEvents;
import com.tll.client.ui.EditEvent.EditOp;
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
	 * The panel containing the edit buttons
	 */
	private final FlowPanel pnlButtonRow = new FlowPanel();

	private final Button btnSave, btnDelete, btnReset, btnCancel;

	private final EditListenerCollection editListeners = new EditListenerCollection();

	/**
	 * Constructor
	 * @param fieldPanel The required {@link FieldPanel}
	 * @param showCancelBtn Show the cancel button? Causes a cancel edit event
	 *        when clicked.
	 * @param showDeleteBtn Show the delete button? Causes a delete edit event
	 *        when clicked.
	 */
	public EditPanel(FieldPanel fieldPanel, boolean showCancelBtn, boolean showDeleteBtn) {

		if(fieldPanel == null) throw new IllegalArgumentException("A field panel must be specified.");
		this.fieldPanel = fieldPanel;

		portal.setStyleName(Style.PORTAL);
		portal.setWidget(fieldPanel);

		pnlButtonRow.setStyleName(STYLE_BTN_ROW);

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

		// hide the button row until initialized
		pnlButtonRow.setVisible(false);

		panel.add(portal);
		panel.add(pnlButtonRow);
		panel.setStyleName(STYLE_ENTITY_EDIT);

		initWidget(panel);
	}

	public void addEditListener(IEditListener listener) {
		editListeners.add(listener);
	}

	public void removeEditListener(IEditListener listener) {
		editListeners.remove(listener);
	}

	private void setEditMode(boolean isAdd) {
		btnSave.setText(isAdd ? "Add" : "Update");
		// now show the button row
		pnlButtonRow.setVisible(true);
	}

	private boolean isAdd() {
		return "Add".equals(btnSave.getText());
	}

	public void draw() {
		fieldPanel.draw();
	}

	/**
	 * Binds the given model to the fields contained in this edit panel then
	 * transfers the model data to the fields.
	 * @param model The model to bind
	 */
	public void setModel(Model model) {
		// NOTE: we do this binding init stuff here as we need to wait to ensure the
		// aux data set the root field group in the binding
		binding.setRootFieldGroup(fieldPanel.getFieldGroup());

		// have the *root* field panel listen to binding events
		binding.addFieldBindingEventListener(fieldPanel);

		binding.setRootModel(model);
		binding.bind();
		binding.setFieldValues();
		setEditMode(model.isNew());
	}

	/**
	 * Applies error messages to the fields contained in the member
	 * {@link FieldPanel}.
	 * @param msgs The error messages to apply
	 */
	public void applyErrorMsgs(final List<Msg> msgs) {
		fieldPanel.getFieldGroup().markInvalid(true, msgs);
	}

	/**
	 * Validates the fields and if successful, the field data is transferred to
	 * the underlying model.
	 * @return <code>true</code> if the model was successfully updated,
	 *         <code>false</code> otherwise.
	 */
	private boolean updateModel() {

		// validate the fields
		try {
			fieldPanel.getFieldGroup().validate();
		}
		catch(ValidationException e) {
			return false;
		}

		// fields are all valid to send field values to the model
		binding.setModelValues();

		return true;
	}

	public void onClick(Widget sender) {
		if(sender == btnSave) {
			if(!updateModel()) return;
			editListeners.fireEditEvent(new EditEvent(this, isAdd() ? EditOp.ADD : EditOp.UPDATE));
		}
		else if(sender == btnReset) {
			fieldPanel.getFieldGroup().reset();
		}
		else if(sender == btnDelete) {
			editListeners.fireEditEvent(new EditEvent(this, EditOp.DELETE));
		}
		else if(sender == btnCancel) {
			editListeners.fireEditEvent(new EditEvent(this, EditOp.CANCEL));
		}
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

		// TODO should this be here ?
		binding.unbind();
	}
}
