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
import com.tll.client.model.ModelChangeTracker;
import com.tll.client.ui.FocusCommand;
import com.tll.client.ui.Position;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.AbstractBindableFieldPanel;
import com.tll.client.ui.field.IFieldBoundWidget;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.NoChangesException;
import com.tll.client.ui.msg.IMsgDisplay;
import com.tll.client.ui.msg.Msgs;
import com.tll.client.validate.Error;
import com.tll.client.validate.ErrorClassifier;
import com.tll.client.validate.ErrorDisplay;
import com.tll.client.validate.ErrorHandlerDelegate;
import com.tll.client.validate.IErrorHandler;
import com.tll.client.validate.ValidationException;
import com.tll.common.model.Model;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * EditPanel - Composite panel targeting a {@link FlowPanel} whose children
 * consist of a {@link ScrollPanel} containing a {@link AbstractBindableFieldPanel} and another
 * {@link FlowPanel} containing edit buttons. The {@link ScrollPanel} enables
 * the the {@link AbstractBindableFieldPanel} content to always be navigable and keeps the edit
 * and cancel buttons in constant position.
 * @author jpk
 */
public class EditPanel extends Composite implements ClickHandler, IHasEditHandlers {

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
		 * Save style.
		 */
		public static final String SAVE = "save";
		/**
		 * Cancel style.
		 */
		public static final String CANCEL = "cancel";
		/**
		 * Delete style.
		 */
		public static final String DELETE = "delete";
		/**
		 * Reset style.
		 */
		public static final String RESET = "reset";
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
	 * This panel contains the {@link AbstractBindableFieldPanel}.
	 */
	private final SimplePanel portal = new SimplePanel();

	/**
	 * Contains the actual edit fields.
	 */
	protected final IFieldBoundWidget fieldPanel;

	/**
	 * Ref to the optional message display which is gotten from the error handler
	 * when set.
	 */
	protected IMsgDisplay msgDisplay;

	/**
	 * The panel containing the edit buttons
	 */
	private final FlowPanel pnlButtonRow = new FlowPanel();

	private final Button btnSave;

	private Button btnReset, btnDelete, btnCancel;

	/**
	 * Constructor
	 * @param fieldPanel The required {@link AbstractBindableFieldPanel}
	 * @param showCancelBtn Show the cancel button? Causes a cancel edit event
	 *        when clicked.
	 * @param showDeleteBtn Show the delete button? Causes a delete edit event
	 *        when clicked.
	 * @param showResetBtn Show the reset button?
	 */
	public EditPanel(IFieldBoundWidget fieldPanel, boolean showCancelBtn, boolean showDeleteBtn, boolean showResetBtn) {
		super();
		if(fieldPanel == null) throw new IllegalArgumentException("A field panel must be specified.");
		fieldPanel.setModelChangeTracker(new ModelChangeTracker());
		this.fieldPanel = fieldPanel;

		portal.setStyleName(Styles.PORTAL);
		// we need to defer this until needed model and aux data is loaded
		// portal.setWidget(fieldPanel);

		pnlButtonRow.setStyleName(Styles.BTN_ROW);

		btnSave = new Button("", this);
		btnSave.addStyleName(Styles.SAVE);
		pnlButtonRow.add(btnSave);

		showDeleteButton(showDeleteBtn);

		showCancelButton(showCancelBtn);

		// hide the button row until initialized
		pnlButtonRow.setVisible(false);

		panel.add(portal);
		panel.add(pnlButtonRow);
		panel.setStyleName(Styles.ENTITY_EDIT);

		initWidget(panel);
	}

	/**
	 * Sets the error handler for field validation feedback and optionally adds
	 * the message display to this panel.
	 * @param errorHandler the error handler to set
	 * @param addMsgDisplay add the held msg display to this panel?
	 */
	public void setErrorHandler(ErrorHandlerDelegate errorHandler, boolean addMsgDisplay) {
		fieldPanel.setErrorHandler(errorHandler);
		msgDisplay = errorHandler.getMsgDisplay();
		if(addMsgDisplay && msgDisplay != null) {
			panel.insert(msgDisplay.getDisplayWidget(), 0);
		}
	}
	
	public final void showResetButton(boolean show) {
		if(btnReset == null) {
			btnReset = new Button("Reset", this);
			btnReset.addStyleName(Styles.RESET);
			pnlButtonRow.add(btnReset);
		}
		btnReset.setVisible(show);
	}

	public final void showDeleteButton(boolean show) {
		if(btnDelete == null) {
			btnDelete = new Button("Delete", this);
			btnDelete.addStyleName(Styles.DELETE);
			pnlButtonRow.add(btnDelete);
		}
		btnDelete.setVisible(show);
	}

	public final void showCancelButton(boolean show) {
		if(btnCancel == null) {
			btnCancel = new Button("Cancel", this);
			btnCancel.addStyleName(Styles.CANCEL);
			pnlButtonRow.add(btnCancel);
		}
		btnCancel.setVisible(show);
	}

	@Override
	public final HandlerRegistration addEditHandler(IEditHandler handler) {
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

	public final Model getModel() {
		return fieldPanel.getModel();
	}

	/**
	 * Sets the model propagating it to the held field panel.
	 * @param model The model to set
	 */
	public void setModel(Model model) {
		Log.debug("EditPanel.setModel() - START");
		fieldPanel.setModel(model);
		if(model != null) {
			setEditMode(model.isNew());
			// deferred attachment to guaranting model and aux data is available
			final Widget w = (Widget) fieldPanel;
			if(w.getParent() == null) {
				Log.debug("EditPanel.setModel() attaching fieldPanel..");
				portal.add(w);
			}
		}
		Log.debug("EditPanel.setModel() - END");
	}

	/**
	 * Applies field error messages to the fields contained in the member
	 * {@link AbstractBindableFieldPanel}.
	 * @param msgs The field error messages to apply
	 * @param classifier the error classifier
	 * @param clearExisting Remove existing errors of the given error classifier
	 *        before applying?
	 */
	public final void applyFieldErrors(final List<Msg> msgs, ErrorClassifier classifier, boolean clearExisting) {
		final FieldGroup root;
		try {
			root = fieldPanel.getFieldGroup();
		}
		catch(final IllegalStateException e) {
			// presume field group not initialized yet
			return;
		}
		final IErrorHandler errorHandler = root.getErrorHandler();
		if(clearExisting) errorHandler.clear(classifier);
		for(final Msg msg : msgs) {
			final IFieldWidget<?> fw = root.getFieldWidgetByProperty(msg.getRefToken());
			String emsg;
			if(fw != null) {
				emsg = msg.getMsg();
			}
			else {
				emsg = msg.getRefToken() + ": " + msg.getMsg();
			}
			errorHandler.handleError(new Error(classifier, fw, emsg), ErrorDisplay.ALL_FLAGS);
		}
	}

	public final void onClick(ClickEvent event) {
		final Object sender = event.getSource();
		if(sender == btnSave) {
			try {
				Log.debug("EditPanel - Saving..");
				fieldPanel.updateModel();
				if(isAdd()) {
					EditEvent.fireAdd(this, fieldPanel.getModel());
				}
				else {
					final Model medited = fieldPanel.getModel();
					final Model mchanged = fieldPanel.getChangedModel();
					EditEvent.fireUpdate(this, medited, mchanged);
				}
			}
			catch(final NoChangesException e) {
				// no field edits were made
				Msgs.post(new Msg("No changes detected.", MsgLevel.INFO), this, Position.CENTER, 3000, true);
			}
			catch(final ValidationException e) {
				// turn on incremental validation after first pass
				fieldPanel.getFieldGroup().validateIncrementally(true);
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
			fieldPanel.reset();
		}
		else if(sender == btnDelete) {
			EditEvent.fireDelete(this);
		}
		else if(sender == btnCancel) {
			EditEvent.fireCancel(this);
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
