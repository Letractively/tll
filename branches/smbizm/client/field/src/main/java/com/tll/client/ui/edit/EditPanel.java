/**
 * The Logic Lab
 * @author jpk Nov 3, 2007
 */
package com.tll.client.ui.edit;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.FieldBindingAction;
import com.tll.client.ui.FocusCommand;
import com.tll.client.ui.edit.EditEvent.EditOp;
import com.tll.client.ui.field.FieldErrorHandler;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.msg.IMsgDisplay;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.client.validate.BillboardValidationFeedback;
import com.tll.client.validate.Error;
import com.tll.client.validate.ErrorClassifier;
import com.tll.client.validate.ErrorHandlerDelegate;
import com.tll.client.validate.IErrorHandler;
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
public final class EditPanel extends Composite implements ClickHandler, IHasEditHandlers {

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
	 * Contains the actual edit fields.
	 */
	private final FieldPanel<? extends Widget> fieldPanel;

	private final FieldBindingAction editAction;

	/**
	 * The panel containing the edit buttons
	 */
	private final FlowPanel pnlButtonRow = new FlowPanel();

	private final Button btnSave, btnDelete, btnReset, btnCancel;

	/**
	 * Constructor
	 * @param globalMsgDisplay Optional global message display employed for
	 *        providing field validation feedback
	 * @param fieldPanel The required {@link FieldPanel}
	 * @param showCancelBtn Show the cancel button? Causes a cancel edit event
	 *        when clicked.
	 * @param showDeleteBtn Show the delete button? Causes a delete edit event
	 *        when clicked.
	 */
	public EditPanel(IMsgDisplay globalMsgDisplay, FieldPanel<? extends Widget> fieldPanel, boolean showCancelBtn,
			boolean showDeleteBtn) {

		if(fieldPanel == null) throw new IllegalArgumentException("A field panel must be specified.");
		this.fieldPanel = fieldPanel;

		IErrorHandler errorHandler;
		if(globalMsgDisplay == null) {
			errorHandler = new FieldErrorHandler(new MsgPopupRegistry());
		}
		else {
			errorHandler =
					new ErrorHandlerDelegate(new BillboardValidationFeedback(globalMsgDisplay), new FieldErrorHandler(
							new MsgPopupRegistry()));
		}

		editAction = new FieldBindingAction(errorHandler);

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

	@Override
	public HandlerRegistration addEditHandler(IEditHandler handler) {
		return addHandler(handler, EditEvent.TYPE);
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
		fieldPanel.setModel(model);
		if(model != null) {
			// assert isAttached() == true;
			setEditMode(model.isNew());
			// deferred attachment to guarantee needed aux data is available
			if(!fieldPanel.isAttached()) {
				fieldPanel.setAction(editAction);
				Log.debug("EditPanel.setModel() adding fieldPanel to DOM..");
				portal.add(fieldPanel);
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
		final FieldGroup root = fieldPanel.getFieldGroup();
		final IErrorHandler ehandler = editAction.getErrorHandler();
		// clear out any existing server side errors
		ehandler.clear(ErrorClassifier.SERVER);
		for(final Msg msg : msgs) {
			final IFieldWidget<?> fw = root.getFieldWidget(msg.getRefToken());
			String emsg;
			if(fw != null) {
				emsg = msg.getMsg();
			}
			else {
				emsg = msg.getRefToken() + ": " + msg.getMsg();
			}
			ehandler.handleError(fw, new Error(ErrorClassifier.SERVER, emsg));
		}
	}

	public void onClick(ClickEvent event) {
		final Object sender = event.getSource();
		if(sender == btnSave) {
			try {
				Log.debug("EditPanel - Saving..");
				fieldPanel.getAction().execute();
				EditEvent.fire(this, isAdd() ? EditOp.ADD : EditOp.UPDATE);
			}
			catch(final Exception e) {
				String emsg = e.getMessage();
				if(emsg == null) {
					emsg = e.getClass().toString();
				}
				assert emsg != null;
				Log.error(emsg);
			}
		}
		else if(sender == btnReset) {
			fieldPanel.getFieldGroup().reset();
		}
		else if(sender == btnDelete) {
			EditEvent.fire(this, EditOp.UPDATE);
		}
		else if(sender == btnCancel) {
			EditEvent.fire(this, EditOp.CANCEL);
		}
	}

	@Override
	protected void onLoad() {
		Log.debug("EditPanel.onLoad()..");
		super.onLoad();
		if(btnCancel != null) {
			DeferredCommand.addCommand(new FocusCommand(btnCancel, true));
		}
	}

	@Override
	protected void onUnload() {
		Log.debug("EditPanel.onUnload()..");
		super.onUnload();
	}
}
