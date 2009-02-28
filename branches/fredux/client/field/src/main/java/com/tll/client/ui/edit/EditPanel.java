/**
 * The Logic Lab
 * @author jpk Nov 3, 2007
 */
package com.tll.client.ui.edit;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.IModelFieldBinding;
import com.tll.client.ui.FocusCommand;
import com.tll.client.ui.edit.EditEvent.EditOp;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.common.model.Model;
import com.tll.common.msg.Msg;

/**
 * EditPanel - Composite panel targeting a {@link FlowPanel} whose children
 * consist of a {@link ScrollPanel} containing a {@link FieldPanel} and another
 * {@link FlowPanel} containing edit buttons. The {@link ScrollPanel} enables
 * the the {@link FieldPanel} content to always be navigable and keeps the edit
 * and cancel buttons in constant position.
 * @author jpk
 */
public final class EditPanel extends Composite implements ClickHandler, ISourcesEditEvents {

	/**
	 * Styles - (admin.css)
	 * @author jpk
	 */
	protected static class Styles {

		/**
		 * The style name for {@link EditPanel}s.
		 */
		public static final String ENTITY_EDIT = "entityEdit";
		/**
		 * The button row style.
		 */
		public static final String BTN_ROW = "btnRow";
		/**
		 * The edit portal style.
		 */
		public static final String PORTAL = "portal";
	}

	/**
	 * The composite's target widget
	 */
	private final FlowPanel panel = new FlowPanel();

	/**
	 * This panel contains the {@link FieldPanel}.
	 */
	private final SimplePanel portal = new SimplePanel();

	/**
	 * The panel containing the edit buttons
	 */
	private final FlowPanel pnlButtonRow = new FlowPanel();

	private final Button btnSave, btnDelete, btnReset, btnCancel;

	/**
	 * the model/field binding action.
	 */
	private final IModelFieldBinding binding;

	private final EditListenerCollection editListeners = new EditListenerCollection();

	/**
	 * Constructor
	 * @param binding the model/field binding action
	 * @param showCancelBtn Show the cancel button? Causes a cancel edit event
	 *        when clicked.
	 * @param showDeleteBtn Show the delete button? Causes a delete edit event
	 *        when clicked.
	 */
	public EditPanel(IModelFieldBinding binding, boolean showCancelBtn, boolean showDeleteBtn) {

		if(binding == null) throw new IllegalArgumentException("A model/field binding must be specified.");
		this.binding = binding;

		portal.setStyleName(Styles.PORTAL);
		// we need to defer this until needed aux data is ready
		// portal.setWidget(fieldPanel);

		pnlButtonRow.setStyleName(Styles.BTN_ROW);

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
		panel.setStyleName(Styles.ENTITY_EDIT);

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

	/**
	 * Sets the model where if <code>null</code> all existing bindings are
	 * cleared.
	 * @param model The model to set
	 */
	public void setModel(Model model) {
		Log.debug("EditPanel.setModel() - START");
		binding.setModel(model);
		if(model != null) {
			assert isAttached() == true;
			setEditMode(model.isNew());
			// deferred attachment to guarantee needed aux data is available
			if(!binding.getRootFieldPanel().isAttached()) {
				Log.debug("EditPanel.setModel() adding fieldPanel to DOM..");
				portal.add(binding.getRootFieldPanel());
			}
		}
		Log.debug("EditPanel.setModel() - END");
	}

	/**
	 * Applies field error messages to the fields contained in the member
	 * {@link FieldPanel}.
	 * @param msgs The field error messages to apply
	 */
	public void applyFieldErrors(final List<Msg> msgs) {
		final FieldGroup root = binding.getRootFieldPanel().getFieldGroup();
		final MsgPopupRegistry mregistry = binding.getRootFieldPanel().getMsgPopupRegistry();
		for(final Msg msg : msgs) {
			if(msg.getRefToken() != null) {
				final Widget fw = root.getField(msg.getRefToken()).getWidget();
				if(fw == null) {
					throw new IllegalStateException("Unable to find field of property name: " + msg.getRefToken());
				}
				mregistry.addMsg(msg, fw, false);
			}
		}
	}

	public void onClick(ClickEvent event) {
		final Object sender = event.getSource();
		if(sender == btnSave) {
			//fieldPanel.getAction().execute();
			binding.execute();
			// TODO propagate
			editListeners.fireEditEvent(new EditEvent(this, isAdd() ? EditOp.ADD : EditOp.UPDATE));
		}
		else if(sender == btnReset) {
			binding.getRootFieldPanel().getFieldGroup().reset();
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
	}
}
